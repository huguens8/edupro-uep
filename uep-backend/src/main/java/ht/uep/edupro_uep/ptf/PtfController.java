package ht.uep.edupro_uep.ptf;

import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import ht.uep.edupro_uep.dto.PtfRequest;
import ht.uep.edupro_uep.dto.PtfResponse;
import jakarta.validation.Valid;

/** Gestion des PTF : saisie par le Porteur de Projet, consultation par les superviseurs. */
@RestController
@RequestMapping("/api/ptf")
public class PtfController {

    private final PtfService ptfService;

    public PtfController(PtfService ptfService) {
        this.ptfService = ptfService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('Porteur_PROJET', 'SUPERVISEUR_UEP', 'SUPERVISEUR_MPCE')")
    public List<PtfResponse> lister() {
        return ptfService.lister();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('Porteur_PROJET')")
    public PtfResponse creer(@Valid @RequestBody PtfRequest request) {
        return ptfService.creer(request);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('Porteur_PROJET')")
    public PtfResponse modifier(@PathVariable Integer id, @Valid @RequestBody PtfRequest request) {
        return ptfService.modifier(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('Porteur_PROJET')")
    public void supprimer(@PathVariable Integer id) {
        ptfService.supprimer(id);
    }

    @PostMapping(value = "/{id}/protocole", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('Porteur_PROJET')")
    public PtfResponse archiverProtocole(@PathVariable Integer id, @RequestParam("fichier") MultipartFile fichier) {
        return ptfService.archiverProtocole(id, fichier);
    }

    @GetMapping("/{id}/protocole")
    @PreAuthorize("hasAnyRole('Porteur_PROJET', 'SUPERVISEUR_UEP', 'SUPERVISEUR_MPCE')")
    public ResponseEntity<Resource> telechargerProtocole(@PathVariable Integer id) {
        return ptfService.telechargerProtocole(id);
    }
}
