package ht.uep.edupro_uep.dashboard;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ht.uep.edupro_uep.dto.MontantProjetResponse;
import ht.uep.edupro_uep.dto.ProjetsParDepartementResponse;
import ht.uep.edupro_uep.fiop.ProjetRepository;
import ht.uep.edupro_uep.fiop.StatutFiop;

/**
 * Indicateurs agrégés du tableau de bord. Lecture seule, ouvert à tout
 * utilisateur authentifié (aucune donnée nominative n'est exposée).
 */
@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final ProjetRepository projetRepository;

    public DashboardController(ProjetRepository projetRepository) {
        this.projetRepository = projetRepository;
    }

    /** Nombre de projets par département et par statut ; le filtrage par statut se fait côté client. */
    @GetMapping("/projets-par-departement")
    public List<ProjetsParDepartementResponse> projetsParDepartement() {
        return projetRepository.countProjetsParDepartementEtStatut().stream()
                .map(row -> new ProjetsParDepartementResponse(
                        (String) row[0],
                        row[1] != null ? (String) row[1] : "Non renseigné",
                        StatutFiop.fromDbValue((String) row[2]).name(),
                        ((Number) row[3]).longValue()))
                .toList();
    }

    /** Coût total de chaque projet (tous statuts) ; le filtrage par statut se fait côté client. */
    @GetMapping("/montants-projets")
    public List<MontantProjetResponse> montantsProjets() {
        return projetRepository.findAll().stream()
                .map(p -> new MontantProjetResponse(p.getId(), p.getTitre(), p.getStatut().name(), p.getCoutTotalGourde()))
                .toList();
    }
}
