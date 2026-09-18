package ht.uep.edupro_uep.fiop;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjetCoutRecurrentRepository extends JpaRepository<ProjetCoutRecurrent, Integer> {

    List<ProjetCoutRecurrent> findByIdProjet(Integer idProjet);

    void deleteByIdProjet(Integer idProjet);
}
