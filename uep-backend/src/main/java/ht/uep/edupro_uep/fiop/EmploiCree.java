package ht.uep.edupro_uep.fiop;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

/** phase ∈ {Pendant, Apres}, sexe ∈ {Homme, Femme} (contraintes CHECK en base). */
@Entity
@Table(name = "projet_emploi_cree")
public class EmploiCree {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "emploi_cree_seq")
    @SequenceGenerator(name = "emploi_cree_seq", sequenceName = "projet_emploi_cree_id_emploi_seq", allocationSize = 1)
    @Column(name = "id_emploi")
    private Integer id;

    @Column(name = "id_projet", nullable = false)
    private Integer idProjet;

    @Column(nullable = false)
    private String phase;

    @Column(nullable = false)
    private String sexe;

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

    public String getPhase() {
        return phase;
    }

    public void setPhase(String phase) {
        this.phase = phase;
    }

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public Integer getNombre() {
        return nombre;
    }

    public void setNombre(Integer nombre) {
        this.nombre = nombre;
    }
}
