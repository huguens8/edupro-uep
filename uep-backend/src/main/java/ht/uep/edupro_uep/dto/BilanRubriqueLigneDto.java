package ht.uep.edupro_uep.dto;

import java.math.BigDecimal;

/**
 * Une ligne "rubrique budgétaire" du § 41 de la FIOP ("Résumé des opérations financières"), en
 * réponse : le "montant prévu" est lu depuis le calendrier budgétaire du projet (§ 34).
 */
public class BilanRubriqueLigneDto {

    private Integer idRubrique;

    private String rubriqueLibelle;

    private BigDecimal montantPrevu;
    private BigDecimal depensesAnterieuresN2;
    private BigDecimal depensesExercice;

    private BigDecimal depensesCumulees;
    private BigDecimal balancePrevisionnelle;

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

    public BigDecimal getMontantPrevu() {
        return montantPrevu;
    }

    public void setMontantPrevu(BigDecimal montantPrevu) {
        this.montantPrevu = montantPrevu;
    }

    public BigDecimal getDepensesAnterieuresN2() {
        return depensesAnterieuresN2;
    }

    public void setDepensesAnterieuresN2(BigDecimal depensesAnterieuresN2) {
        this.depensesAnterieuresN2 = depensesAnterieuresN2;
    }

    public BigDecimal getDepensesExercice() {
        return depensesExercice;
    }

    public void setDepensesExercice(BigDecimal depensesExercice) {
        this.depensesExercice = depensesExercice;
    }

    public BigDecimal getDepensesCumulees() {
        return depensesCumulees;
    }

    public void setDepensesCumulees(BigDecimal depensesCumulees) {
        this.depensesCumulees = depensesCumulees;
    }

    public BigDecimal getBalancePrevisionnelle() {
        return balancePrevisionnelle;
    }

    public void setBalancePrevisionnelle(BigDecimal balancePrevisionnelle) {
        this.balancePrevisionnelle = balancePrevisionnelle;
    }
}
