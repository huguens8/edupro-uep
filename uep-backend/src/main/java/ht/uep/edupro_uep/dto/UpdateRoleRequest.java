package ht.uep.edupro_uep.dto;

import ht.uep.edupro_uep.user.Role;
import jakarta.validation.constraints.NotNull;

public class UpdateRoleRequest {

    @NotNull(message = "Le rôle est obligatoire.")
    private Role role;

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
