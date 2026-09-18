package ht.uep.edupro_uep.bilan;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivitePlanificationAnnuelleRepository extends JpaRepository<ActivitePlanificationAnnuelle, Integer> {

    List<ActivitePlanificationAnnuelle> findByIdActiviteOrderByAnneeNumeroAsc(Integer idActivite);

    void deleteByIdActivite(Integer idActivite);
}
