package ht.uep.edupro_uep.user;

/**
 * Rôles disponibles pour un compte utilisateur.
 * Les constantes suivent la convention Java (UPPER_SNAKE_CASE) ; leur
 * correspondance exacte avec les valeurs stockées en base (contrainte
 * CHECK sur utilisateur.role, qui inclut des accents) est gérée par
 * {@link RoleConverter}, pas par le nom de l'enum lui-même.
 */
public enum Role {
    ADMINISTRATEUR("Administrateur", "Administrateur"),
    OPERATEUR_SAISIE("Opérateur_Saisie", "Opérateur de saisie"),
    SUPERVISEUR_UEP("Superviseur_UEP", "Superviseur UEP"),
    SUPERVISEUR_MPCE("Superviseur_MPCE", "Superviseur MPCE");

    private final String dbValue;
    private final String libelle;

    Role(String dbValue, String libelle) {
        this.dbValue = dbValue;
        this.libelle = libelle;
    }

    public String getDbValue() {
        return dbValue;
    }

    public String getLibelle() {
        return libelle;
    }

    public static Role fromDbValue(String dbValue) {
        for (Role role : values()) {
            if (role.dbValue.equals(dbValue)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Rôle inconnu en base : " + dbValue);
    }
}
