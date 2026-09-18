package ht.uep.edupro_uep.geo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "departement")
public class Departement {

    @Id
    @Column(name = "id_departement")
    private String id;

    @Column(nullable = false)
    private String libelle;

    public String getId() {
        return id;
    }

    public String getLibelle() {
        return libelle;
    }
}
