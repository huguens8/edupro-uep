package ht.uep.edupro_uep.bilan;

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

import ht.uep.edupro_uep.dto.BilanRequest;
import ht.uep.edupro_uep.dto.BilanResponse;
import jakarta.validation.Valid;

@RestController
public class BilanController {

    private final BilanService bilanService;

    public BilanController(BilanService bilanService) {
        this.bilanService = bilanService;
    }

    @GetMapping("/api/fiop/{projetId}/bilans")
    @PreAuthorize("hasAnyRole('OPERATEUR_SAISIE', 'SUPERVISEUR_UEP', 'SUPERVISEUR_MPCE')")
    public List<BilanResponse> lister(@PathVariable Integer projetId) {
        return bilanService.listerBilans(projetId);
    }

    @GetMapping("/api/bilans/{id}")
    @PreAuthorize("hasAnyRole('OPERATEUR_SAISIE', 'SUPERVISEUR_UEP', 'SUPERVISEUR_MPCE')")
    public BilanResponse get(@PathVariable Integer id) {
        return bilanService.getBilan(id);
    }

    @PostMapping("/api/fiop/{projetId}/bilans")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('OPERATEUR_SAISIE', 'SUPERVISEUR_UEP')")
    public BilanResponse creer(@PathVariable Integer projetId, @Valid @RequestBody BilanRequest request) {
        return bilanService.creerBilan(projetId, request);
    }

    @PutMapping("/api/bilans/{id}")
    @PreAuthorize("hasAnyRole('OPERATEUR_SAISIE', 'SUPERVISEUR_UEP')")
    public BilanResponse modifier(@PathVariable Integer id, @Valid @RequestBody BilanRequest request) {
        return bilanService.modifierBilan(id, request);
    }

    @DeleteMapping("/api/bilans/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('OPERATEUR_SAISIE', 'SUPERVISEUR_UEP')")
    public void supprimer(@PathVariable Integer id) {
        bilanService.supprimerBilan(id);
    }
}
