package ht.uep.edupro_uep.bilan;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

/** § 42 de la FIOP : "Avancement physique des activités du projet". */
@Entity
@Table(name = "bilan_avancement_physique")
public class BilanAvancementPhysique {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bilan_avancement_phys_seq")
    @SequenceGenerator(name = "bilan_avancement_phys_seq", sequenceName = "bilan_avancement_physique_id_avancement_phys_seq", allocationSize = 1)
    @Column(name = "id_avancement_phys")
    private Integer id;

    @Column(name = "id_bilan", nullable = false)
    private Integer idBilan;

    @Column(name = "id_activite", nullable = false)
    private Integer idActivite;

    @Column(name = "resultats_prevus", columnDefinition = "text")
    private String resultatsPrevus;

    @Column(name = "resultats_obtenus", columnDefinition = "text")
    private String resultatsObtenus;

    @Column(columnDefinition = "text")
    private String ecarts;

    @Column(name = "pct_avancement_physique")
    private BigDecimal pctAvancementPhysique;

    public Integer getId() {
        return id;
    }

    public Integer getIdBilan() {
        return idBilan;
    }

    public void setIdBilan(Integer idBilan) {
        this.idBilan = idBilan;
    }

    public Integer getIdActivite() {
        return idActivite;
    }

    public void setIdActivite(Integer idActivite) {
        this.idActivite = idActivite;
    }

    public String getResultatsPrevus() {
        return resultatsPrevus;
    }

    public void setResultatsPrevus(String resultatsPrevus) {
        this.resultatsPrevus = resultatsPrevus;
    }

    public String getResultatsObtenus() {
        return resultatsObtenus;
    }

    public void setResultatsObtenus(String resultatsObtenus) {
        this.resultatsObtenus = resultatsObtenus;
    }

    public String getEcarts() {
        return ecarts;
    }

    public void setEcarts(String ecarts) {
        this.ecarts = ecarts;
    }

    public BigDecimal getPctAvancementPhysique() {
        return pctAvancementPhysique;
    }

    public void setPctAvancementPhysique(BigDecimal pctAvancementPhysique) {
        this.pctAvancementPhysique = pctAvancementPhysique;
    }
}
