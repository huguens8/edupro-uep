package ht.uep.edupro_uep.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

/**
 * Ligne des §§ 48-49 "Calendrier prévisionnel d'utilisation des ressources financières" : montants
 * d'une sous-rubrique pour les trimestres 1 à 4 de l'exercice. Sert en requête et en réponse.
 */
public class TrancheCalendrierLigneDto {

    @NotNull(message = "La sous-rubrique budgétaire est obligatoire.")
    private Integer idRubrique;

    @DecimalMin(value = "0", message = "Le montant ne peut pas être négatif.")
    private BigDecimal trimestre1;

    @DecimalMin(value = "0", message = "Le montant ne peut pas être négatif.")
    private BigDecimal trimestre2;

    @DecimalMin(value = "0", message = "Le montant ne peut pas être négatif.")
    private BigDecimal trimestre3;

    @DecimalMin(value = "0", message = "Le montant ne peut pas être négatif.")
    private BigDecimal trimestre4;

    public Integer getIdRubrique() {
        return idRubrique;
    }

    public void setIdRubrique(Integer idRubrique) {
        this.idRubrique = idRubrique;
    }

    public BigDecimal getTrimestre1() {
        return trimestre1;
    }

    public void setTrimestre1(BigDecimal trimestre1) {
        this.trimestre1 = trimestre1;
    }

    public BigDecimal getTrimestre2() {
        return trimestre2;
    }

    public void setTrimestre2(BigDecimal trimestre2) {
        this.trimestre2 = trimestre2;
    }

    public BigDecimal getTrimestre3() {
        return trimestre3;
    }

    public void setTrimestre3(BigDecimal trimestre3) {
        this.trimestre3 = trimestre3;
    }

    public BigDecimal getTrimestre4() {
        return trimestre4;
    }

    public void setTrimestre4(BigDecimal trimestre4) {
        this.trimestre4 = trimestre4;
    }

    /** Montant du trimestre {@code rang} (1 à 4). */
    public BigDecimal getTrimestre(int rang) {
        return switch (rang) {
            case 1 -> trimestre1;
            case 2 -> trimestre2;
            case 3 -> trimestre3;
            case 4 -> trimestre4;
            default -> throw new IllegalArgumentException("Trimestre hors 1-4 : " + rang);
        };
    }

    public void setTrimestre(int rang, BigDecimal montant) {
        switch (rang) {
            case 1 -> trimestre1 = montant;
            case 2 -> trimestre2 = montant;
            case 3 -> trimestre3 = montant;
            case 4 -> trimestre4 = montant;
            default -> throw new IllegalArgumentException("Trimestre hors 1-4 : " + rang);
        }
    }
}
