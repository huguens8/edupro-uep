package ht.uep.edupro_uep.fiop;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

/** role ∈ {Supervision, Execution, Bailleur, Agence1, Agence2} (contrainte CHECK en base). */
@Entity
@Table(name = "projet_intervenant")
public class Intervenant {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "intervenant_seq")
    @SequenceGenerator(name = "intervenant_seq", sequenceName = "projet_intervenant_id_intervenant_seq", allocationSize = 1)
    @Column(name = "id_intervenant")
    private Integer id;

    @Column(name = "id_projet", nullable = false)
    private Integer idProjet;

    @Column(name = "id_ptf")
    private Integer idPtf;

    @Column(nullable = false)
    private String role;

    @Column(name = "nom_charge")
    private String nomCharge;

    private String telephone;

    private String courriel;

    public Integer getId() {
        return id;
    }

    public Integer getIdProjet() {
        return idProjet;
    }

    public void setIdProjet(Integer idProjet) {
        this.idProjet = idProjet;
    }

    public Integer getIdPtf() {
        return idPtf;
    }

    public void setIdPtf(Integer idPtf) {
        this.idPtf = idPtf;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getNomCharge() {
        return nomCharge;
    }

    public void setNomCharge(String nomCharge) {
        this.nomCharge = nomCharge;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getCourriel() {
        return courriel;
    }

    public void setCourriel(String courriel) {
        this.courriel = courriel;
    }
}
