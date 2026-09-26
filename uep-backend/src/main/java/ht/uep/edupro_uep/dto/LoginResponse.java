package ht.uep.edupro_uep.dto;

public class LoginResponse {

    private String token;
    private String username;
    private String role;
    private String roleLibelle;
    private boolean mustChangePassword;

    public LoginResponse(String token, String username, String role, String roleLibelle,
            boolean mustChangePassword) {
        this.token = token;
        this.username = username;
        this.role = role;
        this.roleLibelle = roleLibelle;
        this.mustChangePassword = mustChangePassword;
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

    public boolean isMustChangePassword() {
        return mustChangePassword;
    }
}
