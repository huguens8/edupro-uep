package ht.uep.edupro_uep.user;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import ht.uep.edupro_uep.audit.AuditAction;
import ht.uep.edupro_uep.audit.AuditLogService;
import ht.uep.edupro_uep.dto.CreateUserRequest;
import ht.uep.edupro_uep.dto.LoginResponse;
import ht.uep.edupro_uep.dto.UpdateUserRequest;
import ht.uep.edupro_uep.dto.UserResponse;
import ht.uep.edupro_uep.security.JwtService;
import ht.uep.edupro_uep.security.RefreshTokenService;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final AuditLogService auditLogService;
    private final int maxFailedAttempts;
    private final long lockoutMinutes;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            RefreshTokenService refreshTokenService,
            AuditLogService auditLogService,
            @Value("${app.auth.max-failed-attempts}") int maxFailedAttempts,
            @Value("${app.auth.lockout-minutes}") long lockoutMinutes) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
        this.auditLogService = auditLogService;
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

        String accessToken = jwtService.generateToken(user.getUsername(), user.getRole().name());
        String rawRefreshToken = refreshTokenService.issue(user);
        LoginResponse response = new LoginResponse(
                accessToken, user.getUsername(), user.getRole().name(), user.getRole().getLibelle());
        return new LoginResult(response, rawRefreshToken);
    }

    /** Génère un nouvel access token pour un utilisateur déjà authentifié (utilisé par /api/auth/refresh). */
    public String generateAccessToken(User user) {
        return jwtService.generateToken(user.getUsername(), user.getRole().name());
    }

    public UserResponse createUser(CreateUserRequest request, String currentUsername) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalStateException("Ce nom d'utilisateur existe déjà.");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalStateException("Cet email est déjà utilisé.");
        }

        User user = new User(
                request.getUsername(),
                passwordEncoder.encode(request.getPassword()),
                request.getEmail(),
                request.getRole());
        userRepository.save(user);
        auditLogService.log(currentUsername, AuditAction.USER_CREATED, "User", user.getId(),
                "Rôle : " + user.getRole().name());
        return toResponse(user);
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

    public UserResponse resetPassword(Integer id, String newPassword, String currentUsername) {
        User user = findUserOrThrow(id);
        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        auditLogService.log(currentUsername, AuditAction.PASSWORD_RESET, "User", user.getId(), null);
        return toResponse(user);
    }

    public void deleteUser(Integer id, String currentUsername) {
        User user = findUserOrThrow(id);

        if (user.getUsername().equals(currentUsername)) {
            throw new IllegalStateException("Vous ne pouvez pas supprimer votre propre compte.");
        }
        if (user.getRole() == Role.ADMINISTRATEUR && countActiveAdmins() <= 1) {
            throw new IllegalStateException(
                    "Impossible de supprimer ce compte : il doit rester au moins un administrateur actif.");
        }

        auditLogService.log(currentUsername, AuditAction.USER_DELETED, "User", user.getId(),
                user.getUsername() + " (" + user.getEmail() + ")");
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

    private UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole().name(),
                user.getRole().getLibelle(),
                user.isActive());
    }
}
