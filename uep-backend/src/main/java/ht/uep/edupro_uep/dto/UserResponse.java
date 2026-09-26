package ht.uep.edupro_uep.dto;

public class UserResponse {

    private Integer id;
    private String username;
    private String email;
    private String role;
    private String roleLibelle;
    private boolean active;
    // null si le compte peut être supprimé ; sinon, la raison à afficher à l'administrateur.
    private String deletionBlockedReason;

    public UserResponse(Integer id, String username, String email, String role, String roleLibelle, boolean active,
            String deletionBlockedReason) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.role = role;
        this.roleLibelle = roleLibelle;
        this.active = active;
        this.deletionBlockedReason = deletionBlockedReason;
    }

    public Integer getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public String getRoleLibelle() {
        return roleLibelle;
    }

    public boolean isActive() {
        return active;
    }

    public String getDeletionBlockedReason() {
        return deletionBlockedReason;
    }
}
