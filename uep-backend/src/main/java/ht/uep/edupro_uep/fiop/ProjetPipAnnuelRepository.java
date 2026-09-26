package ht.uep.edupro_uep.fiop;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjetPipAnnuelRepository extends JpaRepository<ProjetPipAnnuel, Integer> {

    List<ProjetPipAnnuel> findByIdProjetOrderByAnneeNumeroAsc(Integer idProjet);

    void deleteByIdProjet(Integer idProjet);
}
