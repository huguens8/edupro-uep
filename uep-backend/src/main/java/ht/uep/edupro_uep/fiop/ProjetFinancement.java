package ht.uep.edupro_uep.fiop;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

/** § 29 de la FIOP : "Sources de financement du projet" (prévisions + Programme Triennal d'Investissement). */
@Entity
@Table(name = "projet_financement")
public class ProjetFinancement {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "projet_financement_seq")
    @SequenceGenerator(name = "projet_financement_seq", sequenceName = "projet_financement_id_projet_financement_seq", allocationSize = 1)
    @Column(name = "id_projet_financement")
    private Integer id;

    @Column(name = "id_projet", nullable = false)
    private Integer idProjet;

    @Column(name = "id_source", nullable = false)
    private Integer idSource;

    @Column(name = "id_exercice")
    private Integer idExercice;

    @Column(name = "prevision_total", nullable = false)
    private BigDecimal previsionTotal;

    @Column(name = "pti_annee1")
    private BigDecimal ptiAnnee1;

    @Column(name = "pti_annee2")
    private BigDecimal ptiAnnee2;

    @Column(name = "pti_annee3")
    private BigDecimal ptiAnnee3;

    @Column(name = "pti_annee4")
    private BigDecimal ptiAnnee4;

    @Column(name = "pti_annee5")
    private BigDecimal ptiAnnee5;

    public Integer getId() {
        return id;
    }

    public Integer getIdProjet() {
        return idProjet;
    }

    public void setIdProjet(Integer idProjet) {
        this.idProjet = idProjet;
    }

    public Integer getIdSource() {
        return idSource;
    }

    public void setIdSource(Integer idSource) {
        this.idSource = idSource;
    }

    public Integer getIdExercice() {
        return idExercice;
    }

    public void setIdExercice(Integer idExercice) {
        this.idExercice = idExercice;
    }

    public BigDecimal getPrevisionTotal() {
        return previsionTotal;
    }

    public void setPrevisionTotal(BigDecimal previsionTotal) {
        this.previsionTotal = previsionTotal;
    }

    public BigDecimal getPtiAnnee1() {
        return ptiAnnee1;
    }

    public void setPtiAnnee1(BigDecimal ptiAnnee1) {
        this.ptiAnnee1 = ptiAnnee1;
    }

    public BigDecimal getPtiAnnee2() {
        return ptiAnnee2;
    }

    public void setPtiAnnee2(BigDecimal ptiAnnee2) {
        this.ptiAnnee2 = ptiAnnee2;
    }

    public BigDecimal getPtiAnnee3() {
        return ptiAnnee3;
    }

    public void setPtiAnnee3(BigDecimal ptiAnnee3) {
        this.ptiAnnee3 = ptiAnnee3;
    }

    public BigDecimal getPtiAnnee4() {
        return ptiAnnee4;
    }

    public void setPtiAnnee4(BigDecimal ptiAnnee4) {
        this.ptiAnnee4 = ptiAnnee4;
    }

    public BigDecimal getPtiAnnee5() {
        return ptiAnnee5;
    }

    public void setPtiAnnee5(BigDecimal ptiAnnee5) {
        this.ptiAnnee5 = ptiAnnee5;
    }
}
