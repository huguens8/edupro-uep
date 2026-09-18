package ht.uep.edupro_uep.fiop;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

/**
 * FIOP = Projet (pas d'entité séparée : "créer une FIOP" = créer la ligne
 * projet). Seules les colonnes nécessaires au cycle de vie de validation et
 * à l'identification du projet sont mappées ici ; les tables de détail
 * (activités, budgets, indicateurs, bilans, découpage géographique...) sont
 * un futur module de saisie complète.
 */
@Entity
@Table(name = "projet")
public class Projet {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "projet_seq")
    @SequenceGenerator(name = "projet_seq", sequenceName = "projet_id_projet_seq", allocationSize = 1)
    @Column(name = "id_projet")
    private Integer id;

    @Column(nullable = false)
    private String titre;

    @Column(name = "code_projet", nullable = false, unique = true)
    private String codeProjet;

    @Column(name = "code_externe_bailleur")
    private String codeExterneBailleur;

    @Column(name = "id_grand_chantier")
    private Integer idGrandChantier;

    @Column(name = "id_programme")
    private Integer idProgramme;

    @Column(name = "id_sous_programme")
    private Integer idSousProgramme;

    @Column(name = "id_exercice_creation")
    private Integer idExerciceCreation;

    @Column(name = "projet_psdh")
    private String projetPsdh;

    @Column(name = "cible_prioritaire_gvt")
    private Boolean ciblePrioritaireGvt = false;

    @Column(name = "echelon_territorial")
    private String echelonTerritorial;

    @Column(name = "ministere_tutelle")
    private String ministereTutelle = "MENFP";

    @Column(name = "code_interne_pip", unique = true)
    private String codeInternePip;

    @Column(name = "date_inscription", nullable = false)
    private LocalDate dateInscription;

    @Column(name = "duree_totale_mois")
    private Integer dureeTotaleMois;

    @Column(name = "cout_total_gourde")
    private BigDecimal coutTotalGourde;

    @Column(columnDefinition = "text")
    private String justification;

    @Column(name = "effets_attendus", columnDefinition = "text")
    private String effetsAttendus;

    @Column(name = "nom_charge_projet")
    private String nomChargeProjet;

    @Column(name = "telephone_charge_projet")
    private String telephoneChargeProjet;

    @Column(name = "courriel_charge_projet")
    private String courrielChargeProjet;

    @Column(name = "id_departement")
    private String idDepartement;

    @Column(name = "id_arrondissement")
    private String idArrondissement;

    @Column(name = "id_commune")
    private String idCommune;

    @Column(name = "id_section_communale")
    private String idSectionCommunale;

    @Column(name = "habitation_localite_quartier")
    private String habitationLocaliteQuartier;

    @Column(name = "localisation_gps")
    private String localisationGps;

    @Column(nullable = false)
    private StatutFiop statut = StatutFiop.BROUILLON;

    // Absent du schéma d'origine : ajouté par une migration applicative
    // (voir db/gestionprojet_extensions.sql) pour respecter la règle métier
    // "un motif de rejet est obligatoire" du cahier des charges.
    @Column(name = "motif_rejet", columnDefinition = "text")
    private String motifRejet;

    @Column(name = "id_utilisateur_creation")
    private Integer idUtilisateurCreation;

    @Column(name = "id_utilisateur_derniere_maj")
    private Integer idUtilisateurDerniereMaj;

    @CreationTimestamp
    @Column(name = "date_creation", nullable = false, updatable = false)
    private LocalDateTime dateCreation;

    @UpdateTimestamp
    @Column(name = "date_modification", nullable = false)
    private LocalDateTime dateModification;

    public Integer getId() {
        return id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getCodeProjet() {
        return codeProjet;
    }

    public void setCodeProjet(String codeProjet) {
        this.codeProjet = codeProjet;
    }

    public String getCodeInternePip() {
        return codeInternePip;
    }

    public void setCodeInternePip(String codeInternePip) {
        this.codeInternePip = codeInternePip;
    }

    public LocalDate getDateInscription() {
        return dateInscription;
    }

    public void setDateInscription(LocalDate dateInscription) {
        this.dateInscription = dateInscription;
    }

    public Integer getDureeTotaleMois() {
        return dureeTotaleMois;
    }

    public void setDureeTotaleMois(Integer dureeTotaleMois) {
        this.dureeTotaleMois = dureeTotaleMois;
    }

    public BigDecimal getCoutTotalGourde() {
        return coutTotalGourde;
    }

    public void setCoutTotalGourde(BigDecimal coutTotalGourde) {
        this.coutTotalGourde = coutTotalGourde;
    }

    public String getJustification() {
        return justification;
    }

    public void setJustification(String justification) {
        this.justification = justification;
    }

    public String getEffetsAttendus() {
        return effetsAttendus;
    }

    public void setEffetsAttendus(String effetsAttendus) {
        this.effetsAttendus = effetsAttendus;
    }

    public String getNomChargeProjet() {
        return nomChargeProjet;
    }

    public void setNomChargeProjet(String nomChargeProjet) {
        this.nomChargeProjet = nomChargeProjet;
    }

    public String getTelephoneChargeProjet() {
        return telephoneChargeProjet;
    }

    public void setTelephoneChargeProjet(String telephoneChargeProjet) {
        this.telephoneChargeProjet = telephoneChargeProjet;
    }

    public String getCourrielChargeProjet() {
        return courrielChargeProjet;
    }

    public void setCourrielChargeProjet(String courrielChargeProjet) {
        this.courrielChargeProjet = courrielChargeProjet;
    }

    public String getIdDepartement() {
        return idDepartement;
    }

    public void setIdDepartement(String idDepartement) {
        this.idDepartement = idDepartement;
    }

    public String getIdArrondissement() {
        return idArrondissement;
    }

    public void setIdArrondissement(String idArrondissement) {
        this.idArrondissement = idArrondissement;
    }

    public String getIdCommune() {
        return idCommune;
    }

    public void setIdCommune(String idCommune) {
        this.idCommune = idCommune;
    }

    public String getIdSectionCommunale() {
        return idSectionCommunale;
    }

    public void setIdSectionCommunale(String idSectionCommunale) {
        this.idSectionCommunale = idSectionCommunale;
    }

    public String getHabitationLocaliteQuartier() {
        return habitationLocaliteQuartier;
    }

    public void setHabitationLocaliteQuartier(String habitationLocaliteQuartier) {
        this.habitationLocaliteQuartier = habitationLocaliteQuartier;
    }

    public String getLocalisationGps() {
        return localisationGps;
    }

    public void setLocalisationGps(String localisationGps) {
        this.localisationGps = localisationGps;
    }

    public String getCodeExterneBailleur() {
        return codeExterneBailleur;
    }

    public void setCodeExterneBailleur(String codeExterneBailleur) {
        this.codeExterneBailleur = codeExterneBailleur;
    }

    public Integer getIdGrandChantier() {
        return idGrandChantier;
    }

    public void setIdGrandChantier(Integer idGrandChantier) {
        this.idGrandChantier = idGrandChantier;
    }

    public Integer getIdProgramme() {
        return idProgramme;
    }

    public void setIdProgramme(Integer idProgramme) {
        this.idProgramme = idProgramme;
    }

    public Integer getIdSousProgramme() {
        return idSousProgramme;
    }

    public void setIdSousProgramme(Integer idSousProgramme) {
        this.idSousProgramme = idSousProgramme;
    }

    public Integer getIdExerciceCreation() {
        return idExerciceCreation;
    }

    public void setIdExerciceCreation(Integer idExerciceCreation) {
        this.idExerciceCreation = idExerciceCreation;
    }

    public String getProjetPsdh() {
        return projetPsdh;
    }

    public void setProjetPsdh(String projetPsdh) {
        this.projetPsdh = projetPsdh;
    }

    public Boolean getCiblePrioritaireGvt() {
        return ciblePrioritaireGvt;
    }

    public void setCiblePrioritaireGvt(Boolean ciblePrioritaireGvt) {
        this.ciblePrioritaireGvt = ciblePrioritaireGvt;
    }

    public String getEchelonTerritorial() {
        return echelonTerritorial;
    }

    public void setEchelonTerritorial(String echelonTerritorial) {
        this.echelonTerritorial = echelonTerritorial;
    }

    public String getMinistereTutelle() {
        return ministereTutelle;
    }

    public void setMinistereTutelle(String ministereTutelle) {
        this.ministereTutelle = ministereTutelle;
    }

    public StatutFiop getStatut() {
        return statut;
    }

    public void setStatut(StatutFiop statut) {
        this.statut = statut;
    }

    public String getMotifRejet() {
        return motifRejet;
    }

    public void setMotifRejet(String motifRejet) {
        this.motifRejet = motifRejet;
    }

    public Integer getIdUtilisateurCreation() {
        return idUtilisateurCreation;
    }

    public void setIdUtilisateurCreation(Integer idUtilisateurCreation) {
        this.idUtilisateurCreation = idUtilisateurCreation;
    }

    public Integer getIdUtilisateurDerniereMaj() {
        return idUtilisateurDerniereMaj;
    }

    public void setIdUtilisateurDerniereMaj(Integer idUtilisateurDerniereMaj) {
        this.idUtilisateurDerniereMaj = idUtilisateurDerniereMaj;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public LocalDateTime getDateModification() {
        return dateModification;
    }
}
