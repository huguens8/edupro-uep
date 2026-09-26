package ht.uep.edupro_uep.dto;

import java.math.BigDecimal;

/**
 * Saisie d'une ligne (année 1 à 5) du § 38 de la FIOP ("Programme d'Investissement Public"),
 * saisie entièrement dans le Bilan d'Exécution.
 */
public class BilanPipExecutionRequest {

    private Short anneeNumero;

    private Integer idExercice;

    private BigDecimal budgetPrevisionnel;

    private BigDecimal budgetAlloue;

    private BigDecimal budgetReel;

    public Short getAnneeNumero() {
        return anneeNumero;
    }

    public void setAnneeNumero(Short anneeNumero) {
        this.anneeNumero = anneeNumero;
    }

    public Integer getIdExercice() {
        return idExercice;
    }

    public void setIdExercice(Integer idExercice) {
        this.idExercice = idExercice;
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
