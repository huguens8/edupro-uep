package ht.uep.edupro_uep.rapport;

import java.util.Locale;

/** Formats d'export proposés par UC-F3 : PDF ou Excel. */
public enum FormatRapport {
    PDF("pdf", "application/pdf"),
    XLSX("xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

    private final String extension;
    private final String contentType;

    FormatRapport(String extension, String contentType) {
        this.extension = extension;
        this.contentType = contentType;
    }

    public String getExtension() {
        return extension;
    }

    public String getContentType() {
        return contentType;
    }

    /** Accepte "pdf", "xlsx" ou "excel" (sans tenir compte de la casse). */
    public static FormatRapport depuis(String valeur) {
        String v = valeur == null ? "" : valeur.trim().toLowerCase(Locale.ROOT);
        return switch (v) {
            case "pdf" -> PDF;
            case "xlsx", "excel" -> XLSX;
            default -> throw new ParametreRapportInvalideException("Format de rapport inconnu : " + valeur + " (attendu : pdf ou xlsx).");
        };
    }
}
