package ht.uep.edupro_uep.rapport;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Component;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfWriter;

import ht.uep.edupro_uep.rapport.Rapport.Champ;
import ht.uep.edupro_uep.rapport.Rapport.Section;
import ht.uep.edupro_uep.rapport.Rapport.Tableau;

/** Met un {@link Rapport} en PDF (A4, paysage pour les tableaux larges), aux couleurs de l'application. */
@Component
public class PdfRapportRenderer {

    private static final Color BLEU = new Color(0x12, 0x3D, 0x82);
    private static final Color BLEU_CLAIR = new Color(0xEE, 0xF1, 0xF6);
    private static final Color GRIS_TEXTE = new Color(0x55, 0x60, 0x70);
    private static final Color BORDURE = new Color(0xCB, 0xD5, 0xE1);

    private static final Font ENTETE = new Font(Font.HELVETICA, 8, Font.NORMAL, GRIS_TEXTE);
    private static final Font TITRE = new Font(Font.HELVETICA, 16, Font.BOLD, BLEU);
    private static final Font SOUS_TITRE = new Font(Font.HELVETICA, 9, Font.ITALIC, GRIS_TEXTE);
    private static final Font TITRE_SECTION = new Font(Font.HELVETICA, 11, Font.BOLD, BLEU);
    private static final Font LIBELLE = new Font(Font.HELVETICA, 9, Font.BOLD, Color.DARK_GRAY);
    private static final Font VALEUR = new Font(Font.HELVETICA, 9, Font.NORMAL, Color.BLACK);
    private static final Font CELLULE = new Font(Font.HELVETICA, 8, Font.NORMAL, Color.BLACK);
    private static final Font CELLULE_ENTETE = new Font(Font.HELVETICA, 8, Font.BOLD, Color.WHITE);
    private static final Font CELLULE_TOTAL = new Font(Font.HELVETICA, 8, Font.BOLD, BLEU);
    private static final Font NOTE = new Font(Font.HELVETICA, 8, Font.ITALIC, GRIS_TEXTE);

    public byte[] rendre(Rapport rapport) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Rectangle page = rapport.paysage() ? PageSize.A4.rotate() : PageSize.A4;
        Document document = new Document(page, 36, 36, 50, 40);
        PdfWriter writer = PdfWriter.getInstance(document, out);
        writer.setPageEvent(new PiedDePage());
        document.addTitle(rapport.titre());
        document.addCreator("EduPro-UEP");
        document.open();

        Paragraph entete = new Paragraph("MENFP · Unité d'Études et de Programmation (UEP) · EduPro-UEP", ENTETE);
        document.add(entete);
        Paragraph titre = new Paragraph(rapport.titre(), TITRE);
        titre.setSpacingBefore(6);
        document.add(titre);
        Paragraph sousTitre = new Paragraph(rapport.sousTitre(), SOUS_TITRE);
        sousTitre.setSpacingAfter(8);
        document.add(sousTitre);

        for (Section section : rapport.sections()) {
            Paragraph t = new Paragraph(section.titre(), TITRE_SECTION);
            t.setSpacingBefore(10);
            t.setSpacingAfter(7);
            t.setKeepTogether(true);
            document.add(t);
            if (!section.champs().isEmpty()) {
                document.add(champs(section.champs()));
            }
            if (section.tableau() != null) {
                document.add(tableau(section.tableau()));
            }
            if (section.note() != null) {
                Paragraph note = new Paragraph(section.note(), NOTE);
                note.setSpacingBefore(3);
                document.add(note);
            }
        }
        document.close();
        return out.toByteArray();
    }

    private PdfPTable champs(List<Champ> champs) {
        PdfPTable table = new PdfPTable(new float[] { 1.3f, 4f });
        table.setWidthPercentage(100);
        table.setSpacingBefore(2);
        for (Champ c : champs) {
            PdfPCell libelle = new PdfPCell(new Phrase(c.libelle(), LIBELLE));
            libelle.setBackgroundColor(BLEU_CLAIR);
            styleBordure(libelle);
            table.addCell(libelle);
            PdfPCell valeur = new PdfPCell(new Phrase(c.valeur(), VALEUR));
            styleBordure(valeur);
            table.addCell(valeur);
        }
        return table;
    }

    private PdfPTable tableau(Tableau t) {
        PdfPTable table = new PdfPTable(t.largeurs());
        table.setWidthPercentage(100);
        table.setSpacingBefore(2);
        table.setHeaderRows(1);
        for (String e : t.entetes()) {
            PdfPCell cell = new PdfPCell(new Phrase(e, CELLULE_ENTETE));
            cell.setBackgroundColor(BLEU);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            styleBordure(cell);
            table.addCell(cell);
        }
        for (List<Object> ligne : t.lignes()) {
            for (Object valeur : ligne) {
                table.addCell(cellule(valeur, CELLULE, null));
            }
        }
        if (t.total() != null) {
            for (Object valeur : t.total()) {
                table.addCell(cellule(valeur, CELLULE_TOTAL, BLEU_CLAIR));
            }
        }
        return table;
    }

    private PdfPCell cellule(Object valeur, Font font, Color fond) {
        String texte;
        int alignement = Element.ALIGN_LEFT;
        if (valeur == null) {
            texte = "—";
            alignement = Element.ALIGN_CENTER;
        } else if (valeur instanceof BigDecimal nombre) {
            texte = Montants.nombre(nombre);
            alignement = Element.ALIGN_RIGHT;
        } else {
            texte = valeur.toString();
            if ("—".equals(texte)) {
                alignement = Element.ALIGN_CENTER;
            } else if (texte.endsWith("%")) {
                alignement = Element.ALIGN_RIGHT;
            }
        }
        PdfPCell cell = new PdfPCell(new Phrase(texte, font));
        cell.setHorizontalAlignment(alignement);
        if (fond != null) {
            cell.setBackgroundColor(fond);
        }
        styleBordure(cell);
        return cell;
    }

    private void styleBordure(PdfPCell cell) {
        cell.setBorderColor(BORDURE);
        cell.setPadding(4);
    }

    /** "Page n" en bas à droite de chaque page. */
    private static final class PiedDePage extends PdfPageEventHelper {
        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            Rectangle page = document.getPageSize();
            ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_RIGHT,
                    new Phrase("Page " + writer.getPageNumber(), ENTETE),
                    page.getRight() - document.rightMargin(), page.getBottom() + 20, 0);
            ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_LEFT,
                    new Phrase("EduPro-UEP — rapport généré automatiquement", ENTETE),
                    page.getLeft() + document.leftMargin(), page.getBottom() + 20, 0);
        }
    }
}
