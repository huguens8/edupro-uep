package ht.uep.edupro_uep.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TrancheAnnuelleResponse {

    private Integer idProjet;
    private Integer idExercice;
    private String exerciceLibelle;
    private BigDecimal montantTotal;
    private List<TrancheSourceLigneDto> sources = new ArrayList<>();
    private List<TrancheChronogrammeLigneDto> chronogramme = new ArrayList<>();
    private List<TrancheVoieMoyenLigneDto> voiesMoyens = new ArrayList<>();
    private List<TrancheCalendrierLigneDto> calendrierNational = new ArrayList<>();
    private List<TrancheCalendrierLigneDto> calendrierExterne = new ArrayList<>();
    /** § 46 "Coût Prévisionnel par Trimestre" : total du § 48 pour les trimestres I à IV. */
    private List<BigDecimal> coutTrimestres = new ArrayList<>();

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

    public String getExerciceLibelle() {
        return exerciceLibelle;
    }

    public void setExerciceLibelle(String exerciceLibelle) {
        this.exerciceLibelle = exerciceLibelle;
    }

    public BigDecimal getMontantTotal() {
        return montantTotal;
    }

    public void setMontantTotal(BigDecimal montantTotal) {
        this.montantTotal = montantTotal;
    }

    public List<TrancheSourceLigneDto> getSources() {
        return sources;
    }

    public void setSources(List<TrancheSourceLigneDto> sources) {
        this.sources = sources;
    }

    public List<TrancheChronogrammeLigneDto> getChronogramme() {
        return chronogramme;
    }

    public void setChronogramme(List<TrancheChronogrammeLigneDto> chronogramme) {
        this.chronogramme = chronogramme;
    }

    public List<TrancheVoieMoyenLigneDto> getVoiesMoyens() {
        return voiesMoyens;
    }

    public void setVoiesMoyens(List<TrancheVoieMoyenLigneDto> voiesMoyens) {
        this.voiesMoyens = voiesMoyens;
    }

    public List<TrancheCalendrierLigneDto> getCalendrierNational() {
        return calendrierNational;
    }

    public void setCalendrierNational(List<TrancheCalendrierLigneDto> calendrierNational) {
        this.calendrierNational = calendrierNational;
    }

    public List<TrancheCalendrierLigneDto> getCalendrierExterne() {
        return calendrierExterne;
    }

    public void setCalendrierExterne(List<TrancheCalendrierLigneDto> calendrierExterne) {
        this.calendrierExterne = calendrierExterne;
    }

    public List<BigDecimal> getCoutTrimestres() {
        return coutTrimestres;
    }

    public void setCoutTrimestres(List<BigDecimal> coutTrimestres) {
        this.coutTrimestres = coutTrimestres;
    }
}
