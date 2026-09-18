package ht.uep.edupro_uep.fiop;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

/** § 27 de la FIOP : "Extrants" (liste ordonnée, comme les aspects légaux/institutionnels). */
@Entity
@Table(name = "projet_extrant")
public class Extrant {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "extrant_seq")
    @SequenceGenerator(name = "extrant_seq", sequenceName = "projet_extrant_id_extrant_seq", allocationSize = 1)
    @Column(name = "id_extrant")
    private Integer id;

    @Column(name = "id_projet", nullable = false)
    private Integer idProjet;

    @Column(nullable = false)
    private Short ordre;

    @Column(nullable = false, columnDefinition = "text")
    private String description;

    public Integer getId() {
        return id;
    }

    public Integer getIdProjet() {
        return idProjet;
    }

    public void setIdProjet(Integer idProjet) {
        this.idProjet = idProjet;
    }

    public Short getOrdre() {
        return ordre;
    }

    public void setOrdre(Short ordre) {
        this.ordre = ordre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
