package ht.uep.edupro_uep.fiop;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ht.uep.edupro_uep.dto.AssignCodePipRequest;
import ht.uep.edupro_uep.dto.FiopRequest;
import ht.uep.edupro_uep.dto.FiopResponse;
import ht.uep.edupro_uep.dto.RejectRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/fiop")
public class ProjetController {

    private final ProjetService projetService;

    public ProjetController(ProjetService projetService) {
        this.projetService = projetService;
    }

    @GetMapping
    public List<FiopResponse> list() {
        return projetService.listFiops();
    }

    @GetMapping("/{id}")
    public FiopResponse get(@PathVariable Integer id) {
        return projetService.getFiop(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('OPERATEUR_SAISIE')")
    public FiopResponse creer(@Valid @RequestBody FiopRequest request, Authentication authentication) {
        return projetService.creerFiop(request, authentication.getName());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('OPERATEUR_SAISIE', 'SUPERVISEUR_UEP')")
    public FiopResponse modifier(@PathVariable Integer id, @Valid @RequestBody FiopRequest request,
            Authentication authentication) {
        return projetService.modifierFiop(id, request, authentication.getName());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('OPERATEUR_SAISIE')")
    public void supprimer(@PathVariable Integer id, Authentication authentication) {
        projetService.supprimerFiop(id, authentication.getName());
    }

    @PostMapping("/{id}/soumettre")
    @PreAuthorize("hasRole('OPERATEUR_SAISIE')")
    public FiopResponse soumettre(@PathVariable Integer id, Authentication authentication) {
        return projetService.soumettreFiop(id, authentication.getName());
    }

    @PostMapping("/{id}/valider")
    @PreAuthorize("hasRole('SUPERVISEUR_UEP')")
    public FiopResponse valider(@PathVariable Integer id, Authentication authentication) {
        return projetService.validerFiop(id, authentication.getName());
    }

    @PostMapping("/{id}/approuver")
    @PreAuthorize("hasRole('SUPERVISEUR_MPCE')")
    public FiopResponse approuver(@PathVariable Integer id, Authentication authentication) {
        return projetService.approuverFiop(id, authentication.getName());
    }

    @PostMapping("/{id}/rejeter")
    @PreAuthorize("hasAnyRole('SUPERVISEUR_UEP', 'SUPERVISEUR_MPCE')")
    public FiopResponse rejeter(@PathVariable Integer id, @Valid @RequestBody RejectRequest request,
            Authentication authentication) {
        return projetService.rejeterFiop(id, request.getMotif(), authentication.getName());
    }

    @PutMapping("/{id}/corriger")
    @PreAuthorize("hasAnyRole('OPERATEUR_SAISIE', 'SUPERVISEUR_UEP')")
    public FiopResponse corriger(@PathVariable Integer id, @Valid @RequestBody FiopRequest request,
            Authentication authentication) {
        return projetService.corrigerFiop(id, request, authentication.getName());
    }

    @PatchMapping("/{id}/code-pip")
    @PreAuthorize("hasRole('SUPERVISEUR_MPCE')")
    public FiopResponse assignerCodePip(@PathVariable Integer id, @Valid @RequestBody AssignCodePipRequest request,
            Authentication authentication) {
        return projetService.assignerCodePip(id, request.getCodeInternePip(), authentication.getName());
    }
}
