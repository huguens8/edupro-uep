package ht.uep.edupro_uep.dto;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.NotNull;

public class BilanRequest {

    @NotNull(message = "L'exercice est obligatoire.")
    private Integer idExercice;

    private LocalDate dateDemarrageEffective;
    private Integer dureeTotaleProjetMois;

    /** Saisie d'exécution par activité du plan projet (§§42-43) ; le "prévu" vient du plan. */
    private List<BilanActiviteExecutionRequest> activites;

    /** Saisie d'exécution par rubrique du calendrier budgétaire projet (§ 41) ; idem. */
    private List<BilanRubriqueExecutionRequest> rubriques;

    public Integer getIdExercice() {
        return idExercice;
    }

    public void setIdExercice(Integer idExercice) {
        this.idExercice = idExercice;
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

    public List<BilanActiviteExecutionRequest> getActivites() {
        return activites;
    }

    public void setActivites(List<BilanActiviteExecutionRequest> activites) {
        this.activites = activites;
    }

    public List<BilanRubriqueExecutionRequest> getRubriques() {
        return rubriques;
    }

    public void setRubriques(List<BilanRubriqueExecutionRequest> rubriques) {
        this.rubriques = rubriques;
    }
}
