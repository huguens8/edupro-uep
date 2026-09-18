package ht.uep.edupro_uep.geo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "arrondissement")
public class Arrondissement {

    @Id
    @Column(name = "id_arrondissement")
    private String id;

    @Column(name = "id_departement", nullable = false)
    private String idDepartement;

    @Column(nullable = false)
    private String libelle;

    public String getId() {
        return id;
    }

    public String getIdDepartement() {
        return idDepartement;
    }

    public String getLibelle() {
        return libelle;
    }
}
