package ht.uep.edupro_uep.fiop;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

/** § 35 de la FIOP : "Coûts récurrents du projet" (Budget de Fonctionnement Post Livrable), par rubrique principale et par année. */
@Entity
@Table(name = "projet_cout_recurrent")
public class ProjetCoutRecurrent {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cout_recurrent_seq")
    @SequenceGenerator(name = "cout_recurrent_seq", sequenceName = "projet_cout_recurrent_id_cout_recurrent_seq", allocationSize = 1)
    @Column(name = "id_cout_recurrent")
    private Integer id;

    @Column(name = "id_projet", nullable = false)
    private Integer idProjet;

    @Column(name = "id_rubrique", nullable = false)
    private Integer idRubrique;

    @Column(name = "annee_numero", nullable = false)
    private Short anneeNumero;

    @Column(name = "montant", nullable = false)
    private BigDecimal montant;

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

    public Short getAnneeNumero() {
        return anneeNumero;
    }

    public void setAnneeNumero(Short anneeNumero) {
        this.anneeNumero = anneeNumero;
    }

    public BigDecimal getMontant() {
        return montant;
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }
}
