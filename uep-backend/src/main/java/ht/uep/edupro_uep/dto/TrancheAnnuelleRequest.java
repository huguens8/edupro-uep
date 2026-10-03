package ht.uep.edupro_uep.dto;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

/**
 * Tranche annuelle du projet (TROISIEME PARTIE de la FIOP) pour un exercice. `idExercice` n'est
 * lu qu'à la création : une tranche existante reste attachée à son exercice.
 */
public class TrancheAnnuelleRequest {

    @NotNull(message = "L'exercice est obligatoire.")
    private Integer idExercice;

    @Valid
    private List<TrancheSourceRequest> sources = new ArrayList<>();

    @Valid
    private List<TrancheChronogrammeLigneDto> chronogramme = new ArrayList<>();

    @Valid
    private List<TrancheVoieMoyenLigneDto> voiesMoyens = new ArrayList<>();

    @Valid
    private List<TrancheCalendrierLigneDto> calendrierNational = new ArrayList<>();

    @Valid
    private List<TrancheCalendrierLigneDto> calendrierExterne = new ArrayList<>();

    public Integer getIdExercice() {
        return idExercice;
    }

    public void setIdExercice(Integer idExercice) {
        this.idExercice = idExercice;
    }

    public List<TrancheSourceRequest> getSources() {
        return sources;
    }

    public void setSources(List<TrancheSourceRequest> sources) {
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
}
