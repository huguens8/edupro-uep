package ht.uep.edupro_uep.fiop;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "projet_aspect_legal")
public class AspectLegal {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "aspect_legal_seq")
    @SequenceGenerator(name = "aspect_legal_seq", sequenceName = "projet_aspect_legal_id_aspect_legal_seq", allocationSize = 1)
    @Column(name = "id_aspect_legal")
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
