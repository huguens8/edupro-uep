package ht.uep.edupro_uep.fiop;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

/** § 26 de la FIOP : "Indicateurs de résultats" (liste ordonnée, comme les aspects légaux/institutionnels). */
@Entity
@Table(name = "projet_indicateur_resultat")
public class IndicateurResultat {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "indicateur_resultat_seq")
    @SequenceGenerator(name = "indicateur_resultat_seq", sequenceName = "projet_indicateur_resultat_id_indicateur_seq", allocationSize = 1)
    @Column(name = "id_indicateur")
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
