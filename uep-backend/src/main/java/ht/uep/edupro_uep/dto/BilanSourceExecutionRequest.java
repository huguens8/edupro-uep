package ht.uep.edupro_uep.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;

/**
 * Saisie d'exécution (§ 39 de la FIOP) pour UNE source de financement du plan projet (§ 29,
 * saisi une seule fois dans la FIOP). La "prévision totale" n'est jamais ressaisie ici.
 */
public class BilanSourceExecutionRequest {

    @NotNull(message = "L'identifiant de la source est obligatoire.")
    private Integer idSource;

    private BigDecimal totalDecaissementsEffectifs;
    private BigDecimal totalDepensesEffectives;

    public Integer getIdSource() {
        return idSource;
    }

    public void setIdSource(Integer idSource) {
        this.idSource = idSource;
    }

    public BigDecimal getTotalDecaissementsEffectifs() {
        return totalDecaissementsEffectifs;
    }

    public void setTotalDecaissementsEffectifs(BigDecimal totalDecaissementsEffectifs) {
        this.totalDecaissementsEffectifs = totalDecaissementsEffectifs;
    }

    public BigDecimal getTotalDepensesEffectives() {
        return totalDepensesEffectives;
    }

    public void setTotalDepensesEffectives(BigDecimal totalDepensesEffectives) {
        this.totalDepensesEffectives = totalDepensesEffectives;
    }
}
