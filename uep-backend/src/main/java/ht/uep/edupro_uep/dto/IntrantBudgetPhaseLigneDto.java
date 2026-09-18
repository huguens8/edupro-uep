package ht.uep.edupro_uep.dto;

import java.math.BigDecimal;

/** § 28 de la FIOP : une ligne du budget du projet par phases ("Voies et Moyens" × 4 phases fixes). */
public class IntrantBudgetPhaseLigneDto {

    private String voieMoyen;

    private BigDecimal montantElaboration;
    private BigDecimal montantPlanification;
    private BigDecimal montantExecution;
    private BigDecimal montantEvaluationImpact;

    private BigDecimal montantTotal;

    public String getVoieMoyen() {
        return voieMoyen;
    }

    public void setVoieMoyen(String voieMoyen) {
        this.voieMoyen = voieMoyen;
    }

    public BigDecimal getMontantElaboration() {
        return montantElaboration;
    }

    public void setMontantElaboration(BigDecimal montantElaboration) {
        this.montantElaboration = montantElaboration;
    }

    public BigDecimal getMontantPlanification() {
        return montantPlanification;
    }

    public void setMontantPlanification(BigDecimal montantPlanification) {
        this.montantPlanification = montantPlanification;
    }

    public BigDecimal getMontantExecution() {
        return montantExecution;
    }

    public void setMontantExecution(BigDecimal montantExecution) {
        this.montantExecution = montantExecution;
    }

    public BigDecimal getMontantEvaluationImpact() {
        return montantEvaluationImpact;
    }

    public void setMontantEvaluationImpact(BigDecimal montantEvaluationImpact) {
        this.montantEvaluationImpact = montantEvaluationImpact;
    }

    public BigDecimal getMontantTotal() {
        return montantTotal;
    }

    public void setMontantTotal(BigDecimal montantTotal) {
        this.montantTotal = montantTotal;
    }
}
