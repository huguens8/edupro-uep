package ht.uep.edupro_uep.reference;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SousProgrammeRepository extends JpaRepository<SousProgramme, Integer> {

    List<SousProgramme> findByIdProgrammeOrderByLibelleAsc(Integer idProgramme);
}
