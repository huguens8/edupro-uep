package ht.uep.edupro_uep.tranche;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ActiviteChronogrammeMensuelRepository extends JpaRepository<ActiviteChronogrammeMensuel, Integer> {

    List<ActiviteChronogrammeMensuel> findByIdActiviteInAndIdExercice(Collection<Integer> idActivites, Integer idExercice);

    void deleteByIdActiviteInAndIdExercice(Collection<Integer> idActivites, Integer idExercice);
}
