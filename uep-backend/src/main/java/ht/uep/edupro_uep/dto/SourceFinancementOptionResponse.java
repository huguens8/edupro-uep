package ht.uep.edupro_uep.dto;

/** Option de référence pour §29 : contrairement à ReferenceOptionResponse, expose la catégorie
 * (Nationale/Externe) pour que le frontend puisse regrouper les lignes du tableau sans la
 * reconstituer depuis un libellé composite. */
public class SourceFinancementOptionResponse {

    private Integer id;
    private String libelle;
    private String categorie;

    public SourceFinancementOptionResponse(Integer id, String libelle, String categorie) {
        this.id = id;
        this.libelle = libelle;
        this.categorie = categorie;
    }

    public Integer getId() {
        return id;
    }

    public String getLibelle() {
        return libelle;
    }

    public String getCategorie() {
        return categorie;
    }
}
