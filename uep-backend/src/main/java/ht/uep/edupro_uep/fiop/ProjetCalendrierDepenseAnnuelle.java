package ht.uep.edupro_uep.fiop;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

/** § 34 de la FIOP : "Calendrier des dépenses prévisionnelles annuelles de l'exécution" (par rubrique, sur 5 ans). */
@Entity
@Table(name = "projet_calendrier_depense_annuelle")
public class ProjetCalendrierDepenseAnnuelle {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "calendrier_depense_annuelle_seq")
    @SequenceGenerator(name = "calendrier_depense_annuelle_seq", sequenceName = "projet_calendrier_depense_annuelle_id_calendrier_annuel_seq", allocationSize = 1)
    @Column(name = "id_calendrier_annuel")
    private Integer id;

    @Column(name = "id_projet", nullable = false)
    private Integer idProjet;

    @Column(name = "id_rubrique", nullable = false)
    private Integer idRubrique;

    @Column(name = "annee_numero", nullable = false)
    private Short anneeNumero;

    @Column(name = "montant_previsionnel", nullable = false)
    private BigDecimal montantPrevisionnel;

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

    public BigDecimal getMontantPrevisionnel() {
        return montantPrevisionnel;
    }

    public void setMontantPrevisionnel(BigDecimal montantPrevisionnel) {
        this.montantPrevisionnel = montantPrevisionnel;
    }
}
