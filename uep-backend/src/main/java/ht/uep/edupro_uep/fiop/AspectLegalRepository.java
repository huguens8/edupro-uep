package ht.uep.edupro_uep.fiop;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AspectLegalRepository extends JpaRepository<AspectLegal, Integer> {

    List<AspectLegal> findByIdProjetOrderByOrdreAsc(Integer idProjet);

    void deleteByIdProjet(Integer idProjet);
}
