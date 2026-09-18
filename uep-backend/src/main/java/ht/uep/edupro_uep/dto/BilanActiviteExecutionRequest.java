package ht.uep.edupro_uep.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;

/**
 * Saisie d'exécution (§§42-43 de la FIOP) pour UNE activité du plan du projet (§§31/33, saisi une
 * seule fois dans la FIOP). Le "prévu" n'est jamais ressaisi ici : il est lu depuis le plan.
 */
public class BilanActiviteExecutionRequest {

    @NotNull(message = "L'identifiant de l'activité est obligatoire.")
    private Integer idActivite;

    private String resultatsObtenus;
    private BigDecimal depensesAnterieuresN2;
    private BigDecimal depensesExercice;

    public Integer getIdActivite() {
        return idActivite;
    }

    public void setIdActivite(Integer idActivite) {
        this.idActivite = idActivite;
    }

    public String getResultatsObtenus() {
        return resultatsObtenus;
    }

    public void setResultatsObtenus(String resultatsObtenus) {
        this.resultatsObtenus = resultatsObtenus;
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
}
