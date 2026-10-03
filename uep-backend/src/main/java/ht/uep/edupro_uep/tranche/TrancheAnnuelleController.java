package ht.uep.edupro_uep.tranche;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ht.uep.edupro_uep.dto.TrancheAnnuelleRequest;
import ht.uep.edupro_uep.dto.TrancheAnnuelleResponse;
import jakarta.validation.Valid;

/** Mêmes rôles que le Bilan d'Exécution : saisie par le porteur et l'UEP, lecture aussi par le MPCE. */
@RestController
@RequestMapping("/api/fiop/{projetId}/tranches")
public class TrancheAnnuelleController {

    private final TrancheAnnuelleService trancheService;

    public TrancheAnnuelleController(TrancheAnnuelleService trancheService) {
        this.trancheService = trancheService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('Porteur_PROJET', 'SUPERVISEUR_UEP', 'SUPERVISEUR_MPCE')")
    public List<TrancheAnnuelleResponse> lister(@PathVariable Integer projetId) {
        return trancheService.listerTranches(projetId);
    }

    @GetMapping("/{idExercice}")
    @PreAuthorize("hasAnyRole('Porteur_PROJET', 'SUPERVISEUR_UEP', 'SUPERVISEUR_MPCE')")
    public TrancheAnnuelleResponse get(@PathVariable Integer projetId, @PathVariable Integer idExercice) {
        return trancheService.getTranche(projetId, idExercice);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('Porteur_PROJET', 'SUPERVISEUR_UEP')")
    public TrancheAnnuelleResponse creer(@PathVariable Integer projetId, @Valid @RequestBody TrancheAnnuelleRequest request) {
        return trancheService.creerTranche(projetId, request);
    }

    @PutMapping("/{idExercice}")
    @PreAuthorize("hasAnyRole('Porteur_PROJET', 'SUPERVISEUR_UEP')")
    public TrancheAnnuelleResponse modifier(@PathVariable Integer projetId, @PathVariable Integer idExercice,
            @Valid @RequestBody TrancheAnnuelleRequest request) {
        return trancheService.modifierTranche(projetId, idExercice, request);
    }

    @DeleteMapping("/{idExercice}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('Porteur_PROJET', 'SUPERVISEUR_UEP')")
    public void supprimer(@PathVariable Integer projetId, @PathVariable Integer idExercice) {
        trancheService.supprimerTranche(projetId, idExercice);
    }
}
