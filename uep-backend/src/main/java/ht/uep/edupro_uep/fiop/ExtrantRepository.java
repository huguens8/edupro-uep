package ht.uep.edupro_uep.fiop;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ExtrantRepository extends JpaRepository<Extrant, Integer> {

    List<Extrant> findByIdProjetOrderByOrdreAsc(Integer idProjet);

    void deleteByIdProjet(Integer idProjet);
}
