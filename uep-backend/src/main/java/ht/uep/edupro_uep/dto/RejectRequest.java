package ht.uep.edupro_uep.dto;

import jakarta.validation.constraints.NotBlank;

public class RejectRequest {

    @NotBlank(message = "Le motif de rejet est obligatoire.")
    private String motif;

    public String getMotif() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }
}
