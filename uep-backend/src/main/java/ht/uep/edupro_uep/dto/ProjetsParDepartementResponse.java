package ht.uep.edupro_uep.dto;

public class ProjetsParDepartementResponse {

    // null quand le projet n'a pas de département renseigné.
    private String idDepartement;
    private String libelle;
    // Nom de l'énumération StatutFiop (ex. VALIDE_ACTIF).
    private String statut;
    private long nombre;

    public ProjetsParDepartementResponse(String idDepartement, String libelle, String statut, long nombre) {
        this.idDepartement = idDepartement;
        this.libelle = libelle;
        this.statut = statut;
        this.nombre = nombre;
    }

    public String getIdDepartement() {
        return idDepartement;
    }

    public String getLibelle() {
        return libelle;
    }

    public String getStatut() {
        return statut;
    }

    public long getNombre() {
        return nombre;
    }
}
