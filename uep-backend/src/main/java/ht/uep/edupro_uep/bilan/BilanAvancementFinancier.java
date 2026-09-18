package ht.uep.edupro_uep.bilan;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

/** § 43 de la FIOP : "Avancement financier des activités du projet". */
@Entity
@Table(name = "bilan_avancement_financier")
public class BilanAvancementFinancier {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bilan_avancement_fin_seq")
    @SequenceGenerator(name = "bilan_avancement_fin_seq", sequenceName = "bilan_avancement_financier_id_avancement_fin_seq", allocationSize = 1)
    @Column(name = "id_avancement_fin")
    private Integer id;

    @Column(name = "id_bilan", nullable = false)
    private Integer idBilan;

    @Column(name = "id_activite", nullable = false)
    private Integer idActivite;

    @Column(name = "montant_prevu")
    private BigDecimal montantPrevu;

    @Column(name = "depenses_anterieures_n2")
    private BigDecimal depensesAnterieuresN2;

    @Column(name = "depenses_exercice")
    private BigDecimal depensesExercice;

    @Column(name = "depenses_cumulees")
    private BigDecimal depensesCumulees;

    @Column(name = "balance_previsionnelle")
    private BigDecimal balancePrevisionnelle;

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

    public BigDecimal getMontantPrevu() {
        return montantPrevu;
    }

    public void setMontantPrevu(BigDecimal montantPrevu) {
        this.montantPrevu = montantPrevu;
    }

    public BigDecimal getDepensesAnterieuresN2() {
        return depensesAnterieuresN2;
    }

    public void setDepensesAnterieuresN2(BigDecimal depensesAnterieuresN2) {
        this.depensesAnterieuresN2 = depensesAnterieuresN2;
    }

    public BigDecimal getDepensesExercice() {
        return depensesExercice;
    }

    public void setDepensesExercice(BigDecimal depensesExercice) {
        this.depensesExercice = depensesExercice;
    }

    public BigDecimal getDepensesCumulees() {
        return depensesCumulees;
    }

    public void setDepensesCumulees(BigDecimal depensesCumulees) {
        this.depensesCumulees = depensesCumulees;
    }

    public BigDecimal getBalancePrevisionnelle() {
        return balancePrevisionnelle;
    }

    public void setBalancePrevisionnelle(BigDecimal balancePrevisionnelle) {
        this.balancePrevisionnelle = balancePrevisionnelle;
    }
}
