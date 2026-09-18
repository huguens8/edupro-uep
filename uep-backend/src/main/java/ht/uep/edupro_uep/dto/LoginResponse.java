package ht.uep.edupro_uep.dto;

public class LoginResponse {

    private String token;
    private String username;
    private String role;
    private String roleLibelle;

    public LoginResponse(String token, String username, String role, String roleLibelle) {
        this.token = token;
        this.username = username;
        this.role = role;
        this.roleLibelle = roleLibelle;
    }

    public String getToken() {
        return token;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }

    public String getRoleLibelle() {
        return roleLibelle;
    }
}
