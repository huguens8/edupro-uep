package ht.uep.edupro_uep.security;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {

    Optional<RefreshToken> findByTokenHash(String tokenHash);

    /**
     * Révoque le token uniquement s'il est encore valide, de façon atomique
     * (verrou de ligne côté base). Retourne le nombre de lignes affectées :
     * 0 signifie que le token a déjà été révoqué/consommé par une autre
     * requête concurrente (ex. double appel de refresh), ce qui doit être
     * traité comme un token invalide plutôt que de laisser une rotation
     * en doublon silencieuse.
     */
    @Modifying
    @Query("update RefreshToken rt set rt.revoked = true, rt.revokedAt = CURRENT_TIMESTAMP "
            + "where rt.tokenHash = :hash and rt.revoked = false and rt.expiresAt > CURRENT_TIMESTAMP")
    int revokeIfValid(@Param("hash") String hash);
}
