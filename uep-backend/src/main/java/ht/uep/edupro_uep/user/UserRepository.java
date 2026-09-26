package ht.uep.edupro_uep.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    /** Vrai si le compte est tracé comme auteur d'un projet ou d'une pièce d'archive. */
    @Query(value = "select exists (select 1 from projet where id_utilisateur_creation = :id"
            + " or id_utilisateur_derniere_maj = :id)"
            + " or exists (select 1 from archive_projet where id_utilisateur_ajout = :id)", nativeQuery = true)
    boolean isReferencedByProjets(@Param("id") Integer id);
}
