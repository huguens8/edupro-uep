package ht.uep.edupro_uep.fiop;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjetIntrantBudgetPhaseRepository extends JpaRepository<ProjetIntrantBudgetPhase, Integer> {

    List<ProjetIntrantBudgetPhase> findByIdProjet(Integer idProjet);

    void deleteByIdProjet(Integer idProjet);
}
