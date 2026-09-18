package ht.uep.edupro_uep.fiop;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjetCalendrierRubriqueOrdreActivitesRepository extends JpaRepository<ProjetCalendrierRubriqueOrdreActivites, Integer> {

    List<ProjetCalendrierRubriqueOrdreActivites> findByIdProjet(Integer idProjet);

    void deleteByIdProjet(Integer idProjet);
}
