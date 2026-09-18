package ht.uep.edupro_uep.reference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "programme")
public class Programme {

    @Id
    @Column(name = "id_programme")
    private Integer id;

    @Column(name = "id_grand_chantier", nullable = false)
    private Integer idGrandChantier;

    @Column(nullable = false)
    private String libelle;

    public Integer getId() {
        return id;
    }

    public Integer getIdGrandChantier() {
        return idGrandChantier;
    }

    public String getLibelle() {
        return libelle;
    }
}
