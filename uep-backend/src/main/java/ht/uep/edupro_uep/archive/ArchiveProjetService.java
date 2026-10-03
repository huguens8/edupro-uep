package ht.uep.edupro_uep.archive;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import ht.uep.edupro_uep.dto.ArchiveProjetResponse;
import ht.uep.edupro_uep.fiop.Projet;
import ht.uep.edupro_uep.fiop.ProjetRepository;
import ht.uep.edupro_uep.storage.FichierInvalideException;
import ht.uep.edupro_uep.storage.FileStorageService;
import ht.uep.edupro_uep.storage.Telechargement;
import ht.uep.edupro_uep.user.User;
import ht.uep.edupro_uep.user.UserRepository;

/**
 * UC-E1 — Archiver un document de projet. Comme le reste du FIOP, seul le Porteur de Projet
 * créateur alimente le dossier ; l'archivage n'est pas conditionné au statut du FIOP.
 */
@Service
public class ArchiveProjetService {

    private final ArchiveProjetRepository archiveRepository;
    private final ProjetRepository projetRepository;
    private final UserRepository userRepository;
    private final FileStorageService storage;

    public ArchiveProjetService(ArchiveProjetRepository archiveRepository, ProjetRepository projetRepository,
            UserRepository userRepository, FileStorageService storage) {
        this.archiveRepository = archiveRepository;
        this.projetRepository = projetRepository;
        this.userRepository = userRepository;
        this.storage = storage;
    }

    public List<ArchiveProjetResponse> lister(Integer projetId) {
        findProjetOrThrow(projetId);
        List<ArchiveProjet> documents = archiveRepository.findByIdProjetAndActifTrueOrderByDateAjoutDesc(projetId);
        Map<Integer, String> auteurs = userRepository.findAllById(documents.stream()
                .map(ArchiveProjet::getIdUtilisateurAjout).filter(id -> id != null).distinct().toList())
                .stream().collect(Collectors.toMap(User::getId, User::getUsername));
        return documents.stream().map(d -> toResponse(d, auteurs::get)).toList();
    }

    /**
     * Un document portant déjà ce nom dans le dossier devient une nouvelle version : l'ancienne
     * reste conservée (actif = false) et n'apparaît plus dans la liste courante.
     */
    @Transactional
    public ArchiveProjetResponse archiver(Integer projetId, MultipartFile fichier, String typeDocument,
            String description, String username) {
        Projet projet = findProjetOrThrow(projetId);
        User user = findUserOrThrow(username);
        requireCreateur(projet, user);
        if (typeDocument == null || typeDocument.isBlank()) {
            throw new FichierInvalideException("Le type de document est obligatoire.");
        }
        FileStorageService.extensionValidee(fichier);
        String nom = nomOrigine(fichier);

        List<ArchiveProjet> versions = archiveRepository.findByIdProjetAndNomDocumentIgnoreCase(projetId, nom);
        int derniere = versions.stream().map(ArchiveProjet::getVersion)
                .filter(v -> v != null).mapToInt(Short::intValue).max().orElse(0);
        versions.forEach(v -> v.setActif(false));

        ArchiveProjet doc = new ArchiveProjet();
        doc.setIdProjet(projetId);
        doc.setNomDocument(nom);
        doc.setTypeDocument(typeDocument.trim());
        doc.setDescription(description == null || description.isBlank() ? null : description.trim());
        doc.setVersion((short) (derniere + 1));
        doc.setIdUtilisateurAjout(user.getId());
        doc.setDateAjout(LocalDateTime.now());
        doc.setActif(true);
        doc.setCheminStockage(storage.enregistrer(fichier, "projets/" + projetId));
        archiveRepository.save(doc);
        return toResponse(doc, id -> user.getUsername());
    }

    public ResponseEntity<Resource> telecharger(Integer id) {
        ArchiveProjet doc = findOrThrow(id);
        return Telechargement.reponse(storage.charger(doc.getCheminStockage()), doc.getNomDocument());
    }

    /** Retire le document de la liste courante sans effacer le fichier : l'archive reste conservée. */
    @Transactional
    public void retirer(Integer id, String username) {
        ArchiveProjet doc = findOrThrow(id);
        requireCreateur(findProjetOrThrow(doc.getIdProjet()), findUserOrThrow(username));
        doc.setActif(false);
        archiveRepository.save(doc);
    }

    private static String nomOrigine(MultipartFile fichier) {
        String nom = fichier.getOriginalFilename() == null ? "document" : fichier.getOriginalFilename();
        // Certains navigateurs transmettent le chemin complet : on ne garde que le nom.
        nom = nom.substring(Math.max(nom.lastIndexOf('/'), nom.lastIndexOf('\\')) + 1).trim();
        return nom.length() > 255 ? nom.substring(nom.length() - 255) : nom;
    }

    private ArchiveProjet findOrThrow(Integer id) {
        return archiveRepository.findById(id)
                .filter(d -> Boolean.TRUE.equals(d.getActif()))
                .orElseThrow(() -> new NoSuchElementException("Document introuvable."));
    }

    private Projet findProjetOrThrow(Integer id) {
        return projetRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("FIOP introuvable."));
    }

    private User findUserOrThrow(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("Utilisateur introuvable."));
    }

    private static void requireCreateur(Projet projet, User user) {
        if (!user.getId().equals(projet.getIdUtilisateurCreation())) {
            throw new IllegalStateException("Seul le Porteur de Projet créateur du FIOP peut gérer ses documents.");
        }
    }

    private static ArchiveProjetResponse toResponse(ArchiveProjet doc, Function<Integer, String> auteur) {
        ArchiveProjetResponse r = new ArchiveProjetResponse();
        r.setId(doc.getId());
        r.setNomDocument(doc.getNomDocument());
        r.setTypeDocument(doc.getTypeDocument());
        r.setVersion(doc.getVersion());
        r.setDescription(doc.getDescription());
        r.setAjoutePar(doc.getIdUtilisateurAjout() == null ? null : auteur.apply(doc.getIdUtilisateurAjout()));
        r.setDateAjout(doc.getDateAjout());
        return r;
    }
}
