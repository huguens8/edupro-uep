package ht.uep.edupro_uep.bilan;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BilanAvancementPhysiqueRepository extends JpaRepository<BilanAvancementPhysique, Integer> {

    List<BilanAvancementPhysique> findByIdBilan(Integer idBilan);

    void deleteByIdBilan(Integer idBilan);

    boolean existsByIdActivite(Integer idActivite);

    void deleteByIdBilanAndIdActivite(Integer idBilan, Integer idActivite);
}
