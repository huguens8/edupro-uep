package ht.uep.edupro_uep.reference;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SourceFinancementRepository extends JpaRepository<SourceFinancement, Integer> {

    List<SourceFinancement> findAllByOrderByCategorieAscLibelleAsc();
}
