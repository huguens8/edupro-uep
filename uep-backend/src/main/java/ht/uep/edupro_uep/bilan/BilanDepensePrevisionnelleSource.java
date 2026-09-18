package ht.uep.edupro_uep.bilan;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

/** § 39 de la FIOP : "Évolution financière du projet" (prévisions par source de financement). */
@Entity
@Table(name = "bilan_depense_previsionnelle_source")
public class BilanDepensePrevisionnelleSource {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bilan_depense_prev_source_seq")
    @SequenceGenerator(name = "bilan_depense_prev_source_seq", sequenceName = "bilan_depense_previsionnelle_sour_id_depense_previsionnelle_seq", allocationSize = 1)
    @Column(name = "id_depense_previsionnelle")
    private Integer id;

    @Column(name = "id_bilan", nullable = false)
    private Integer idBilan;

    @Column(name = "id_source", nullable = false)
    private Integer idSource;

    @Column(name = "prevision_total")
    private BigDecimal previsionTotal;

    @Column(name = "poids_pct")
    private BigDecimal poidsPct;

    public Integer getId() {
        return id;
    }

    public Integer getIdBilan() {
        return idBilan;
    }

    public void setIdBilan(Integer idBilan) {
        this.idBilan = idBilan;
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

    public BigDecimal getPoidsPct() {
        return poidsPct;
    }

    public void setPoidsPct(BigDecimal poidsPct) {
        this.poidsPct = poidsPct;
    }
}
