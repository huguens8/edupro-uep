package ht.uep.edupro_uep.bilan;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

/**
 * § 38 de la FIOP : "Programme d'Investissement Public" — une ligne (année 1 à 5) saisie
 * entièrement dans le Bilan d'Exécution (prévisionnel, alloué et réel), comme dans la feuille
 * "Bilan d'Exécution" du canevas.
 */
@Entity
@Table(name = "bilan_pip_annuel")
public class BilanPipAnnuel {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bilan_pip_annuel_seq")
    @SequenceGenerator(name = "bilan_pip_annuel_seq", sequenceName = "bilan_pip_annuel_id_pip_annuel_seq", allocationSize = 1)
    @Column(name = "id_pip_annuel")
    private Integer id;

    @Column(name = "id_bilan", nullable = false)
    private Integer idBilan;

    @Column(name = "id_exercice", nullable = false)
    private Integer idExercice;

    @Column(name = "annee_numero")
    private Short anneeNumero;

    @Column(name = "budget_previsionnel")
    private BigDecimal budgetPrevisionnel;

    @Column(name = "budget_alloue")
    private BigDecimal budgetAlloue;

    @Column(name = "budget_reel")
    private BigDecimal budgetReel;

    public Integer getId() {
        return id;
    }

    public Integer getIdBilan() {
        return idBilan;
    }

    public void setIdBilan(Integer idBilan) {
        this.idBilan = idBilan;
    }

    public Integer getIdExercice() {
        return idExercice;
    }

    public void setIdExercice(Integer idExercice) {
        this.idExercice = idExercice;
    }

    public Short getAnneeNumero() {
        return anneeNumero;
    }

    public void setAnneeNumero(Short anneeNumero) {
        this.anneeNumero = anneeNumero;
    }

    public BigDecimal getBudgetPrevisionnel() {
        return budgetPrevisionnel;
    }

    public void setBudgetPrevisionnel(BigDecimal budgetPrevisionnel) {
        this.budgetPrevisionnel = budgetPrevisionnel;
    }

    public BigDecimal getBudgetAlloue() {
        return budgetAlloue;
    }

    public void setBudgetAlloue(BigDecimal budgetAlloue) {
        this.budgetAlloue = budgetAlloue;
    }

    public BigDecimal getBudgetReel() {
        return budgetReel;
    }

    public void setBudgetReel(BigDecimal budgetReel) {
        this.budgetReel = budgetReel;
    }
}
