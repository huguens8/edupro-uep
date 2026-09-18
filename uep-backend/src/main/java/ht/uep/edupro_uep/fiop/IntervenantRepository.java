package ht.uep.edupro_uep.fiop;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IntervenantRepository extends JpaRepository<Intervenant, Integer> {

    List<Intervenant> findByIdProjet(Integer idProjet);

    void deleteByIdProjet(Integer idProjet);
}
