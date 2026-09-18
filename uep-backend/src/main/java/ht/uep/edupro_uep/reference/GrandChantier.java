package ht.uep.edupro_uep.reference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "grand_chantier")
public class GrandChantier {

    @Id
    @Column(name = "id_grand_chantier")
    private Integer id;

    @Column(nullable = false)
    private String libelle;

    public Integer getId() {
        return id;
    }

    public String getLibelle() {
        return libelle;
    }
}
