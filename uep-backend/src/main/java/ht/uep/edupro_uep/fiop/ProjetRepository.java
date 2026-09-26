package ht.uep.edupro_uep.fiop;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProjetRepository extends JpaRepository<Projet, Integer> {

    /**
     * Une ligne par (département, statut) : [id_departement, libelle, statut, nombre].
     * Département null = non renseigné ; statut = valeur stockée en base.
     */
    @Query(value = "select p.id_departement, d.libelle, p.statut, count(*) from projet p"
            + " left join departement d on d.id_departement = p.id_departement"
            + " group by p.id_departement, d.libelle, p.statut", nativeQuery = true)
    List<Object[]> countProjetsParDepartementEtStatut();

    List<Projet> findByStatutAndCodeInternePipIsNull(StatutFiop statut);
}
