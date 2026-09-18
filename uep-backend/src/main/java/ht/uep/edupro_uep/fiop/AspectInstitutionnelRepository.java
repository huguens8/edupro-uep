package ht.uep.edupro_uep.fiop;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AspectInstitutionnelRepository extends JpaRepository<AspectInstitutionnel, Integer> {

    List<AspectInstitutionnel> findByIdProjetOrderByOrdreAsc(Integer idProjet);

    void deleteByIdProjet(Integer idProjet);
}
