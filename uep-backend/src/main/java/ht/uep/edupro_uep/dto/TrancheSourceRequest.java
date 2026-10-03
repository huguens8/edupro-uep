package ht.uep.edupro_uep.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

/** Prévision de l'exercice (§ 44 de la FIOP) pour UNE source de financement. */
public class TrancheSourceRequest {

    @NotNull(message = "L'identifiant de la source est obligatoire.")
    private Integer idSource;

    @DecimalMin(value = "0", message = "La prévision ne peut pas être négative.")
    private BigDecimal previsionTotal;

    public Integer getIdSource() {
        return idSource;
    }

    public void setIdSource(Integer idSource) {
        this.idSource = idSource;
    }

    public BigDecimal getPrevisionTotal() {
        return previsionTotal;
    }

    public void setPrevisionTotal(BigDecimal previsionTotal) {
        this.previsionTotal = previsionTotal;
    }
}
