package ht.uep.edupro_uep.fiop;

/**
 * Cycle de vie du FIOP (validation à deux niveaux : Superviseur UEP puis
 * Superviseur MPCE). Les valeurs stockées en base (contrainte CHECK sur
 * projet.statut) sont gérées par {@link StatutFiopConverter}.
 *
 * Brouillon -> SoumisSuperviseurUep -> SoumisMpce -> ValideActif
 *                     \-> RejeteUep         \-> RejeteMpce
 */
public enum StatutFiop {
    BROUILLON("Brouillon"),
    SOUMIS_SUPERVISEUR_UEP("Soumis_Superviseur_UEP"),
    REJETE_UEP("Rejete_UEP"),
    SOUMIS_MPCE("Soumis_MPCE"),
    REJETE_MPCE("Rejete_MPCE"),
    VALIDE_ACTIF("Valide_Actif");

    private final String dbValue;

    StatutFiop(String dbValue) {
        this.dbValue = dbValue;
    }

    public String getDbValue() {
        return dbValue;
    }

    public static StatutFiop fromDbValue(String dbValue) {
        for (StatutFiop statut : values()) {
            if (statut.dbValue.equals(dbValue)) {
                return statut;
            }
        }
        throw new IllegalArgumentException("Statut FIOP inconnu en base : " + dbValue);
    }
}
