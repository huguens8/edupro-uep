package ht.uep.edupro_uep.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;

public class IntervenantDto {

    /** Renseigné pour le bailleur : le PTF choisi dans la liste des partenaires. */
    private Integer idPtf;

    private String nomCharge;
    private String telephone;

    @Email(message = "Le courriel n'est pas valide.")
    private String courriel;

    public IntervenantDto() {
    }

    public IntervenantDto(Integer idPtf, String nomCharge, String telephone, String courriel) {
        this.idPtf = idPtf;
        this.nomCharge = nomCharge;
        this.telephone = telephone;
        this.courriel = courriel;
    }

    public Integer getIdPtf() {
        return idPtf;
    }

    public void setIdPtf(Integer idPtf) {
        this.idPtf = idPtf;
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

    @JsonIgnore
    public boolean isEmpty() {
        return idPtf == null && isBlank(nomCharge) && isBlank(telephone) && isBlank(courriel);
    }

    private boolean isBlank(String s) {
        return s == null || s.isBlank();
    }
}
