package ht.uep.edupro_uep.bilan;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BilanExerciceRepository extends JpaRepository<BilanExercice, Integer> {

    List<BilanExercice> findByIdProjetOrderByIdExerciceDesc(Integer idProjet);

    boolean existsByIdProjetAndIdExercice(Integer idProjet, Integer idExercice);

    boolean existsByIdProjetAndIdExerciceAndIdNot(Integer idProjet, Integer idExercice, Integer id);
}
