package ht.uep.edupro_uep.geo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "commune")
public class Commune {

    @Id
    @Column(name = "id_commune")
    private String id;

    @Column(name = "id_arrondissement", nullable = false)
    private String idArrondissement;

    @Column(nullable = false)
    private String libelle;

    public String getId() {
        return id;
    }

    public String getIdArrondissement() {
        return idArrondissement;
    }

    public String getLibelle() {
        return libelle;
    }
}
