package ht.uep.edupro_uep.dto;

import java.math.BigDecimal;

/** § 38 de la FIOP : une ligne (année 1 à 5) du "Programme d'Investissement Public (PIP)". */
public class PipAnnuelPlanLigneDto {

    private Short anneeNumero;
    private Integer idExercice;
    private String exerciceLibelle;
    private BigDecimal budgetPrevisionnel;
    private BigDecimal budgetAlloue;

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
}
