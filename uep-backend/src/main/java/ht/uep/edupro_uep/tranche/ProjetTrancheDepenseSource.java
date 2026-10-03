package ht.uep.edupro_uep.tranche;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

/** § 44 de la FIOP : "Dépenses prévisionnelles (Exercice Actuel)" d'une source de financement. */
@Entity
@Table(name = "projet_tranche_depense_source")
public class ProjetTrancheDepenseSource {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "projet_tranche_depense_source_seq")
    @SequenceGenerator(name = "projet_tranche_depense_source_seq", sequenceName = "projet_tranche_depense_source_id_tranche_depense_seq", allocationSize = 1)
    @Column(name = "id_tranche_depense")
    private Integer id;

    @Column(name = "id_projet", nullable = false)
    private Integer idProjet;

    @Column(name = "id_exercice", nullable = false)
    private Integer idExercice;

    @Column(name = "id_source", nullable = false)
    private Integer idSource;

    @Column(name = "prevision_total", nullable = false)
    private BigDecimal previsionTotal;

    public Integer getId() {
        return id;
    }

    public Integer getIdProjet() {
        return idProjet;
    }

    public void setIdProjet(Integer idProjet) {
        this.idProjet = idProjet;
    }

    public Integer getIdExercice() {
        return idExercice;
    }

    public void setIdExercice(Integer idExercice) {
        this.idExercice = idExercice;
    }

    public Integer getIdSource() {
        return idSource;
    }

    public void setIdSource(Integer idSource) {
        this.idSource = idSource;
    }

    public BigDecimal getPrevisionTotal() {
        return previsionTotal;
    }

    public void setPrevisionTotal(BigDecimal previsionTotal) {
        this.previsionTotal = previsionTotal;
    }
}
