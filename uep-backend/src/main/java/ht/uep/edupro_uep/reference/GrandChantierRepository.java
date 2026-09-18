package ht.uep.edupro_uep.reference;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GrandChantierRepository extends JpaRepository<GrandChantier, Integer> {

    List<GrandChantier> findAllByOrderByLibelleAsc();
}
