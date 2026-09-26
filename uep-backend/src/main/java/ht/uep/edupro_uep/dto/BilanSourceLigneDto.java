package ht.uep.edupro_uep.dto;

import java.math.BigDecimal;

/**
 * Une ligne "source de financement" du § 39 de la FIOP ("Évolution financière du projet") : la
 * prévision provient du plan de financement du projet (§ 29) ; décaissements et dépenses
 * effectives sont saisis ici ; balance, valeur aux livres et taux (§ 40) sont calculés.
 */
public class BilanSourceLigneDto {

    private Integer idSource;

    private String sourceLibelle;
    private String sourceCategorie;

    private BigDecimal previsionTotal;
    private BigDecimal poidsPct;
    private BigDecimal totalDecaissementsEffectifs;
    private BigDecimal balancePrevisionnelle;
    private BigDecimal totalDepensesEffectives;
    private BigDecimal valeurAuxLivresComptables;
    private BigDecimal tauxFinancementPct;
    private BigDecimal tauxAbsorptionPct;

    public Integer getIdSource() {
        return idSource;
    }

    public void setIdSource(Integer idSource) {
        this.idSource = idSource;
    }

    public String getSourceLibelle() {
        return sourceLibelle;
    }

    public void setSourceLibelle(String sourceLibelle) {
        this.sourceLibelle = sourceLibelle;
    }

    public String getSourceCategorie() {
        return sourceCategorie;
    }

    public void setSourceCategorie(String sourceCategorie) {
        this.sourceCategorie = sourceCategorie;
    }

    public BigDecimal getPrevisionTotal() {
        return previsionTotal;
    }

    public void setPrevisionTotal(BigDecimal previsionTotal) {
        this.previsionTotal = previsionTotal;
    }

    public BigDecimal getPoidsPct() {
        return poidsPct;
    }

    public void setPoidsPct(BigDecimal poidsPct) {
        this.poidsPct = poidsPct;
    }

    public BigDecimal getTotalDecaissementsEffectifs() {
        return totalDecaissementsEffectifs;
    }

    public void setTotalDecaissementsEffectifs(BigDecimal totalDecaissementsEffectifs) {
        this.totalDecaissementsEffectifs = totalDecaissementsEffectifs;
    }

    public BigDecimal getBalancePrevisionnelle() {
        return balancePrevisionnelle;
    }

    public void setBalancePrevisionnelle(BigDecimal balancePrevisionnelle) {
        this.balancePrevisionnelle = balancePrevisionnelle;
    }

    public BigDecimal getTotalDepensesEffectives() {
        return totalDepensesEffectives;
    }

    public void setTotalDepensesEffectives(BigDecimal totalDepensesEffectives) {
        this.totalDepensesEffectives = totalDepensesEffectives;
    }

    public BigDecimal getValeurAuxLivresComptables() {
        return valeurAuxLivresComptables;
    }

    public void setValeurAuxLivresComptables(BigDecimal valeurAuxLivresComptables) {
        this.valeurAuxLivresComptables = valeurAuxLivresComptables;
    }

    public BigDecimal getTauxFinancementPct() {
        return tauxFinancementPct;
    }

    public void setTauxFinancementPct(BigDecimal tauxFinancementPct) {
        this.tauxFinancementPct = tauxFinancementPct;
    }

    public BigDecimal getTauxAbsorptionPct() {
        return tauxAbsorptionPct;
    }

    public void setTauxAbsorptionPct(BigDecimal tauxAbsorptionPct) {
        this.tauxAbsorptionPct = tauxAbsorptionPct;
    }
}
