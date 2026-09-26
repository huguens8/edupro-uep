package ht.uep.edupro_uep.dto;

import java.math.BigDecimal;

/**
 * Une ligne du § 38 de la FIOP ("Programme d'Investissement Public") : budget prévisionnel et
 * alloué proviennent du plan du projet (§ 38 saisi une seule fois) ; le budget réel est saisi
 * ici ; les écarts sont calculés.
 */
public class BilanPipLigneDto {

    private Short anneeNumero;
    private Integer idExercice;
    private String exerciceLibelle;
    private BigDecimal budgetPrevisionnel;
    private BigDecimal budgetAlloue;
    private BigDecimal budgetReel;
    private BigDecimal ecartPrevisionnel;
    private BigDecimal ecartExecution;

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

    public String getExerciceLibelle() {
        return exerciceLibelle;
    }

    public void setExerciceLibelle(String exerciceLibelle) {
        this.exerciceLibelle = exerciceLibelle;
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

    public BigDecimal getEcartPrevisionnel() {
        return ecartPrevisionnel;
    }

    public void setEcartPrevisionnel(BigDecimal ecartPrevisionnel) {
        this.ecartPrevisionnel = ecartPrevisionnel;
    }

    public BigDecimal getEcartExecution() {
        return ecartExecution;
    }

    public void setEcartExecution(BigDecimal ecartExecution) {
        this.ecartExecution = ecartExecution;
    }
}
