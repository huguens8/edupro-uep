package ht.uep.edupro_uep.dto;

/** § 34 de la FIOP : une rubrique ou sous-rubrique budgétaire, avec sa hiérarchie (pour regrouper les sous-rubriques sous leur rubrique principale et calculer les sous-totaux). */
public class RubriqueBudgetaireDetailResponse {

    private Integer id;
    private String code;
    private String libelle;
    private Short niveau;
    private Integer idRubriqueParent;

    public RubriqueBudgetaireDetailResponse(Integer id, String code, String libelle, Short niveau, Integer idRubriqueParent) {
        this.id = id;
        this.code = code;
        this.libelle = libelle;
        this.niveau = niveau;
        this.idRubriqueParent = idRubriqueParent;
    }

    public Integer getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getLibelle() {
        return libelle;
    }

    public Short getNiveau() {
        return niveau;
    }

    public Integer getIdRubriqueParent() {
        return idRubriqueParent;
    }
}
