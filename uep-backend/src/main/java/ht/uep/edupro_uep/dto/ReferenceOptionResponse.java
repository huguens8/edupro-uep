package ht.uep.edupro_uep.dto;

public class ReferenceOptionResponse {

    private Integer id;
    private String libelle;

    public ReferenceOptionResponse(Integer id, String libelle) {
        this.id = id;
        this.libelle = libelle;
    }

    public Integer getId() {
        return id;
    }

    public String getLibelle() {
        return libelle;
    }
}
