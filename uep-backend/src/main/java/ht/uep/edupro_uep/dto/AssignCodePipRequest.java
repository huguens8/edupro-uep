package ht.uep.edupro_uep.dto;

import jakarta.validation.constraints.NotBlank;

public class AssignCodePipRequest {

    @NotBlank(message = "Le code PIP est obligatoire.")
    private String codeInternePip;

    public String getCodeInternePip() {
        return codeInternePip;
    }

    public void setCodeInternePip(String codeInternePip) {
        this.codeInternePip = codeInternePip;
    }
}
