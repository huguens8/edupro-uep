package ht.uep.edupro_uep.reference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "sous_programme")
public class SousProgramme {

    @Id
    @Column(name = "id_sous_programme")
    private Integer id;

    @Column(name = "id_programme", nullable = false)
    private Integer idProgramme;

    @Column(nullable = false)
    private String libelle;

    public Integer getId() {
        return id;
    }

    public Integer getIdProgramme() {
        return idProgramme;
    }

    public String getLibelle() {
        return libelle;
    }
}
