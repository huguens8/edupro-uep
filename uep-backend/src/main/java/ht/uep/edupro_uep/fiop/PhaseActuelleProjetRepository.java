package ht.uep.edupro_uep.fiop;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PhaseActuelleProjetRepository extends JpaRepository<PhaseActuelleProjet, Integer> {

    List<PhaseActuelleProjet> findByIdProjetOrderByDateChangementAsc(Integer idProjet);

    void deleteByIdProjet(Integer idProjet);
}
