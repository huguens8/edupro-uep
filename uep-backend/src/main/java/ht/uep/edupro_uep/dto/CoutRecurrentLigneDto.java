package ht.uep.edupro_uep.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;

/** § 35 de la FIOP : une ligne du budget de fonctionnement post-livrable (coûts récurrents), par rubrique principale. */
public class CoutRecurrentLigneDto {

    @NotNull(message = "La rubrique budgétaire est obligatoire.")
    private Integer idRubrique;

    private String rubriqueLibelle;

    private BigDecimal montantAnnee1;
    private BigDecimal montantAnnee2;
    private BigDecimal montantAnnee3;
    private BigDecimal montantAnnee4;
    private BigDecimal montantAnnee5;

    private BigDecimal montantTotal;

    public Integer getIdRubrique() {
        return idRubrique;
    }

    public void setIdRubrique(Integer idRubrique) {
        this.idRubrique = idRubrique;
    }

    public String getRubriqueLibelle() {
        return rubriqueLibelle;
    }

    public void setRubriqueLibelle(String rubriqueLibelle) {
        this.rubriqueLibelle = rubriqueLibelle;
    }

    public BigDecimal getMontantAnnee1() {
        return montantAnnee1;
    }

    public void setMontantAnnee1(BigDecimal montantAnnee1) {
        this.montantAnnee1 = montantAnnee1;
    }

    public BigDecimal getMontantAnnee2() {
        return montantAnnee2;
    }

    public void setMontantAnnee2(BigDecimal montantAnnee2) {
        this.montantAnnee2 = montantAnnee2;
    }

    public BigDecimal getMontantAnnee3() {
        return montantAnnee3;
    }

    public void setMontantAnnee3(BigDecimal montantAnnee3) {
        this.montantAnnee3 = montantAnnee3;
    }

    public BigDecimal getMontantAnnee4() {
        return montantAnnee4;
    }

    public void setMontantAnnee4(BigDecimal montantAnnee4) {
        this.montantAnnee4 = montantAnnee4;
    }

    public BigDecimal getMontantAnnee5() {
        return montantAnnee5;
    }

    public void setMontantAnnee5(BigDecimal montantAnnee5) {
        this.montantAnnee5 = montantAnnee5;
    }

    public BigDecimal getMontantTotal() {
        return montantTotal;
    }

    public void setMontantTotal(BigDecimal montantTotal) {
        this.montantTotal = montantTotal;
    }
}
