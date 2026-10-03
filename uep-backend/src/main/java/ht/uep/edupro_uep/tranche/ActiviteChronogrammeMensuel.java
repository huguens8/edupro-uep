package ht.uep.edupro_uep.tranche;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

/**
 * § 46 de la FIOP : "Chronogramme d'exécution des activités" — un mois (OCT. à SEPT., année
 * fiscale haïtienne) où une activité est planifiée pour l'exercice. Seuls les mois cochés sont
 * enregistrés.
 */
@Entity
@Table(name = "activite_chronogramme_mensuel")
public class ActiviteChronogrammeMensuel {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "activite_chronogramme_mensuel_seq")
    @SequenceGenerator(name = "activite_chronogramme_mensuel_seq", sequenceName = "activite_chronogramme_mensuel_id_chrono_seq", allocationSize = 1)
    @Column(name = "id_chrono")
    private Integer id;

    @Column(name = "id_activite", nullable = false)
    private Integer idActivite;

    @Column(name = "id_exercice", nullable = false)
    private Integer idExercice;

    @Column(nullable = false)
    private Short trimestre;

    @Column(nullable = false, length = 15)
    private String mois;

    @Column(nullable = false)
    private Boolean planifie;

    public Integer getId() {
        return id;
    }

    public Integer getIdActivite() {
        return idActivite;
    }

    public void setIdActivite(Integer idActivite) {
        this.idActivite = idActivite;
    }

    public Integer getIdExercice() {
        return idExercice;
    }

    public void setIdExercice(Integer idExercice) {
        this.idExercice = idExercice;
    }

    public Short getTrimestre() {
        return trimestre;
    }

    public void setTrimestre(Short trimestre) {
        this.trimestre = trimestre;
    }

    public String getMois() {
        return mois;
    }

    public void setMois(String mois) {
        this.mois = mois;
    }

    public Boolean getPlanifie() {
        return planifie;
    }

    public void setPlanifie(Boolean planifie) {
        this.planifie = planifie;
    }
}
