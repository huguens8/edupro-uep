package ht.uep.edupro_uep.tranche;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

/**
 * §§ 48-49 de la FIOP : "Calendrier prévisionnel d'utilisation des ressources financières"
 * nationales ({@link #NATIONAL}) ou externes ({@link #EXTERNE}) — montant d'une sous-rubrique pour
 * un trimestre de l'exercice. Le § 50 (nationales et externes) est la somme des deux.
 */
@Entity
@Table(name = "projet_calendrier_depense_trimestrielle")
public class ProjetCalendrierDepenseTrimestrielle {

    public static final String NATIONAL = "NATIONAL";
    public static final String EXTERNE = "EXTERNE";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "projet_calendrier_depense_trim_seq")
    @SequenceGenerator(name = "projet_calendrier_depense_trim_seq", sequenceName = "projet_calendrier_depense_trimestrielle_id_calendrier_trim_seq", allocationSize = 1)
    @Column(name = "id_calendrier_trim")
    private Integer id;

    @Column(name = "id_projet", nullable = false)
    private Integer idProjet;

    @Column(name = "id_rubrique", nullable = false)
    private Integer idRubrique;

    @Column(name = "id_exercice", nullable = false)
    private Integer idExercice;

    @Column(name = "categorie_financement", nullable = false, length = 10)
    private String categorieFinancement;

    @Column(nullable = false)
    private Short trimestre;

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

    public Integer getIdRubrique() {
        return idRubrique;
    }

    public void setIdRubrique(Integer idRubrique) {
        this.idRubrique = idRubrique;
    }

    public Integer getIdExercice() {
        return idExercice;
    }

    public void setIdExercice(Integer idExercice) {
        this.idExercice = idExercice;
    }

    public String getCategorieFinancement() {
        return categorieFinancement;
    }

    public void setCategorieFinancement(String categorieFinancement) {
        this.categorieFinancement = categorieFinancement;
    }

    public Short getTrimestre() {
        return trimestre;
    }

    public void setTrimestre(Short trimestre) {
        this.trimestre = trimestre;
    }

    public BigDecimal getMontant() {
        return montant;
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }
}
