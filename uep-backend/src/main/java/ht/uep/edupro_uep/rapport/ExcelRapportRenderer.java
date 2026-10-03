package ht.uep.edupro_uep.rapport;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.math.BigDecimal;
import java.util.List;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import ht.uep.edupro_uep.rapport.Rapport.Champ;
import ht.uep.edupro_uep.rapport.Rapport.Section;
import ht.uep.edupro_uep.rapport.Rapport.Tableau;

/**
 * Met un {@link Rapport} en classeur Excel (.xlsx) : une feuille, sections les unes sous les
 * autres. Les montants restent des nombres (format "# ##0,00"), donc sommables et triables.
 */
@Component
public class ExcelRapportRenderer {

    private static final String FORMAT_MONTANT = "#,##0.00";

    public byte[] rendre(Rapport rapport) {
        try (XSSFWorkbook wb = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Styles s = new Styles(wb);
            Sheet sheet = wb.createSheet("Rapport");
            int nbColonnes = rapport.sections().stream()
                    .filter(sec -> sec.tableau() != null)
                    .mapToInt(sec -> sec.tableau().entetes().size())
                    .max().orElse(2);
            nbColonnes = Math.max(nbColonnes, 2);

            int r = 0;
            r = texte(sheet, r, "MENFP · Unité d'Études et de Programmation (UEP) · EduPro-UEP", s.entete, nbColonnes);
            r = texte(sheet, r, rapport.titre(), s.titre, nbColonnes);
            r = texte(sheet, r, rapport.sousTitre(), s.sousTitre, nbColonnes);
            r++;

            for (Section section : rapport.sections()) {
                r = texte(sheet, r, section.titre(), s.titreSection, nbColonnes);
                for (Champ c : section.champs()) {
                    Row row = sheet.createRow(r);
                    cellule(row, 0, c.libelle(), s.libelle);
                    cellule(row, 1, c.valeur(), s.valeur);
                    if (nbColonnes > 2) {
                        sheet.addMergedRegion(new CellRangeAddress(r, r, 1, nbColonnes - 1));
                    }
                    r++;
                }
                if (section.tableau() != null) {
                    r = tableau(sheet, r, section.tableau(), s);
                }
                if (section.note() != null) {
                    r = texte(sheet, r, section.note(), s.note, nbColonnes);
                }
                r++;
            }

            for (int c = 0; c < nbColonnes; c++) {
                sheet.autoSizeColumn(c);
                int largeur = Math.min(Math.max(sheet.getColumnWidth(c), 12 * 256), 60 * 256);
                sheet.setColumnWidth(c, largeur);
            }
            sheet.setColumnWidth(0, Math.max(sheet.getColumnWidth(0), 22 * 256));

            wb.write(out);
            return out.toByteArray();
        } catch (IOException e) {
            throw new UncheckedIOException("Génération du rapport Excel impossible", e);
        }
    }

    private int tableau(Sheet sheet, int r, Tableau t, Styles s) {
        Row entete = sheet.createRow(r++);
        for (int c = 0; c < t.entetes().size(); c++) {
            cellule(entete, c, t.entetes().get(c), s.enteteTableau);
        }
        for (List<Object> ligne : t.lignes()) {
            Row row = sheet.createRow(r++);
            for (int c = 0; c < ligne.size(); c++) {
                valeur(row, c, ligne.get(c), s.texteCellule, s.texteCelluleCentre, s.texteCelluleDroite, s.montant);
            }
        }
        if (t.total() != null) {
            Row row = sheet.createRow(r++);
            for (int c = 0; c < t.total().size(); c++) {
                valeur(row, c, t.total().get(c), s.texteTotal, s.texteTotalCentre, s.texteTotalDroite, s.montantTotal);
            }
        }
        return r;
    }

    /** Valeur absente ("—") centrée, pourcentage aligné à droite comme les montants. */
    private void valeur(Row row, int c, Object valeur, CellStyle styleTexte, CellStyle styleCentre, CellStyle styleDroite,
            CellStyle styleMontant) {
        Cell cell = row.createCell(c);
        if (valeur instanceof BigDecimal nombre) {
            cell.setCellValue(nombre.doubleValue());
            cell.setCellStyle(styleMontant);
        } else {
            String texte = valeur == null ? "—" : valeur.toString();
            cell.setCellValue(texte);
            cell.setCellStyle("—".equals(texte) ? styleCentre : texte.endsWith("%") ? styleDroite : styleTexte);
        }
    }

    /**
     * Ligne de texte (titre, note...) fusionnée sur toute la largeur : l'ajustement automatique des
     * colonnes ignore les cellules fusionnées, sinon la colonne A prendrait la longueur du titre.
     */
    private int texte(Sheet sheet, int r, String texte, CellStyle style, int nbColonnes) {
        Row row = sheet.createRow(r);
        cellule(row, 0, texte, style);
        if (nbColonnes > 1) {
            sheet.addMergedRegion(new CellRangeAddress(r, r, 0, nbColonnes - 1));
        }
        return r + 1;
    }

    private void cellule(Row row, int c, String texte, CellStyle style) {
        Cell cell = row.createCell(c);
        cell.setCellValue(texte);
        cell.setCellStyle(style);
    }

    /** Styles partagés du classeur (POI limite leur nombre : on les crée une seule fois). */
    private static final class Styles {
        final CellStyle entete, titre, sousTitre, titreSection, libelle, valeur, note;
        final CellStyle enteteTableau, texteCellule, montant, texteTotal, montantTotal;
        final CellStyle texteCelluleCentre, texteCelluleDroite, texteTotalCentre, texteTotalDroite;

        Styles(XSSFWorkbook wb) {
            short formatMontant = wb.createDataFormat().getFormat(FORMAT_MONTANT);

            entete = style(wb, police(wb, 9, false, false, IndexedColors.GREY_50_PERCENT), null, false);
            titre = style(wb, police(wb, 14, true, false, IndexedColors.DARK_BLUE), null, false);
            sousTitre = style(wb, police(wb, 9, false, true, IndexedColors.GREY_50_PERCENT), null, false);
            titreSection = style(wb, police(wb, 11, true, false, IndexedColors.DARK_BLUE), null, false);
            libelle = style(wb, police(wb, 10, true, false, IndexedColors.BLACK), IndexedColors.PALE_BLUE, true);
            valeur = style(wb, police(wb, 10, false, false, IndexedColors.BLACK), null, true);
            note = style(wb, police(wb, 9, false, true, IndexedColors.GREY_50_PERCENT), null, false);

            enteteTableau = style(wb, police(wb, 10, true, false, IndexedColors.WHITE), IndexedColors.DARK_BLUE, true);
            enteteTableau.setWrapText(true);
            enteteTableau.setVerticalAlignment(VerticalAlignment.CENTER);
            texteCellule = style(wb, police(wb, 10, false, false, IndexedColors.BLACK), null, true);
            montant = style(wb, police(wb, 10, false, false, IndexedColors.BLACK), null, true);
            montant.setDataFormat(formatMontant);
            montant.setAlignment(HorizontalAlignment.RIGHT);
            texteTotal = style(wb, police(wb, 10, true, false, IndexedColors.DARK_BLUE), IndexedColors.PALE_BLUE, true);
            montantTotal = style(wb, police(wb, 10, true, false, IndexedColors.DARK_BLUE), IndexedColors.PALE_BLUE, true);
            montantTotal.setDataFormat(formatMontant);
            montantTotal.setAlignment(HorizontalAlignment.RIGHT);

            texteCelluleCentre = aligne(wb, texteCellule, HorizontalAlignment.CENTER);
            texteCelluleDroite = aligne(wb, texteCellule, HorizontalAlignment.RIGHT);
            texteTotalCentre = aligne(wb, texteTotal, HorizontalAlignment.CENTER);
            texteTotalDroite = aligne(wb, texteTotal, HorizontalAlignment.RIGHT);
        }

        private static CellStyle aligne(XSSFWorkbook wb, CellStyle base, HorizontalAlignment alignement) {
            CellStyle st = wb.createCellStyle();
            st.cloneStyleFrom(base);
            st.setAlignment(alignement);
            return st;
        }

        private static Font police(XSSFWorkbook wb, int taille, boolean gras, boolean italique, IndexedColors couleur) {
            Font f = wb.createFont();
            f.setFontName("Calibri");
            f.setFontHeightInPoints((short) taille);
            f.setBold(gras);
            f.setItalic(italique);
            f.setColor(couleur.getIndex());
            return f;
        }

        private static CellStyle style(XSSFWorkbook wb, Font police, IndexedColors fond, boolean bordure) {
            CellStyle st = wb.createCellStyle();
            st.setFont(police);
            if (fond != null) {
                st.setFillForegroundColor(fond.getIndex());
                st.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            }
            if (bordure) {
                st.setBorderTop(BorderStyle.THIN);
                st.setBorderBottom(BorderStyle.THIN);
                st.setBorderLeft(BorderStyle.THIN);
                st.setBorderRight(BorderStyle.THIN);
                short gris = IndexedColors.GREY_25_PERCENT.getIndex();
                st.setTopBorderColor(gris);
                st.setBottomBorderColor(gris);
                st.setLeftBorderColor(gris);
                st.setRightBorderColor(gris);
            }
            return st;
        }
    }
}
