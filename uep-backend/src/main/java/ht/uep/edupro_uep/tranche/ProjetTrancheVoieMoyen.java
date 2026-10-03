package ht.uep.edupro_uep.tranche;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

/** § 47 de la FIOP : "Voies et moyens pour la réalisation des activités du projet" — une ligne. */
@Entity
@Table(name = "projet_tranche_voie_moyen")
public class ProjetTrancheVoieMoyen {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "projet_tranche_voie_moyen_seq")
    @SequenceGenerator(name = "projet_tranche_voie_moyen_seq", sequenceName = "projet_tranche_voie_moyen_id_voie_moyen_seq", allocationSize = 1)
    @Column(name = "id_voie_moyen")
    private Integer id;

    @Column(name = "id_projet", nullable = false)
    private Integer idProjet;

    @Column(name = "id_exercice", nullable = false)
    private Integer idExercice;

    @Column(name = "id_rubrique", nullable = false)
    private Integer idRubrique;

    @Column(nullable = false)
    private Short ordre;

    @Column(name = "code_article", length = 20)
    private String codeArticle;

    private String designation;

    @Column(name = "ordre_activites", length = 150)
    private String ordreActivites;

    @Column(name = "unite_mesure", length = 50)
    private String uniteMesure;

    private BigDecimal quantite;

    @Column(name = "cout_unitaire")
    private BigDecimal coutUnitaire;

    @Column(name = "montant_tresor_public")
    private BigDecimal montantTresorPublic;

    @Column(name = "montant_afc")
    private BigDecimal montantAfc;

    @Column(name = "montant_fonds_propres")
    private BigDecimal montantFondsPropres;

    @Column(name = "montant_bilateral")
    private BigDecimal montantBilateral;

    @Column(name = "montant_multilateral")
    private BigDecimal montantMultilateral;

    public Integer getId() {
        return id;
    }

    public Integer getIdProjet() {
        return idProjet;
    }

    public void setIdProjet(Integer idProjet) {
        this.idProjet = idProjet;
    }

    public Integer getIdExercice() {
        return idExercice;
    }

    public void setIdExercice(Integer idExercice) {
        this.idExercice = idExercice;
    }

    public Integer getIdRubrique() {
        return idRubrique;
    }

    public void setIdRubrique(Integer idRubrique) {
        this.idRubrique = idRubrique;
    }

    public Short getOrdre() {
        return ordre;
    }

    public void setOrdre(Short ordre) {
        this.ordre = ordre;
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
