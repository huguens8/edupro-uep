package ht.uep.edupro_uep.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;

/**
 * Une ligne du plan d'activités du projet (§§31/33 de la FIOP : "Calendrier annuel prévisionnel
 * d'exécution des activités" + "Résultats attendus par activité"). Saisie une seule fois dans la
 * FIOP ; le Bilan d'Exécution s'y réfère en lecture seule pour son avancement physique/financier.
 */
public class ActivitePlanLigneDto {

    /** Identifiant de l'activité, présent uniquement dans les réponses (absent à la création). */
    private Integer id;

    @NotBlank(message = "Le libellé de l'activité est obligatoire.")
    private String libelle;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    private String uniteResultat;
    private BigDecimal quantiteResultatAttendu;
    private BigDecimal coutUnitaire;
    private BigDecimal ressourcesNationales;
    private BigDecimal ressourcesExternes;

    private BigDecimal coutAnnee1;
    private BigDecimal coutAnnee2;
    private BigDecimal coutAnnee3;
    private BigDecimal coutAnnee4;
    private BigDecimal coutAnnee5;

    /** § 32 : durée de l'activité sur chacune des 5 années du chronogramme. */
    private String dureeAnnee1;
    private String dureeAnnee2;
    private String dureeAnnee3;
    private String dureeAnnee4;
    private String dureeAnnee5;

    // -- Calculés côté serveur, présents uniquement dans les réponses --
    private BigDecimal coutTotal;

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getUniteResultat() {
        return uniteResultat;
    }

    public void setUniteResultat(String uniteResultat) {
        this.uniteResultat = uniteResultat;
    }

    public BigDecimal getQuantiteResultatAttendu() {
        return quantiteResultatAttendu;
    }

    public void setQuantiteResultatAttendu(BigDecimal quantiteResultatAttendu) {
        this.quantiteResultatAttendu = quantiteResultatAttendu;
    }

    public BigDecimal getCoutUnitaire() {
        return coutUnitaire;
    }

    public void setCoutUnitaire(BigDecimal coutUnitaire) {
        this.coutUnitaire = coutUnitaire;
    }

    public BigDecimal getRessourcesNationales() {
        return ressourcesNationales;
    }

    public void setRessourcesNationales(BigDecimal ressourcesNationales) {
        this.ressourcesNationales = ressourcesNationales;
    }

    public BigDecimal getRessourcesExternes() {
        return ressourcesExternes;
    }

    public void setRessourcesExternes(BigDecimal ressourcesExternes) {
        this.ressourcesExternes = ressourcesExternes;
    }

    public BigDecimal getCoutAnnee1() {
        return coutAnnee1;
    }

    public void setCoutAnnee1(BigDecimal coutAnnee1) {
        this.coutAnnee1 = coutAnnee1;
    }

    public BigDecimal getCoutAnnee2() {
        return coutAnnee2;
    }

    public void setCoutAnnee2(BigDecimal coutAnnee2) {
        this.coutAnnee2 = coutAnnee2;
    }

    public BigDecimal getCoutAnnee3() {
        return coutAnnee3;
    }

    public void setCoutAnnee3(BigDecimal coutAnnee3) {
        this.coutAnnee3 = coutAnnee3;
    }

    public BigDecimal getCoutAnnee4() {
        return coutAnnee4;
    }

    public void setCoutAnnee4(BigDecimal coutAnnee4) {
        this.coutAnnee4 = coutAnnee4;
    }

    public BigDecimal getCoutAnnee5() {
        return coutAnnee5;
    }

    public void setCoutAnnee5(BigDecimal coutAnnee5) {
        this.coutAnnee5 = coutAnnee5;
    }

    public BigDecimal getCoutTotal() {
        return coutTotal;
    }

    public void setCoutTotal(BigDecimal coutTotal) {
        this.coutTotal = coutTotal;
    }

    public String getDureeAnnee1() {
        return dureeAnnee1;
    }

    public void setDureeAnnee1(String dureeAnnee1) {
        this.dureeAnnee1 = dureeAnnee1;
    }

    public String getDureeAnnee2() {
        return dureeAnnee2;
    }

    public void setDureeAnnee2(String dureeAnnee2) {
        this.dureeAnnee2 = dureeAnnee2;
    }

    public String getDureeAnnee3() {
        return dureeAnnee3;
    }

    public void setDureeAnnee3(String dureeAnnee3) {
        this.dureeAnnee3 = dureeAnnee3;
    }

    public String getDureeAnnee4() {
        return dureeAnnee4;
    }

    public void setDureeAnnee4(String dureeAnnee4) {
        this.dureeAnnee4 = dureeAnnee4;
    }

    public String getDureeAnnee5() {
        return dureeAnnee5;
    }

    public void setDureeAnnee5(String dureeAnnee5) {
        this.dureeAnnee5 = dureeAnnee5;
    }
}
