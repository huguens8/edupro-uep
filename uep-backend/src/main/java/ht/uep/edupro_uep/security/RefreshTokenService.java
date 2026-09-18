package ht.uep.edupro_uep.security;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ht.uep.edupro_uep.user.User;

/**
 * Émission, rotation et révocation des refresh tokens. Seul le hash SHA-256
 * du token est persisté (voir RefreshToken) ; la valeur brute n'est jamais
 * stockée, uniquement renvoyée une fois au client (cookie httpOnly).
 */
@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final long refreshExpirationMs;
    private final SecureRandom secureRandom = new SecureRandom();

    public RefreshTokenService(
            RefreshTokenRepository refreshTokenRepository,
            @Value("${app.jwt.refresh-expiration-ms}") long refreshExpirationMs) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.refreshExpirationMs = refreshExpirationMs;
    }

    public record Rotated(String rawToken, User user) {
    }

    /** Émet un nouveau refresh token pour l'utilisateur et retourne sa valeur brute. */
    public String issue(User user) {
        byte[] randomBytes = new byte[32];
        secureRandom.nextBytes(randomBytes);
        String rawToken = Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);

        RefreshToken token = new RefreshToken(
                user, hash(rawToken), LocalDateTime.now().plusNanos(refreshExpirationMs * 1_000_000));
        refreshTokenRepository.save(token);
        return rawToken;
    }

    /**
     * Révoque le token présenté et en émet un nouveau (rotation à chaque usage).
     * La révocation est une opération atomique en base : si deux requêtes
     * présentent le même token en même temps (ex. double appel de refresh),
     * une seule "gagne" la rotation, l'autre reçoit une erreur claire au lieu
     * de générer un second token valide en doublon.
     */
    @Transactional
    public Rotated rotate(String rawToken) {
        String tokenHash = hash(rawToken);
        RefreshToken existing = refreshTokenRepository.findByTokenHash(tokenHash)
                .orElseThrow(() -> new BadCredentialsException("Session invalide ou expirée."));

        int revokedRows = refreshTokenRepository.revokeIfValid(tokenHash);
        if (revokedRows == 0) {
            throw new BadCredentialsException("Session invalide ou expirée.");
        }

        User user = existing.getUser();
        return new Rotated(issue(user), user);
    }

    /** Révocation best-effort : un logout doit toujours réussir côté client. */
    public void revoke(String rawToken) {
        refreshTokenRepository.findByTokenHash(hash(rawToken)).ifPresent(token -> {
            token.setRevoked(true);
            token.setRevokedAt(LocalDateTime.now());
            refreshTokenRepository.save(token);
        });
    }

    private String hash(String rawToken) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashed = digest.digest(rawToken.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hashed);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 indisponible", e);
        }
    }
}
