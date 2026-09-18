package ht.uep.edupro_uep.bilan;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

/**
 * Bilan d'Exécution d'un projet pour un exercice donné (DEUXIEME PARTIE DE LA
 * FIOP : "Bilan Physique et Financier du Projet", § 35 de la FIOP).
 */
@Entity
@Table(name = "bilan_exercice")
public class BilanExercice {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bilan_exercice_seq")
    @SequenceGenerator(name = "bilan_exercice_seq", sequenceName = "bilan_exercice_id_bilan_seq", allocationSize = 1)
    @Column(name = "id_bilan")
    private Integer id;

    @Column(name = "id_projet", nullable = false)
    private Integer idProjet;

    @Column(name = "id_exercice", nullable = false)
    private Integer idExercice;

    @Column(name = "date_demarrage_effective")
    private LocalDate dateDemarrageEffective;

    @Column(name = "duree_totale_projet_mois")
    private Integer dureeTotaleProjetMois;

    @Column(name = "temps_ecoule_mois")
    private Integer tempsEcouleMois;

    @Column(name = "temps_restant_mois")
    private Integer tempsRestantMois;

    @CreationTimestamp
    @Column(name = "date_creation", nullable = false, updatable = false)
    private LocalDateTime dateCreation;

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

    public LocalDate getDateDemarrageEffective() {
        return dateDemarrageEffective;
    }

    public void setDateDemarrageEffective(LocalDate dateDemarrageEffective) {
        this.dateDemarrageEffective = dateDemarrageEffective;
    }

    public Integer getDureeTotaleProjetMois() {
        return dureeTotaleProjetMois;
    }

    public void setDureeTotaleProjetMois(Integer dureeTotaleProjetMois) {
        this.dureeTotaleProjetMois = dureeTotaleProjetMois;
    }

    public Integer getTempsEcouleMois() {
        return tempsEcouleMois;
    }

    public void setTempsEcouleMois(Integer tempsEcouleMois) {
        this.tempsEcouleMois = tempsEcouleMois;
    }

    public Integer getTempsRestantMois() {
        return tempsRestantMois;
    }

    public void setTempsRestantMois(Integer tempsRestantMois) {
        this.tempsRestantMois = tempsRestantMois;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }
}
