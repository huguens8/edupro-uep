package ht.uep.edupro_uep.tranche;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjetCalendrierDepenseTrimestrielleRepository extends JpaRepository<ProjetCalendrierDepenseTrimestrielle, Integer> {

    List<ProjetCalendrierDepenseTrimestrielle> findByIdProjetAndIdExercice(Integer idProjet, Integer idExercice);

    void deleteByIdProjetAndIdExercice(Integer idProjet, Integer idExercice);
}
