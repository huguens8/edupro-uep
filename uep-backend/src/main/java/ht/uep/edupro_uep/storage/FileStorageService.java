package ht.uep.edupro_uep.storage;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * Stockage sur disque des pièces téléversées (documents de projet, protocoles d'accord des PTF).
 * La base ne garde que le chemin relatif au dossier racine (colonne chemin_stockage /
 * document_archive_url) : déplacer le dossier ne demande que de changer app.storage.dir.
 */
@Service
public class FileStorageService {

    /** UC-E1 : "Format de fichier non supporté : le système rejette le téléversement." */
    public static final Set<String> EXTENSIONS_AUTORISEES = Set.of(
            "pdf", "doc", "docx", "xls", "xlsx", "ppt", "pptx", "odt", "ods", "txt", "csv",
            "jpg", "jpeg", "png", "zip");

    private final Path racine;

    public FileStorageService(@Value("${app.storage.dir:./uploads}") String dossier) {
        this.racine = Path.of(dossier).toAbsolutePath().normalize();
    }

    /**
     * Enregistre le fichier sous {sousDossier}/{uuid}.{ext} et renvoie ce chemin relatif. Le nom
     * d'origine n'est jamais utilisé comme nom de fichier sur disque (pas de traversée de chemin).
     */
    public String enregistrer(MultipartFile fichier, String sousDossier) {
        String extension = extensionValidee(fichier);
        Path relatif = Path.of(sousDossier, UUID.randomUUID() + "." + extension);
        Path cible = resoudre(relatif.toString());
        try (InputStream in = fichier.getInputStream()) {
            Files.createDirectories(cible.getParent());
            Files.copy(in, cible, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new UncheckedIOException("Échec de l'enregistrement du fichier.", e);
        }
        return relatif.toString().replace('\\', '/');
    }

    public Resource charger(String cheminRelatif) {
        Path chemin = resoudre(cheminRelatif);
        if (!Files.isRegularFile(chemin)) {
            throw new NoSuchElementException("Fichier introuvable sur le serveur.");
        }
        return new PathResource(chemin);
    }

    public void supprimer(String cheminRelatif) {
        if (cheminRelatif == null || cheminRelatif.isBlank()) {
            return;
        }
        try {
            Files.deleteIfExists(resoudre(cheminRelatif));
        } catch (IOException e) {
            throw new UncheckedIOException("Échec de la suppression du fichier.", e);
        }
    }

    /** Extension en minuscules, contrôlée contre la liste autorisée. */
    public static String extensionValidee(MultipartFile fichier) {
        if (fichier == null || fichier.isEmpty()) {
            throw new FichierInvalideException("Aucun fichier sélectionné, ou fichier vide.");
        }
        String nom = fichier.getOriginalFilename() == null ? "" : fichier.getOriginalFilename();
        int point = nom.lastIndexOf('.');
        String extension = point < 0 ? "" : nom.substring(point + 1).toLowerCase(Locale.ROOT);
        if (!EXTENSIONS_AUTORISEES.contains(extension)) {
            throw new FichierInvalideException("Format de fichier non supporté. Formats acceptés : "
                    + String.join(", ", EXTENSIONS_AUTORISEES.stream().sorted().toList()) + ".");
        }
        return extension;
    }

    private Path resoudre(String cheminRelatif) {
        Path chemin = racine.resolve(cheminRelatif).normalize();
        if (!chemin.startsWith(racine)) {
            throw new NoSuchElementException("Fichier introuvable sur le serveur.");
        }
        return chemin;
    }
}
