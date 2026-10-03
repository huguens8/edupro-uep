package ht.uep.edupro_uep.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class PtfRequest {

    @NotBlank(message = "Le nom du partenaire est obligatoire.")
    @Size(max = 200, message = "200 caractères maximum.")
    private String nom;

    @Size(max = 100, message = "100 caractères maximum.")
    private String pays;

    @Size(max = 30, message = "30 caractères maximum.")
    private String telephone;

    @Email(message = "Adresse e-mail invalide.")
    @Size(max = 100, message = "100 caractères maximum.")
    private String courriel;

    private LocalDate dateProtocole;

    @Size(max = 100, message = "100 caractères maximum.")
    private String referenceProtocole;

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
}
