package ht.uep.edupro_uep.rapport;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

/**
 * Formatage des nombres des rapports à la française (1 000 000,00). Le séparateur de milliers est
 * une espace insécable U+00A0 et non l'espace fine U+202F que Java utilise pour le français : cette
 * dernière n'existe pas dans la police Helvetica standard des PDF et s'afficherait mal.
 */
final class Montants {

    private Montants() {
    }

    private static DecimalFormat format(String motif) {
        DecimalFormatSymbols symboles = new DecimalFormatSymbols();
        symboles.setDecimalSeparator(',');
        symboles.setGroupingSeparator(' ');
        return new DecimalFormat(motif, symboles);
    }

    /** 1234567.5 -> "1 234 567,50" */
    static String nombre(BigDecimal valeur) {
        return format("#,##0.00").format(valeur);
    }

    /** Quantités : décimales seulement si nécessaires (12 ou 12,5). */
    static String quantite(BigDecimal valeur) {
        return format("#,##0.##").format(valeur);
    }

    static String gourdes(BigDecimal valeur) {
        return nombre(valeur) + " G";
    }

    static String pourcent(BigDecimal valeur) {
        return format("0.00").format(valeur) + " %";
    }
}
