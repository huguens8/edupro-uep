package ht.uep.edupro_uep.archive;

import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import ht.uep.edupro_uep.dto.ArchiveProjetResponse;

@RestController
public class ArchiveProjetController {

    private final ArchiveProjetService archiveService;

    public ArchiveProjetController(ArchiveProjetService archiveService) {
        this.archiveService = archiveService;
    }

    @GetMapping("/api/fiop/{projetId}/documents")
    @PreAuthorize("hasAnyRole('Porteur_PROJET', 'SUPERVISEUR_UEP', 'SUPERVISEUR_MPCE')")
    public List<ArchiveProjetResponse> lister(@PathVariable Integer projetId) {
        return archiveService.lister(projetId);
    }

    @PostMapping(value = "/api/fiop/{projetId}/documents", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('Porteur_PROJET')")
    public ArchiveProjetResponse archiver(@PathVariable Integer projetId,
            @RequestParam("fichier") MultipartFile fichier,
            @RequestParam("typeDocument") String typeDocument,
            @RequestParam(value = "description", required = false) String description,
            Authentication authentication) {
        return archiveService.archiver(projetId, fichier, typeDocument, description, authentication.getName());
    }

    @GetMapping("/api/documents/{id}/fichier")
    @PreAuthorize("hasAnyRole('Porteur_PROJET', 'SUPERVISEUR_UEP', 'SUPERVISEUR_MPCE')")
    public ResponseEntity<Resource> telecharger(@PathVariable Integer id) {
        return archiveService.telecharger(id);
    }

    @DeleteMapping("/api/documents/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('Porteur_PROJET')")
    public void retirer(@PathVariable Integer id, Authentication authentication) {
        archiveService.retirer(id, authentication.getName());
    }
}
