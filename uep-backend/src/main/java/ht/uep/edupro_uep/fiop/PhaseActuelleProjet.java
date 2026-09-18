package ht.uep.edupro_uep.fiop;

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
 * §30 de la FIOP : historique des phases traversées par le projet
 * (Elaboration -> Planification -> Exécution -> Evaluation d'Impact).
 * Un projet peut avoir plusieurs lignes ici au fil du temps ; la phase
 * "actuelle" est la plus récente (voir PhaseActuelleProjetRepository).
 */
@Entity
@Table(name = "projet_phase_actuelle")
public class PhaseActuelleProjet {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "projet_phase_actuelle_seq")
    @SequenceGenerator(name = "projet_phase_actuelle_seq", sequenceName = "projet_phase_actuelle_id_phase_actuelle_seq", allocationSize = 1)
    @Column(name = "id_phase_actuelle")
    private Integer id;

    @Column(name = "id_projet", nullable = false)
    private Integer idProjet;

    @Column(nullable = false)
    private String phase;

    private String periode;

    @CreationTimestamp
    @Column(name = "date_changement", nullable = false, updatable = false)
    private LocalDateTime dateChangement;

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

    public String getPeriode() {
        return periode;
    }

    public void setPeriode(String periode) {
        this.periode = periode;
    }

    public LocalDateTime getDateChangement() {
        return dateChangement;
    }
}
