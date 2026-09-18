package ht.uep.edupro_uep.bilan;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BilanDepensePrevisionnelleSourceRepository extends JpaRepository<BilanDepensePrevisionnelleSource, Integer> {

    List<BilanDepensePrevisionnelleSource> findByIdBilan(Integer idBilan);

    void deleteByIdBilan(Integer idBilan);
}
