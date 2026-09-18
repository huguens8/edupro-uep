package ht.uep.edupro_uep.fiop;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjetCalendrierDepenseAnnuelleRepository extends JpaRepository<ProjetCalendrierDepenseAnnuelle, Integer> {

    List<ProjetCalendrierDepenseAnnuelle> findByIdProjet(Integer idProjet);

    void deleteByIdProjet(Integer idProjet);
}
