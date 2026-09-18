package ht.uep.edupro_uep.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;

/** § 29 de la FIOP : une ligne des sources de financement du projet (+ Programme Triennal d'Investissement). */
public class SourceFinancementPlanLigneDto {

    @NotNull(message = "La source de financement est obligatoire.")
    private Integer idSource;

    private String sourceLibelle;

    private BigDecimal previsionTotal;
    private BigDecimal ptiAnnee1;
    private BigDecimal ptiAnnee2;
    private BigDecimal ptiAnnee3;
    private BigDecimal ptiAnnee4;
    private BigDecimal ptiAnnee5;

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

    public BigDecimal getPtiAnnee1() {
        return ptiAnnee1;
    }

    public void setPtiAnnee1(BigDecimal ptiAnnee1) {
        this.ptiAnnee1 = ptiAnnee1;
    }

    public BigDecimal getPtiAnnee2() {
        return ptiAnnee2;
    }

    public void setPtiAnnee2(BigDecimal ptiAnnee2) {
        this.ptiAnnee2 = ptiAnnee2;
    }

    public BigDecimal getPtiAnnee3() {
        return ptiAnnee3;
    }

    public void setPtiAnnee3(BigDecimal ptiAnnee3) {
        this.ptiAnnee3 = ptiAnnee3;
    }

    public BigDecimal getPtiAnnee4() {
        return ptiAnnee4;
    }

    public void setPtiAnnee4(BigDecimal ptiAnnee4) {
        this.ptiAnnee4 = ptiAnnee4;
    }

    public BigDecimal getPtiAnnee5() {
        return ptiAnnee5;
    }

    public void setPtiAnnee5(BigDecimal ptiAnnee5) {
        this.ptiAnnee5 = ptiAnnee5;
    }

    public BigDecimal getPoidsPct() {
        return poidsPct;
    }

    public void setPoidsPct(BigDecimal poidsPct) {
        this.poidsPct = poidsPct;
    }
}
