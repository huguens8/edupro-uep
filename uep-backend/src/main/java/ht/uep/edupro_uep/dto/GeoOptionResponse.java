package ht.uep.edupro_uep.dto;

public class GeoOptionResponse {

    private String id;
    private String libelle;

    public GeoOptionResponse(String id, String libelle) {
        this.id = id;
        this.libelle = libelle;
    }

    public String getId() {
        return id;
    }

    public String getLibelle() {
        return libelle;
    }
}
