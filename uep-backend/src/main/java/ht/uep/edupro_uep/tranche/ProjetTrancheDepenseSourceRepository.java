package ht.uep.edupro_uep.tranche;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjetTrancheDepenseSourceRepository extends JpaRepository<ProjetTrancheDepenseSource, Integer> {

    List<ProjetTrancheDepenseSource> findByIdProjet(Integer idProjet);

    List<ProjetTrancheDepenseSource> findByIdProjetAndIdExercice(Integer idProjet, Integer idExercice);

    boolean existsByIdProjetAndIdExercice(Integer idProjet, Integer idExercice);

    void deleteByIdProjetAndIdExercice(Integer idProjet, Integer idExercice);
}
