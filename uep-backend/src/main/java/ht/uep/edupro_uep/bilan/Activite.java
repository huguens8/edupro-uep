package ht.uep.edupro_uep.bilan;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

/**
 * Activité d'un projet. Gérée ici uniquement comme ligne du Bilan d'Exécution
 * (les colonnes de planification budgétaire ne sont pas encore exposées ;
 * futur module Activités/Chronogramme).
 */
@Entity
@Table(name = "activite")
public class Activite {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "activite_seq")
    @SequenceGenerator(name = "activite_seq", sequenceName = "activite_id_activite_seq", allocationSize = 1)
    @Column(name = "id_activite")
    private Integer id;

    @Column(name = "id_projet", nullable = false)
    private Integer idProjet;

    @Column(name = "ordre_sequentiel", nullable = false)
    private Short ordreSequentiel;

    @Column(nullable = false)
    private String libelle;

    @Column(name = "unite_resultat")
    private String uniteResultat;

    @Column(name = "quantite_resultat_attendu")
    private BigDecimal quantiteResultatAttendu;

    @Column(name = "cout_unitaire")
    private BigDecimal coutUnitaire;

    @Column(name = "cout_total")
    private BigDecimal coutTotal;

    @Column(name = "ressources_nationales")
    private BigDecimal ressourcesNationales;

    @Column(name = "ressources_externes")
    private BigDecimal ressourcesExternes;

    public Integer getId() {
        return id;
    }

    public Integer getIdProjet() {
        return idProjet;
    }

    public void setIdProjet(Integer idProjet) {
        this.idProjet = idProjet;
    }

    public Short getOrdreSequentiel() {
        return ordreSequentiel;
    }

    public void setOrdreSequentiel(Short ordreSequentiel) {
        this.ordreSequentiel = ordreSequentiel;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getUniteResultat() {
        return uniteResultat;
    }

    public void setUniteResultat(String uniteResultat) {
        this.uniteResultat = uniteResultat;
    }

    public BigDecimal getQuantiteResultatAttendu() {
        return quantiteResultatAttendu;
    }

    public void setQuantiteResultatAttendu(BigDecimal quantiteResultatAttendu) {
        this.quantiteResultatAttendu = quantiteResultatAttendu;
    }

    public BigDecimal getCoutUnitaire() {
        return coutUnitaire;
    }

    public void setCoutUnitaire(BigDecimal coutUnitaire) {
        this.coutUnitaire = coutUnitaire;
    }

    public BigDecimal getCoutTotal() {
        return coutTotal;
    }

    public void setCoutTotal(BigDecimal coutTotal) {
        this.coutTotal = coutTotal;
    }

    public BigDecimal getRessourcesNationales() {
        return ressourcesNationales;
    }

    public void setRessourcesNationales(BigDecimal ressourcesNationales) {
        this.ressourcesNationales = ressourcesNationales;
    }

    public BigDecimal getRessourcesExternes() {
        return ressourcesExternes;
    }

    public void setRessourcesExternes(BigDecimal ressourcesExternes) {
        this.ressourcesExternes = ressourcesExternes;
    }
}
