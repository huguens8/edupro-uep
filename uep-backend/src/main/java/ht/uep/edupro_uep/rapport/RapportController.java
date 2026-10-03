package ht.uep.edupro_uep.rapport;

import java.nio.charset.StandardCharsets;
import java.text.Normalizer;

import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * UC-F3 "Générer un rapport FIOP / PIP" — réservé aux Superviseurs UEP et MPCE (cahier des charges,
 * §6 Acteurs). Le fichier est renvoyé directement en téléchargement.
 */
@RestController
@RequestMapping("/api/rapports")
@PreAuthorize("hasAnyRole('SUPERVISEUR_UEP', 'SUPERVISEUR_MPCE')")
public class RapportController {

    private final RapportService rapportService;
    private final PdfRapportRenderer pdfRenderer;
    private final ExcelRapportRenderer excelRenderer;

    public RapportController(RapportService rapportService, PdfRapportRenderer pdfRenderer, ExcelRapportRenderer excelRenderer) {
        this.rapportService = rapportService;
        this.pdfRenderer = pdfRenderer;
        this.excelRenderer = excelRenderer;
    }

    /** Fiche d'un projet ; avec {@code idExercice}, ajoute le bilan et la tranche annuelle de cet exercice. */
    @GetMapping("/projets/{projetId}")
    public ResponseEntity<byte[]> ficheProjet(@PathVariable Integer projetId,
            @RequestParam(defaultValue = "pdf") String format,
            @RequestParam(required = false) Integer idExercice,
            Authentication authentication) {
        FormatRapport f = FormatRapport.depuis(format);
        Rapport rapport = rapportService.ficheProjet(projetId, idExercice, authentication.getName());
        String nom = "Rapport_FIOP-" + projetId
                + (idExercice != null ? "_" + rapportService.libelleExercice(idExercice) : "");
        return fichier(rapport, f, nom);
    }

    /** Portefeuille des projets pour un exercice, éventuellement limité à un statut. */
    @GetMapping("/portefeuille")
    public ResponseEntity<byte[]> portefeuille(@RequestParam(defaultValue = "pdf") String format,
            @RequestParam(required = false) Integer idExercice,
            @RequestParam(required = false) String statut,
            Authentication authentication) {
        FormatRapport f = FormatRapport.depuis(format);
        Rapport rapport = rapportService.portefeuille(idExercice, statut, authentication.getName());
        return fichier(rapport, f, "Portefeuille_projets_" + rapportService.libelleExercice(idExercice));
    }

    private ResponseEntity<byte[]> fichier(Rapport rapport, FormatRapport format, String nomSansExtension) {
        byte[] contenu = format == FormatRapport.PDF ? pdfRenderer.rendre(rapport) : excelRenderer.rendre(rapport);
        String nom = nettoyer(nomSansExtension) + "." + format.getExtension();
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(format.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment().filename(nom, StandardCharsets.UTF_8).build().toString())
                .body(contenu);
    }

    /** Nom de fichier sûr : sans accents ni caractères spéciaux. */
    private static String nettoyer(String nom) {
        String sansAccents = Normalizer.normalize(nom, Normalizer.Form.NFD).replaceAll("\\p{M}", "");
        return sansAccents.replaceAll("[^A-Za-z0-9_.-]", "_");
    }
}
