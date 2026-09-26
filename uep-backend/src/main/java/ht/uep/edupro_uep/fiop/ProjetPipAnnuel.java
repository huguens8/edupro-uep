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
 * § 38 de la FIOP : "Programme d'Investissement Public (PIP)" — une ligne par année (1 à 5),
 * avec le budget prévisionnel et le budget alloué du plan. Le "budget réel" (exécution) est
 * saisi par exercice dans le Bilan d'Exécution ({@link ht.uep.edupro_uep.bilan.BilanPipAnnuel}).
 */
@Entity
@Table(name = "projet_pip_annuel")
public class ProjetPipAnnuel {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "projet_pip_annuel_seq")
    @SequenceGenerator(name = "projet_pip_annuel_seq", sequenceName = "projet_pip_annuel_id_pip_annuel_seq", allocationSize = 1)
    @Column(name = "id_pip_annuel")
    private Integer id;

    @Column(name = "id_projet", nullable = false)
    private Integer idProjet;

    @Column(name = "annee_numero", nullable = false)
    private Short anneeNumero;

    @Column(name = "id_exercice")
    private Integer idExercice;

    @Column(name = "budget_previsionnel")
    private BigDecimal budgetPrevisionnel;

    @Column(name = "budget_alloue")
    private BigDecimal budgetAlloue;

    public Integer getId() {
        return id;
    }

    public Integer getIdProjet() {
        return idProjet;
    }

    public void setIdProjet(Integer idProjet) {
        this.idProjet = idProjet;
    }

    public Short getAnneeNumero() {
        return anneeNumero;
    }

    public void setAnneeNumero(Short anneeNumero) {
        this.anneeNumero = anneeNumero;
    }

    public Integer getIdExercice() {
        return idExercice;
    }

    public void setIdExercice(Integer idExercice) {
        this.idExercice = idExercice;
    }

    public BigDecimal getBudgetPrevisionnel() {
        return budgetPrevisionnel;
    }

    public void setBudgetPrevisionnel(BigDecimal budgetPrevisionnel) {
        this.budgetPrevisionnel = budgetPrevisionnel;
    }

    public BigDecimal getBudgetAlloue() {
        return budgetAlloue;
    }

    public void setBudgetAlloue(BigDecimal budgetAlloue) {
        this.budgetAlloue = budgetAlloue;
    }
}
