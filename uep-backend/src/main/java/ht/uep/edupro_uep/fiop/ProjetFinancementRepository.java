package ht.uep.edupro_uep.fiop;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjetFinancementRepository extends JpaRepository<ProjetFinancement, Integer> {

    List<ProjetFinancement> findByIdProjet(Integer idProjet);

    void deleteByIdProjet(Integer idProjet);
}
