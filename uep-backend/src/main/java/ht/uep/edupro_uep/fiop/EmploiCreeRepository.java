package ht.uep.edupro_uep.fiop;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EmploiCreeRepository extends JpaRepository<EmploiCree, Integer> {

    List<EmploiCree> findByIdProjet(Integer idProjet);

    void deleteByIdProjet(Integer idProjet);
}
