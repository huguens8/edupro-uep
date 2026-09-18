package ht.uep.edupro_uep.reference;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ExerciceBudgetaireRepository extends JpaRepository<ExerciceBudgetaire, Integer> {

    List<ExerciceBudgetaire> findAllByOrderByLibelleDesc();
}
