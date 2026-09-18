package ht.uep.edupro_uep.fiop;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IndicateurResultatRepository extends JpaRepository<IndicateurResultat, Integer> {

    List<IndicateurResultat> findByIdProjetOrderByOrdreAsc(Integer idProjet);

    void deleteByIdProjet(Integer idProjet);
}
