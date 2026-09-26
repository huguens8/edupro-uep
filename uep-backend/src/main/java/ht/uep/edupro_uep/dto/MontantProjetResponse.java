package ht.uep.edupro_uep.dto;

import java.math.BigDecimal;

public class MontantProjetResponse {

    private Integer id;
    private String titre;
    // Nom de l'énumération StatutFiop (ex. VALIDE_ACTIF).
    private String statut;
    // null quand le coût total n'est pas encore renseigné.
    private BigDecimal coutTotalGourde;

    public MontantProjetResponse(Integer id, String titre, String statut, BigDecimal coutTotalGourde) {
        this.id = id;
        this.titre = titre;
        this.statut = statut;
        this.coutTotalGourde = coutTotalGourde;
    }

    public Integer getId() {
        return id;
    }

    public String getTitre() {
        return titre;
    }

    public String getStatut() {
        return statut;
    }

    public BigDecimal getCoutTotalGourde() {
        return coutTotalGourde;
    }
}
