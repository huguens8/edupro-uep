package ht.uep.edupro_uep.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class FiopResponse {

    private Integer id;
    private String titre;
    private String codeProjet;
    private String codeInternePip;
    private LocalDate dateInscription;
    private Integer dureeTotaleMois;
    private BigDecimal coutTotalGourde;
    private String nomChargeProjet;
    private String telephoneChargeProjet;
    private String courrielChargeProjet;
    private String justification;
    private String effetsAttendus;

    private List<String> indicateursResultats;
    private List<String> extrants;
    private List<IntrantBudgetPhaseLigneDto> intrantsBudgetPhases;

    private String idDepartement;
    private String departementLibelle;
    private String idArrondissement;
    private String arrondissementLibelle;
    private String idCommune;
    private String communeLibelle;
    private String idSectionCommunale;
    private String sectionCommunaleLibelle;
    private String habitationLocaliteQuartier;
    private String localisationGps;

    private String codeExterneBailleur;
    private Integer idGrandChantier;
    private String grandChantierLibelle;
    private Integer idProgramme;
    private String programmeLibelle;
    private Integer idSousProgramme;
    private String sousProgrammeLibelle;
    private Integer idExerciceCreation;
    private String exerciceLibelle;
    private String projetPsdh;
    private Boolean ciblePrioritaireGvt;
    private String echelonTerritorial;
    private String ministereTutelle;
    private List<PhaseActuelleLigneDto> phasesActuelles;

    private List<String> aspectsLegaux;
    private List<String> aspectsInstitutionnels;

    private Integer populationEnfantsGarcons;
    private Integer populationEnfantsFilles;
    private Integer populationHommes;
    private Integer populationFemmes;

    private Integer emploiPendantHomme;
    private Integer emploiPendantFemme;
    private Integer emploiApresHomme;
    private Integer emploiApresFemme;

    private IntervenantDto supervision;
    private IntervenantDto execution;
    private IntervenantDto bailleur;
    private IntervenantDto agence1;
    private IntervenantDto agence2;

    private List<ActivitePlanLigneDto> activites;
    private List<RubriqueCalendrierLigneDto> calendrierRubriques;
    private List<CoutRecurrentLigneDto> coutsRecurrents;
    private List<SourceFinancementPlanLigneDto> sourcesFinancement;

    private String statut;
    private String motifRejet;
    private Integer idUtilisateurCreation;
    private String creeParUsername;
    private LocalDateTime dateCreation;
    private LocalDateTime dateModification;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public List<String> getIndicateursResultats() {
        return indicateursResultats;
    }

    public void setIndicateursResultats(List<String> indicateursResultats) {
        this.indicateursResultats = indicateursResultats;
    }

    public List<String> getExtrants() {
        return extrants;
    }

    public void setExtrants(List<String> extrants) {
        this.extrants = extrants;
    }

    public List<IntrantBudgetPhaseLigneDto> getIntrantsBudgetPhases() {
        return intrantsBudgetPhases;
    }

    public void setIntrantsBudgetPhases(List<IntrantBudgetPhaseLigneDto> intrantsBudgetPhases) {
        this.intrantsBudgetPhases = intrantsBudgetPhases;
    }

    public String getIdDepartement() {
        return idDepartement;
    }

    public void setIdDepartement(String idDepartement) {
        this.idDepartement = idDepartement;
    }

    public String getDepartementLibelle() {
        return departementLibelle;
    }

    public void setDepartementLibelle(String departementLibelle) {
        this.departementLibelle = departementLibelle;
    }

    public String getIdArrondissement() {
        return idArrondissement;
    }

    public void setIdArrondissement(String idArrondissement) {
        this.idArrondissement = idArrondissement;
    }

    public String getArrondissementLibelle() {
        return arrondissementLibelle;
    }

    public void setArrondissementLibelle(String arrondissementLibelle) {
        this.arrondissementLibelle = arrondissementLibelle;
    }

    public String getIdCommune() {
        return idCommune;
    }

    public void setIdCommune(String idCommune) {
        this.idCommune = idCommune;
    }

    public String getCommuneLibelle() {
        return communeLibelle;
    }

    public void setCommuneLibelle(String communeLibelle) {
        this.communeLibelle = communeLibelle;
    }

    public String getIdSectionCommunale() {
        return idSectionCommunale;
    }

    public void setIdSectionCommunale(String idSectionCommunale) {
        this.idSectionCommunale = idSectionCommunale;
    }

    public String getSectionCommunaleLibelle() {
        return sectionCommunaleLibelle;
    }

    public void setSectionCommunaleLibelle(String sectionCommunaleLibelle) {
        this.sectionCommunaleLibelle = sectionCommunaleLibelle;
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

    public String getGrandChantierLibelle() {
        return grandChantierLibelle;
    }

    public void setGrandChantierLibelle(String grandChantierLibelle) {
        this.grandChantierLibelle = grandChantierLibelle;
    }

    public Integer getIdProgramme() {
        return idProgramme;
    }

    public void setIdProgramme(Integer idProgramme) {
        this.idProgramme = idProgramme;
    }

    public String getProgrammeLibelle() {
        return programmeLibelle;
    }

    public void setProgrammeLibelle(String programmeLibelle) {
        this.programmeLibelle = programmeLibelle;
    }

    public Integer getIdSousProgramme() {
        return idSousProgramme;
    }

    public void setIdSousProgramme(Integer idSousProgramme) {
        this.idSousProgramme = idSousProgramme;
    }

    public String getSousProgrammeLibelle() {
        return sousProgrammeLibelle;
    }

    public void setSousProgrammeLibelle(String sousProgrammeLibelle) {
        this.sousProgrammeLibelle = sousProgrammeLibelle;
    }

    public Integer getIdExerciceCreation() {
        return idExerciceCreation;
    }

    public void setIdExerciceCreation(Integer idExerciceCreation) {
        this.idExerciceCreation = idExerciceCreation;
    }

    public String getExerciceLibelle() {
        return exerciceLibelle;
    }

    public void setExerciceLibelle(String exerciceLibelle) {
        this.exerciceLibelle = exerciceLibelle;
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

    public List<PhaseActuelleLigneDto> getPhasesActuelles() {
        return phasesActuelles;
    }

    public void setPhasesActuelles(List<PhaseActuelleLigneDto> phasesActuelles) {
        this.phasesActuelles = phasesActuelles;
    }

    public List<String> getAspectsLegaux() {
        return aspectsLegaux;
    }

    public void setAspectsLegaux(List<String> aspectsLegaux) {
        this.aspectsLegaux = aspectsLegaux;
    }

    public List<String> getAspectsInstitutionnels() {
        return aspectsInstitutionnels;
    }

    public void setAspectsInstitutionnels(List<String> aspectsInstitutionnels) {
        this.aspectsInstitutionnels = aspectsInstitutionnels;
    }

    public Integer getPopulationEnfantsGarcons() {
        return populationEnfantsGarcons;
    }

    public void setPopulationEnfantsGarcons(Integer populationEnfantsGarcons) {
        this.populationEnfantsGarcons = populationEnfantsGarcons;
    }

    public Integer getPopulationEnfantsFilles() {
        return populationEnfantsFilles;
    }

    public void setPopulationEnfantsFilles(Integer populationEnfantsFilles) {
        this.populationEnfantsFilles = populationEnfantsFilles;
    }

    public Integer getPopulationHommes() {
        return populationHommes;
    }

    public void setPopulationHommes(Integer populationHommes) {
        this.populationHommes = populationHommes;
    }

    public Integer getPopulationFemmes() {
        return populationFemmes;
    }

    public void setPopulationFemmes(Integer populationFemmes) {
        this.populationFemmes = populationFemmes;
    }

    public Integer getEmploiPendantHomme() {
        return emploiPendantHomme;
    }

    public void setEmploiPendantHomme(Integer emploiPendantHomme) {
        this.emploiPendantHomme = emploiPendantHomme;
    }

    public Integer getEmploiPendantFemme() {
        return emploiPendantFemme;
    }

    public void setEmploiPendantFemme(Integer emploiPendantFemme) {
        this.emploiPendantFemme = emploiPendantFemme;
    }

    public Integer getEmploiApresHomme() {
        return emploiApresHomme;
    }

    public void setEmploiApresHomme(Integer emploiApresHomme) {
        this.emploiApresHomme = emploiApresHomme;
    }

    public Integer getEmploiApresFemme() {
        return emploiApresFemme;
    }

    public void setEmploiApresFemme(Integer emploiApresFemme) {
        this.emploiApresFemme = emploiApresFemme;
    }

    public IntervenantDto getSupervision() {
        return supervision;
    }

    public void setSupervision(IntervenantDto supervision) {
        this.supervision = supervision;
    }

    public IntervenantDto getExecution() {
        return execution;
    }

    public void setExecution(IntervenantDto execution) {
        this.execution = execution;
    }

    public IntervenantDto getBailleur() {
        return bailleur;
    }

    public void setBailleur(IntervenantDto bailleur) {
        this.bailleur = bailleur;
    }

    public IntervenantDto getAgence1() {
        return agence1;
    }

    public void setAgence1(IntervenantDto agence1) {
        this.agence1 = agence1;
    }

    public IntervenantDto getAgence2() {
        return agence2;
    }

    public void setAgence2(IntervenantDto agence2) {
        this.agence2 = agence2;
    }

    public List<ActivitePlanLigneDto> getActivites() {
        return activites;
    }

    public void setActivites(List<ActivitePlanLigneDto> activites) {
        this.activites = activites;
    }

    public List<RubriqueCalendrierLigneDto> getCalendrierRubriques() {
        return calendrierRubriques;
    }

    public void setCalendrierRubriques(List<RubriqueCalendrierLigneDto> calendrierRubriques) {
        this.calendrierRubriques = calendrierRubriques;
    }

    public List<CoutRecurrentLigneDto> getCoutsRecurrents() {
        return coutsRecurrents;
    }

    public void setCoutsRecurrents(List<CoutRecurrentLigneDto> coutsRecurrents) {
        this.coutsRecurrents = coutsRecurrents;
    }

    public List<SourceFinancementPlanLigneDto> getSourcesFinancement() {
        return sourcesFinancement;
    }

    public void setSourcesFinancement(List<SourceFinancementPlanLigneDto> sourcesFinancement) {
        this.sourcesFinancement = sourcesFinancement;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
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

    public String getCreeParUsername() {
        return creeParUsername;
    }

    public void setCreeParUsername(String creeParUsername) {
        this.creeParUsername = creeParUsername;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public LocalDateTime getDateModification() {
        return dateModification;
    }

    public void setDateModification(LocalDateTime dateModification) {
        this.dateModification = dateModification;
    }
}
