package ht.uep.edupro_uep.dto;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.NotNull;

/**
 * Ligne du § 46 pour UNE activité du plan projet : rangs (1 = OCT. … 12 = SEPT.) des mois où
 * elle est planifiée. Sert à la fois en requête et en réponse.
 */
public class TrancheChronogrammeLigneDto {

    @NotNull(message = "L'identifiant de l'activité est obligatoire.")
    private Integer idActivite;

    private List<Integer> mois = new ArrayList<>();

    public Integer getIdActivite() {
        return idActivite;
    }

    public void setIdActivite(Integer idActivite) {
        this.idActivite = idActivite;
    }

    public List<Integer> getMois() {
        return mois;
    }

    public void setMois(List<Integer> mois) {
        this.mois = mois;
    }
}
