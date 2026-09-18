package ht.uep.edupro_uep.reference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/** Référentiel des sources de financement (catégorie ∈ {Nationale, Externe}). */
@Entity
@Table(name = "source_financement")
public class SourceFinancement {

    @Id
    @Column(name = "id_source")
    private Integer id;

    @Column(name = "id_type_source", nullable = false)
    private Integer idTypeSource;

    @Column(nullable = false)
    private String libelle;

    @Column(nullable = false)
    private String categorie;

    public Integer getId() {
        return id;
    }

    public Integer getIdTypeSource() {
        return idTypeSource;
    }

    public String getLibelle() {
        return libelle;
    }

    public String getCategorie() {
        return categorie;
    }
}
