package ht.uep.edupro_uep.dto;

import java.time.LocalDate;

public class PtfResponse {

    private Integer id;
    private String nom;
    private String pays;
    private String telephone;
    private String courriel;
    private LocalDate dateProtocole;
    private String referenceProtocole;
    private boolean protocoleArchive;
    private boolean engageSurUnProjet;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPays() {
        return pays;
    }

    public void setPays(String pays) {
        this.pays = pays;
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

    public LocalDate getDateProtocole() {
        return dateProtocole;
    }

    public void setDateProtocole(LocalDate dateProtocole) {
        this.dateProtocole = dateProtocole;
    }

    public String getReferenceProtocole() {
        return referenceProtocole;
    }

    public void setReferenceProtocole(String referenceProtocole) {
        this.referenceProtocole = referenceProtocole;
    }

    public boolean isProtocoleArchive() {
        return protocoleArchive;
    }

    public void setProtocoleArchive(boolean protocoleArchive) {
        this.protocoleArchive = protocoleArchive;
    }

    public boolean isEngageSurUnProjet() {
        return engageSurUnProjet;
    }

    public void setEngageSurUnProjet(boolean engageSurUnProjet) {
        this.engageSurUnProjet = engageSurUnProjet;
    }
}
