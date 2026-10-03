package ht.uep.edupro_uep.ptf;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PtfRepository extends JpaRepository<Ptf, Integer> {

    List<Ptf> findAllByOrderByNomAsc();

    /** Vrai si le PTF est cité comme intervenant (bailleur) d'un projet. */
    @Query(value = "select exists (select 1 from projet_intervenant where id_ptf = :id)", nativeQuery = true)
    boolean isEngageSurUnProjet(@Param("id") Integer id);
}
