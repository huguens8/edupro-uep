package ht.uep.edupro_uep.tranche;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjetTrancheVoieMoyenRepository extends JpaRepository<ProjetTrancheVoieMoyen, Integer> {

    List<ProjetTrancheVoieMoyen> findByIdProjetAndIdExerciceOrderByOrdreAsc(Integer idProjet, Integer idExercice);

    void deleteByIdProjetAndIdExercice(Integer idProjet, Integer idExercice);
}
