package ht.uep.edupro_uep.geo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CommuneRepository extends JpaRepository<Commune, String> {

    List<Commune> findByIdArrondissementOrderByLibelleAsc(String idArrondissement);
}
