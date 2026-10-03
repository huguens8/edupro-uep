package ht.uep.edupro_uep.dto;

import java.math.BigDecimal;

/** Ligne du tableau § 44 : prévision de l'exercice et poids (5 = 4 / Montant Total) d'une source. */
public class TrancheSourceLigneDto {

    private Integer idSource;
    private String sourceLibelle;
    private String sourceCategorie;
    private BigDecimal previsionTotal;
    private BigDecimal poidsPct;

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
}
