package ht.uep.edupro_uep.bilan;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BilanAvancementBudgetNationalRepository extends JpaRepository<BilanAvancementBudgetNational, Integer> {

    List<BilanAvancementBudgetNational> findByIdBilan(Integer idBilan);

    void deleteByIdBilan(Integer idBilan);
}
