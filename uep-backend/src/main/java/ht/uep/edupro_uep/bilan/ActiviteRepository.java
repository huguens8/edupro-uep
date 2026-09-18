package ht.uep.edupro_uep.bilan;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ActiviteRepository extends JpaRepository<Activite, Integer> {

    List<Activite> findByIdProjetOrderByOrdreSequentielAsc(Integer idProjet);

    Optional<Activite> findByIdProjetAndOrdreSequentiel(Integer idProjet, Short ordreSequentiel);

    void deleteByIdProjet(Integer idProjet);
}
