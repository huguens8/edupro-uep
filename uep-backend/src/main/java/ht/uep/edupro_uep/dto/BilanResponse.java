package ht.uep.edupro_uep.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class BilanResponse {

    private Integer id;
    private Integer idProjet;
    private String projetTitre;
    private Integer idExercice;
    private String exerciceLibelle;

    private LocalDate dateDemarrageEffective;
    private Integer dureeTotaleProjetMois;
    private Integer tempsEcouleMois;
    private Integer tempsRestantMois;
    private LocalDate dateAchevementPrevue;

    private List<BilanActiviteLigneDto> activites;
    private List<BilanRubriqueLigneDto> rubriques;
    private List<BilanSourceLigneDto> sources;

    private LocalDateTime dateCreation;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdProjet() {
        return idProjet;
    }

    public void setIdProjet(Integer idProjet) {
        this.idProjet = idProjet;
    }

    public String getProjetTitre() {
        return projetTitre;
    }

    public void setProjetTitre(String projetTitre) {
        this.projetTitre = projetTitre;
    }

    public Integer getIdExercice() {
        return idExercice;
    }

    public void setIdExercice(Integer idExercice) {
        this.idExercice = idExercice;
    }

    public String getExerciceLibelle() {
        return exerciceLibelle;
    }

    public void setExerciceLibelle(String exerciceLibelle) {
        this.exerciceLibelle = exerciceLibelle;
    }

    public LocalDate getDateDemarrageEffective() {
        return dateDemarrageEffective;
    }

    public void setDateDemarrageEffective(LocalDate dateDemarrageEffective) {
        this.dateDemarrageEffective = dateDemarrageEffective;
    }

    public Integer getDureeTotaleProjetMois() {
        return dureeTotaleProjetMois;
    }

    public void setDureeTotaleProjetMois(Integer dureeTotaleProjetMois) {
        this.dureeTotaleProjetMois = dureeTotaleProjetMois;
    }

    public Integer getTempsEcouleMois() {
        return tempsEcouleMois;
    }

    public void setTempsEcouleMois(Integer tempsEcouleMois) {
        this.tempsEcouleMois = tempsEcouleMois;
    }

    public Integer getTempsRestantMois() {
        return tempsRestantMois;
    }

    public void setTempsRestantMois(Integer tempsRestantMois) {
        this.tempsRestantMois = tempsRestantMois;
    }

    public LocalDate getDateAchevementPrevue() {
        return dateAchevementPrevue;
    }

    public void setDateAchevementPrevue(LocalDate dateAchevementPrevue) {
        this.dateAchevementPrevue = dateAchevementPrevue;
    }

    public List<BilanActiviteLigneDto> getActivites() {
        return activites;
    }

    public void setActivites(List<BilanActiviteLigneDto> activites) {
        this.activites = activites;
    }

    public List<BilanRubriqueLigneDto> getRubriques() {
        return rubriques;
    }

    public void setRubriques(List<BilanRubriqueLigneDto> rubriques) {
        this.rubriques = rubriques;
    }

    public List<BilanSourceLigneDto> getSources() {
        return sources;
    }

    public void setSources(List<BilanSourceLigneDto> sources) {
        this.sources = sources;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }
}
