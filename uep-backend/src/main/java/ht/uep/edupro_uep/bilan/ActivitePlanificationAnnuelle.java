package ht.uep.edupro_uep.bilan;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

/** § 31 de la FIOP : coût d'une activité réparti sur les 5 années du calendrier prévisionnel. */
@Entity
@Table(name = "activite_planification_annuelle")
public class ActivitePlanificationAnnuelle {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "activite_planif_seq")
    @SequenceGenerator(name = "activite_planif_seq", sequenceName = "activite_planification_annuelle_id_planif_annuelle_seq", allocationSize = 1)
    @Column(name = "id_planif_annuelle")
    private Integer id;

    @Column(name = "id_activite", nullable = false)
    private Integer idActivite;

    @Column(name = "annee_numero", nullable = false)
    private Short anneeNumero;

    @Column(name = "cout_annee")
    private BigDecimal coutAnnee;

    @Column(name = "duree_annee")
    private String dureeAnnee;

    public Integer getId() {
        return id;
    }

    public Integer getIdActivite() {
        return idActivite;
    }

    public void setIdActivite(Integer idActivite) {
        this.idActivite = idActivite;
    }

    public Short getAnneeNumero() {
        return anneeNumero;
    }

    public void setAnneeNumero(Short anneeNumero) {
        this.anneeNumero = anneeNumero;
    }

    public BigDecimal getCoutAnnee() {
        return coutAnnee;
    }

    public void setCoutAnnee(BigDecimal coutAnnee) {
        this.coutAnnee = coutAnnee;
    }

    public String getDureeAnnee() {
        return dureeAnnee;
    }

    public void setDureeAnnee(String dureeAnnee) {
        this.dureeAnnee = dureeAnnee;
    }
}
