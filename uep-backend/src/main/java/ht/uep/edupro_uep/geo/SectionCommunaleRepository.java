package ht.uep.edupro_uep.geo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SectionCommunaleRepository extends JpaRepository<SectionCommunale, String> {

    List<SectionCommunale> findByIdCommuneOrderByLibelleAsc(String idCommune);
}
