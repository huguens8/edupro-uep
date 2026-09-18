package ht.uep.edupro_uep.dto;

import java.math.BigDecimal;

/**
 * Une ligne "activité" du Bilan d'Exécution, en réponse : regroupe le "prévu" (lu depuis le plan
 * du projet, §§31/33 de la FIOP) et l'avancement physique (§42) / financier (§43) saisi pour cette
 * activité dans ce bilan.
 */
public class BilanActiviteLigneDto {

    private Integer idActivite;
    private String libelle;
    private String uniteResultat;
    private BigDecimal quantiteResultatAttendu;
    private String resultatsObtenus;

    private BigDecimal montantPrevu;
    private BigDecimal depensesAnterieuresN2;
    private BigDecimal depensesExercice;

    // -- Champs calculés côté serveur --
    private String resultatsPrevus;
    private String ecarts;
    private BigDecimal pctAvancementPhysique;
    private BigDecimal depensesCumulees;
    private BigDecimal balancePrevisionnelle;

    public Integer getIdActivite() {
        return idActivite;
    }

    public void setIdActivite(Integer idActivite) {
        this.idActivite = idActivite;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getUniteResultat() {
        return uniteResultat;
    }

    public void setUniteResultat(String uniteResultat) {
        this.uniteResultat = uniteResultat;
    }

    public BigDecimal getQuantiteResultatAttendu() {
        return quantiteResultatAttendu;
    }

    public void setQuantiteResultatAttendu(BigDecimal quantiteResultatAttendu) {
        this.quantiteResultatAttendu = quantiteResultatAttendu;
    }

    public String getResultatsObtenus() {
        return resultatsObtenus;
    }

    public void setResultatsObtenus(String resultatsObtenus) {
        this.resultatsObtenus = resultatsObtenus;
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

    public String getResultatsPrevus() {
        return resultatsPrevus;
    }

    public void setResultatsPrevus(String resultatsPrevus) {
        this.resultatsPrevus = resultatsPrevus;
    }

    public String getEcarts() {
        return ecarts;
    }

    public void setEcarts(String ecarts) {
        this.ecarts = ecarts;
    }

    public BigDecimal getPctAvancementPhysique() {
        return pctAvancementPhysique;
    }

    public void setPctAvancementPhysique(BigDecimal pctAvancementPhysique) {
        this.pctAvancementPhysique = pctAvancementPhysique;
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
