package ht.uep.edupro_uep.fiop;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PopulationViseeRepository extends JpaRepository<PopulationVisee, Integer> {

    List<PopulationVisee> findByIdProjet(Integer idProjet);

    void deleteByIdProjet(Integer idProjet);
}
