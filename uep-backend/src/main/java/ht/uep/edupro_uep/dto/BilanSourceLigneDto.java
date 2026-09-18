package ht.uep.edupro_uep.dto;

import java.math.BigDecimal;

/**
 * Une ligne "source de financement" du § 39 de la FIOP ("Évolution financière du projet"), en
 * réponse : entièrement dérivée du plan de financement du projet (§ 29), aucune saisie ici.
 */
public class BilanSourceLigneDto {

    private Integer idSource;

    private String sourceLibelle;

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
