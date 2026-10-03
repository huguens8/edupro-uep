package ht.uep.edupro_uep.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Ligne du § 47 "Voies et moyens" : un code/article budgétaire d'une sous-rubrique, les activités
 * qu'il sert (texte libre comme dans le canevas, ex. "01, 04, 06"), quantité × coût unitaire et
 * la répartition sur les 5 sources du canevas. Sert à la fois en requête et en réponse (le total
 * n'est renseigné qu'en réponse).
 */
public class TrancheVoieMoyenLigneDto {

    @NotNull(message = "La sous-rubrique budgétaire est obligatoire.")
    private Integer idRubrique;

    @Size(max = 20, message = "Le code article ne peut pas dépasser 20 caractères.")
    private String codeArticle;

    @Size(max = 255, message = "La désignation ne peut pas dépasser 255 caractères.")
    private String designation;

    @Size(max = 150, message = "Les numéros d'ordre des activités ne peuvent pas dépasser 150 caractères.")
    private String ordreActivites;

    @Size(max = 50, message = "L'unité de mesure ne peut pas dépasser 50 caractères.")
    private String uniteMesure;

    @DecimalMin(value = "0", message = "La quantité ne peut pas être négative.")
    private BigDecimal quantite;

    @DecimalMin(value = "0", message = "Le coût unitaire ne peut pas être négatif.")
    private BigDecimal coutUnitaire;

    private BigDecimal total;

    @DecimalMin(value = "0", message = "Le montant ne peut pas être négatif.")
    private BigDecimal montantTresorPublic;

    @DecimalMin(value = "0", message = "Le montant ne peut pas être négatif.")
    private BigDecimal montantAfc;

    @DecimalMin(value = "0", message = "Le montant ne peut pas être négatif.")
    private BigDecimal montantFondsPropres;

    @DecimalMin(value = "0", message = "Le montant ne peut pas être négatif.")
    private BigDecimal montantBilateral;

    @DecimalMin(value = "0", message = "Le montant ne peut pas être négatif.")
    private BigDecimal montantMultilateral;

    public Integer getIdRubrique() {
        return idRubrique;
    }

    public void setIdRubrique(Integer idRubrique) {
        this.idRubrique = idRubrique;
    }

    public String getCodeArticle() {
        return codeArticle;
    }

    public void setCodeArticle(String codeArticle) {
        this.codeArticle = codeArticle;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getOrdreActivites() {
        return ordreActivites;
    }

    public void setOrdreActivites(String ordreActivites) {
        this.ordreActivites = ordreActivites;
    }

    public String getUniteMesure() {
        return uniteMesure;
    }

    public void setUniteMesure(String uniteMesure) {
        this.uniteMesure = uniteMesure;
    }

    public BigDecimal getQuantite() {
        return quantite;
    }

    public void setQuantite(BigDecimal quantite) {
        this.quantite = quantite;
    }

    public BigDecimal getCoutUnitaire() {
        return coutUnitaire;
    }

    public void setCoutUnitaire(BigDecimal coutUnitaire) {
        this.coutUnitaire = coutUnitaire;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getMontantTresorPublic() {
        return montantTresorPublic;
    }

    public void setMontantTresorPublic(BigDecimal montantTresorPublic) {
        this.montantTresorPublic = montantTresorPublic;
    }

    public BigDecimal getMontantAfc() {
        return montantAfc;
    }

    public void setMontantAfc(BigDecimal montantAfc) {
        this.montantAfc = montantAfc;
    }

    public BigDecimal getMontantFondsPropres() {
        return montantFondsPropres;
    }

    public void setMontantFondsPropres(BigDecimal montantFondsPropres) {
        this.montantFondsPropres = montantFondsPropres;
    }

    public BigDecimal getMontantBilateral() {
        return montantBilateral;
    }

    public void setMontantBilateral(BigDecimal montantBilateral) {
        this.montantBilateral = montantBilateral;
    }

    public BigDecimal getMontantMultilateral() {
        return montantMultilateral;
    }

    public void setMontantMultilateral(BigDecimal montantMultilateral) {
        this.montantMultilateral = montantMultilateral;
    }
}
