package ht.uep.edupro_uep.fiop;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

/** § 34 de la FIOP : colonne "# d'ordre des Activités" — une valeur par (projet, sous-rubrique), pas par année. */
@Entity
@Table(name = "projet_calendrier_rubrique_activites")
public class ProjetCalendrierRubriqueOrdreActivites {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "calendrier_ordre_activites_seq")
    @SequenceGenerator(name = "calendrier_ordre_activites_seq", sequenceName = "projet_calendrier_rubrique_activites_id_ordre_activites_seq", allocationSize = 1)
    @Column(name = "id_ordre_activites")
    private Integer id;

    @Column(name = "id_projet", nullable = false)
    private Integer idProjet;

    @Column(name = "id_rubrique", nullable = false)
    private Integer idRubrique;

    @Column(name = "ordre_activites")
    private String ordreActivites;

    public Integer getId() {
        return id;
    }

    public Integer getIdProjet() {
        return idProjet;
    }

    public void setIdProjet(Integer idProjet) {
        this.idProjet = idProjet;
    }

    public Integer getIdRubrique() {
        return idRubrique;
    }

    public void setIdRubrique(Integer idRubrique) {
        this.idRubrique = idRubrique;
    }

    public String getOrdreActivites() {
        return ordreActivites;
    }

    public void setOrdreActivites(String ordreActivites) {
        this.ordreActivites = ordreActivites;
    }
}
