package ht.uep.edupro_uep.fiop;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

/** Catégorie ∈ {Enfants garçons, Enfants filles, Hommes, Femmes} (contrainte CHECK en base). */
@Entity
@Table(name = "projet_population_visee")
public class PopulationVisee {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "population_visee_seq")
    @SequenceGenerator(name = "population_visee_seq", sequenceName = "projet_population_visee_id_population_seq", allocationSize = 1)
    @Column(name = "id_population")
    private Integer id;

    @Column(name = "id_projet", nullable = false)
    private Integer idProjet;

    @Column(nullable = false)
    private String categorie;

    @Column(nullable = false)
    private Integer nombre = 0;

    public Integer getId() {
        return id;
    }

    public Integer getIdProjet() {
        return idProjet;
    }

    public void setIdProjet(Integer idProjet) {
        this.idProjet = idProjet;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public Integer getNombre() {
        return nombre;
    }

    public void setNombre(Integer nombre) {
        this.nombre = nombre;
    }
}
