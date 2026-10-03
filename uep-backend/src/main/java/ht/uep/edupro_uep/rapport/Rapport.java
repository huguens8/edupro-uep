package ht.uep.edupro_uep.rapport;

import java.util.ArrayList;
import java.util.List;

/**
 * Contenu d'un rapport (UC-F3), indépendant du format : la même instance est rendue en PDF par
 * {@link PdfRapportRenderer} et en Excel par {@link ExcelRapportRenderer}, ce qui garantit que les
 * deux fichiers contiennent exactement les mêmes chiffres.
 *
 * Les cellules des tableaux sont des {@code String} ou des nombres ({@code BigDecimal}) : les
 * nombres restent numériques dans Excel (sommables) et sont formatés à la française dans le PDF.
 */
public record Rapport(String titre, String sousTitre, boolean paysage, List<Section> sections) {

    /** Une partie du rapport : des champs "libellé : valeur", puis éventuellement un tableau et une note. */
    public record Section(String titre, List<Champ> champs, Tableau tableau, String note) {

        public static Section champs(String titre, List<Champ> champs) {
            return new Section(titre, champs, null, null);
        }

        public static Section tableau(String titre, Tableau tableau, String note) {
            return new Section(titre, List.of(), tableau, note);
        }

        public static Section note(String titre, String note) {
            return new Section(titre, List.of(), null, note);
        }
    }

    public record Champ(String libelle, String valeur) {
    }

    /**
     * Tableau de données. {@code total} (facultatif) est la ligne de total, de même longueur que les
     * en-têtes. {@code largeurs} donne les largeurs relatives des colonnes (PDF et Excel).
     */
    public record Tableau(List<String> entetes, float[] largeurs, List<List<Object>> lignes, List<Object> total) {
    }

    /** Petit constructeur de listes de cellules hétérogènes (texte et nombres). */
    public static List<Object> ligne(Object... cellules) {
        List<Object> l = new ArrayList<>(cellules.length);
        for (Object c : cellules) {
            l.add(c);
        }
        return l;
    }
}
