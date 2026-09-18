package ht.uep.edupro_uep.bilan;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BilanAvancementFinancierRepository extends JpaRepository<BilanAvancementFinancier, Integer> {

    List<BilanAvancementFinancier> findByIdBilan(Integer idBilan);

    void deleteByIdBilan(Integer idBilan);

    void deleteByIdBilanAndIdActivite(Integer idBilan, Integer idActivite);
}
