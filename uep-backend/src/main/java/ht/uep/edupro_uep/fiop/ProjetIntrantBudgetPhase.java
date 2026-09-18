package ht.uep.edupro_uep.fiop;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

/**
 * § 28 de la FIOP : "Intrants — Budget du projet par phases". Contrairement au calendrier des
 * dépenses (§34), les 5 "Voies et Moyens" ne sont pas une table de référence en base : ce sont des
 * libellés fixes (voir FiopService.VOIES_MOYENS_BUDGET), stockés tels quels ici.
 */
@Entity
@Table(name = "projet_intrant_budget_phase")
public class ProjetIntrantBudgetPhase {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "intrant_budget_phase_seq")
    @SequenceGenerator(name = "intrant_budget_phase_seq", sequenceName = "projet_intrant_budget_phase_id_intrant_seq", allocationSize = 1)
    @Column(name = "id_intrant")
    private Integer id;

    @Column(name = "id_projet", nullable = false)
    private Integer idProjet;

    @Column(name = "voie_moyen", nullable = false)
    private String voieMoyen;

    @Column(nullable = false)
    private String phase;

    @Column(nullable = false)
    private BigDecimal montant;

    public Integer getId() {
        return id;
    }

    public Integer getIdProjet() {
        return idProjet;
    }

    public void setIdProjet(Integer idProjet) {
        this.idProjet = idProjet;
    }

    public String getVoieMoyen() {
        return voieMoyen;
    }

    public void setVoieMoyen(String voieMoyen) {
        this.voieMoyen = voieMoyen;
    }

    public String getPhase() {
        return phase;
    }

    public void setPhase(String phase) {
        this.phase = phase;
    }

    public BigDecimal getMontant() {
        return montant;
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }
}
