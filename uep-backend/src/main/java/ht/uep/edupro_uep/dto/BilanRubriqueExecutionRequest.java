package ht.uep.edupro_uep.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;

/**
 * Saisie d'exécution (§ 41 de la FIOP) pour UNE rubrique du calendrier budgétaire du projet
 * (§ 34, saisi une seule fois dans la FIOP). Le "montant prévu" n'est jamais ressaisi ici.
 */
public class BilanRubriqueExecutionRequest {

    @NotNull(message = "L'identifiant de la rubrique est obligatoire.")
    private Integer idRubrique;

    private BigDecimal depensesAnterieuresN2;
    private BigDecimal depensesExercice;

    public Integer getIdRubrique() {
        return idRubrique;
    }

    public void setIdRubrique(Integer idRubrique) {
        this.idRubrique = idRubrique;
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
