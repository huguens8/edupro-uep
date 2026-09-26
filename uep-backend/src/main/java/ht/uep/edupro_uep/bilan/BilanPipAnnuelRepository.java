package ht.uep.edupro_uep.bilan;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BilanPipAnnuelRepository extends JpaRepository<BilanPipAnnuel, Integer> {

    List<BilanPipAnnuel> findByIdBilan(Integer idBilan);

    void deleteByIdBilan(Integer idBilan);
}
