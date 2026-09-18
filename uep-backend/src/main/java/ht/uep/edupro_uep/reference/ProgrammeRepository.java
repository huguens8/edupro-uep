package ht.uep.edupro_uep.reference;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProgrammeRepository extends JpaRepository<Programme, Integer> {

    List<Programme> findByIdGrandChantierOrderByLibelleAsc(Integer idGrandChantier);
}
