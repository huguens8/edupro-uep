package ht.uep.edupro_uep.geo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ArrondissementRepository extends JpaRepository<Arrondissement, String> {

    List<Arrondissement> findByIdDepartementOrderByLibelleAsc(String idDepartement);
}
