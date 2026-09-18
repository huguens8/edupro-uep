package ht.uep.edupro_uep.geo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartementRepository extends JpaRepository<Departement, String> {

    List<Departement> findAllByOrderByLibelleAsc();
}
