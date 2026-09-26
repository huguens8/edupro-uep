package ht.uep.edupro_uep.user;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ht.uep.edupro_uep.audit.AuditAction;
import ht.uep.edupro_uep.audit.AuditLogService;
import ht.uep.edupro_uep.dto.CreateUserRequest;
import ht.uep.edupro_uep.dto.LoginResponse;
import ht.uep.edupro_uep.dto.UpdateUserRequest;
import ht.uep.edupro_uep.dto.UserResponse;
import ht.uep.edupro_uep.mail.AccountMailService;
import ht.uep.edupro_uep.security.JwtService;
import ht.uep.edupro_uep.security.RefreshTokenRepository;
import ht.uep.edupro_uep.security.RefreshTokenService;

@Service
public class UserService {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy à HH:mm");
    private static final SecureRandom RANDOM = new SecureRandom();

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final AuditLogService auditLogService;
    private final AccountMailService accountMailService;
    private final int maxFailedAttempts;
    private final long lockoutMinutes;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            RefreshTokenService refreshTokenService,
            RefreshTokenRepository refreshTokenRepository,
            AuditLogService auditLogService,
            AccountMailService accountMailService,
            @Value("${app.auth.max-failed-attempts}") int maxFailedAttempts,
            @Value("${app.auth.lockout-minutes}") long lockoutMinutes) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
        this.refreshTokenRepository = refreshTokenRepository;
        this.auditLogService = auditLogService;
        this.accountMailService = accountMailService;
        this.maxFailedAttempts = maxFailedAttempts;
        this.lockoutMinutes = lockoutMinutes;
    }

    /** Résultat d'une connexion réussie : la réponse à renvoyer au client et le refresh token à poser en cookie. */
    public record LoginResult(LoginResponse loginResponse, String rawRefreshToken) {
    }

    public LoginResult login(String email, String rawPassword) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            auditLogService.log(email, AuditAction.LOGIN_FAILURE, "User", null, "Compte inconnu.");
            throw new BadCredentialsException("Identifiants incorrects.");
        }

        if (user.getLockedUntil() != null && user.getLockedUntil().isAfter(LocalDateTime.now())) {
            auditLogService.log(user.getUsername(), AuditAction.ACCOUNT_LOCKED, "User", user.getId(),
                    "Tentative de connexion pendant le verrouillage.");
            throw new LockedException(
                    "Compte verrouillé suite à trop de tentatives. Réessayez après " + user.getLockedUntil() + ".");
        }

        if (!user.isActive()) {
            auditLogService.log(user.getUsername(), AuditAction.LOGIN_DISABLED_ATTEMPT, "User", user.getId(), null);
            throw new DisabledException("Ce compte est désactivé.");
        }

        if (!passwordEncoder.matches(rawPassword, user.getPasswordHash())) {
            user.setFailedLoginAttempts(user.getFailedLoginAttempts() + 1);
            if (user.getFailedLoginAttempts() >= maxFailedAttempts) {
                user.setLockedUntil(LocalDateTime.now().plusMinutes(lockoutMinutes));
                user.setFailedLoginAttempts(0);
                userRepository.save(user);
                auditLogService.log(user.getUsername(), AuditAction.ACCOUNT_LOCKED, "User", user.getId(),
                        "Verrouillage après " + maxFailedAttempts + " échecs consécutifs.");
                throw new LockedException(
                        "Compte verrouillé suite à trop de tentatives. Réessayez dans " + lockoutMinutes + " minutes.");
            }
            userRepository.save(user);
            auditLogService.log(user.getUsername(), AuditAction.LOGIN_FAILURE, "User", user.getId(),
                    "Mot de passe incorrect (" + user.getFailedLoginAttempts() + "/" + maxFailedAttempts + ").");
            throw new BadCredentialsException("Identifiants incorrects.");
        }

        user.setFailedLoginAttempts(0);
        user.setLockedUntil(null);
        user.setDateDerniereConnexion(LocalDateTime.now());
        userRepository.save(user);
        auditLogService.log(user.getUsername(), AuditAction.LOGIN_SUCCESS, "User", user.getId(), null);

        return new LoginResult(toLoginResponse(user), refreshTokenService.issue(user));
    }

    /** Réponse de connexion (access token + identité) pour un utilisateur authentifié ; utilisée aussi par /api/auth/refresh. */
    public LoginResponse toLoginResponse(User user) {
        String accessToken = jwtService.generateToken(
                user.getUsername(), user.getRole().name(), user.isPasswordChangeRequired());
        return new LoginResponse(accessToken, user.getUsername(), user.getRole().name(),
                user.getRole().getLibelle(), user.isPasswordChangeRequired());
    }

    /**
     * Seul l'administrateur crée les comptes. Le mot de passe n'est pas saisi : un mot de
     * passe temporaire est généré et envoyé par e-mail au titulaire, qui devra le changer
     * à sa première connexion. Si l'e-mail ne part pas, la création est annulée.
     */
    @Transactional
    public UserResponse createUser(CreateUserRequest request, String currentUsername) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalStateException("Ce nom d'utilisateur existe déjà.");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalStateException("Cet email est déjà utilisé.");
        }

        String temporaryPassword = generateTemporaryPassword();
        User user = new User(
                request.getUsername(),
                passwordEncoder.encode(temporaryPassword),
                request.getEmail(),
                request.getRole());
        user.setPasswordChangeRequired(true);
        userRepository.save(user);
        accountMailService.sendAccountCreated(user, temporaryPassword);
        auditLogService.log(currentUsername, AuditAction.USER_CREATED, "User", user.getId(),
                "Rôle : " + user.getRole().name() + " ; identifiants envoyés à " + user.getEmail());
        return toResponse(user);
    }

    /**
     * Changement de mot de passe par l'utilisateur connecté (obligatoire après la création
     * du compte). Les autres sessions sont fermées ; une nouvelle session est ouverte.
     */
    @Transactional
    public LoginResult changePassword(String username, String currentPassword, String newPassword) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("Utilisateur introuvable."));

        if (!passwordEncoder.matches(currentPassword, user.getPasswordHash())) {
            throw new IllegalStateException("Le mot de passe actuel est incorrect.");
        }
        if (passwordEncoder.matches(newPassword, user.getPasswordHash())) {
            throw new IllegalStateException("Le nouveau mot de passe doit être différent de l'actuel.");
        }

        user.setPasswordHash(passwordEncoder.encode(newPassword));
        user.setPasswordChangeRequired(false);
        userRepository.save(user);
        refreshTokenRepository.deleteByUser(user);
        auditLogService.log(user.getUsername(), AuditAction.PASSWORD_CHANGED, "User", user.getId(), null);
        return new LoginResult(toLoginResponse(user), refreshTokenService.issue(user));
    }

    /** 12 caractères sans ambiguïté visuelle (pas de 0/O, 1/l/I), avec au moins un chiffre. */
    private String generateTemporaryPassword() {
        String letters = "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnpqrstuvwxyz";
        String digits = "23456789";
        String all = letters + digits;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 11; i++) {
            sb.append(all.charAt(RANDOM.nextInt(all.length())));
        }
        sb.insert(RANDOM.nextInt(sb.length() + 1), digits.charAt(RANDOM.nextInt(digits.length())));
        return sb.toString();
    }

    public List<UserResponse> listUsers() {
        return userRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public UserResponse updateRole(Integer id, Role newRole, String currentUsername) {
        User user = findUserOrThrow(id);

        if (user.getUsername().equals(currentUsername) && newRole != Role.ADMINISTRATEUR) {
            throw new IllegalStateException("Vous ne pouvez pas retirer votre propre rôle Administrateur.");
        }
        if (user.getRole() == Role.ADMINISTRATEUR && newRole != Role.ADMINISTRATEUR && countActiveAdmins() <= 1) {
            throw new IllegalStateException(
                    "Impossible de changer ce rôle : il doit rester au moins un administrateur actif.");
        }

        Role oldRole = user.getRole();
        user.setRole(newRole);
        userRepository.save(user);
        auditLogService.log(currentUsername, AuditAction.ROLE_CHANGED, "User", user.getId(),
                oldRole.name() + " -> " + newRole.name());
        return toResponse(user);
    }

    public UserResponse updateStatus(Integer id, boolean active, String currentUsername) {
        User user = findUserOrThrow(id);

        if (user.getUsername().equals(currentUsername) && !active) {
            throw new IllegalStateException("Vous ne pouvez pas désactiver votre propre compte.");
        }
        if (!active && user.getRole() == Role.ADMINISTRATEUR && countActiveAdmins() <= 1) {
            throw new IllegalStateException(
                    "Impossible de désactiver ce compte : il doit rester au moins un administrateur actif.");
        }

        user.setActive(active);
        userRepository.save(user);
        auditLogService.log(currentUsername, AuditAction.STATUS_CHANGED, "User", user.getId(),
                active ? "Compte activé." : "Compte désactivé.");
        return toResponse(user);
    }

    public UserResponse updateUser(Integer id, UpdateUserRequest request, String currentUsername) {
        User user = findUserOrThrow(id);

        if (!user.getUsername().equals(request.getUsername())
                && userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalStateException("Ce nom d'utilisateur existe déjà.");
        }
        if (!user.getEmail().equals(request.getEmail()) && userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalStateException("Cet email est déjà utilisé.");
        }

        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        userRepository.save(user);
        auditLogService.log(currentUsername, AuditAction.USER_UPDATED, "User", user.getId(), null);
        return toResponse(user);
    }

    @Transactional
    public void deleteUser(Integer id, String currentUsername) {
        User user = findUserOrThrow(id);

        if (user.getUsername().equals(currentUsername)) {
            throw new IllegalStateException("Vous ne pouvez pas supprimer votre propre compte.");
        }
        if (user.getRole() == Role.ADMINISTRATEUR && countActiveAdmins() <= 1) {
            throw new IllegalStateException(
                    "Impossible de supprimer ce compte : il doit rester au moins un administrateur actif.");
        }
        String blockedReason = deletionBlockedReason(user);
        if (blockedReason != null) {
            throw new IllegalStateException(blockedReason);
        }

        auditLogService.log(currentUsername, AuditAction.USER_DELETED, "User", user.getId(),
                user.getUsername() + " (" + user.getEmail() + ")");
        // Les sessions (refresh tokens) référencent le compte : on les supprime d'abord.
        refreshTokenRepository.deleteByUser(user);
        userRepository.delete(user);
    }

    private User findUserOrThrow(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Compte introuvable."));
    }

    private long countActiveAdmins() {
        return userRepository.findAll().stream()
                .filter(u -> u.getRole() == Role.ADMINISTRATEUR && u.isActive())
                .count();
    }

    /**
     * Raison pour laquelle le compte ne peut pas être supprimé (null s'il peut l'être).
     * Seul un compte jamais utilisé est supprimable : les autres se désactivent, pour garder la traçabilité.
     */
    private String deletionBlockedReason(User user) {
        if (user.getDateDerniereConnexion() != null) {
            return "Ce compte a déjà été utilisé (dernière connexion le "
                    + user.getDateDerniereConnexion().format(DATE_FORMAT)
                    + ") : le supprimer effacerait la trace de son activité. Désactivez-le plutôt.";
        }
        if (userRepository.isReferencedByProjets(user.getId())) {
            return "Ce compte est l'auteur de projets ou de documents archivés : le supprimer ferait perdre "
                    + "la traçabilité de ces projets. Désactivez-le plutôt.";
        }
        return null;
    }

    private UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole().name(),
                user.getRole().getLibelle(),
                user.isActive(),
                deletionBlockedReason(user));
    }
}
