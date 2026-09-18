package ht.uep.edupro_uep.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;

/** § 34 de la FIOP : une ligne du calendrier des dépenses prévisionnelles annuelles par rubrique. */
public class RubriqueCalendrierLigneDto {

    @NotNull(message = "La rubrique budgétaire est obligatoire.")
    private Integer idRubrique;

    private String rubriqueLibelle;

    /** Colonne "# d'ordre des Activités" du canevas : libre, une valeur par sous-rubrique (pas par année). */
    private String ordreActivites;

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

    public String getOrdreActivites() {
        return ordreActivites;
    }

    public void setOrdreActivites(String ordreActivites) {
        this.ordreActivites = ordreActivites;
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
