package ht.uep.edupro_uep.reference;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RubriqueBudgetaireRepository extends JpaRepository<RubriqueBudgetaire, Integer> {

    List<RubriqueBudgetaire> findAllByOrderByCodeAsc();

    List<RubriqueBudgetaire> findByNiveauOrderByCodeAsc(Short niveau);
}
