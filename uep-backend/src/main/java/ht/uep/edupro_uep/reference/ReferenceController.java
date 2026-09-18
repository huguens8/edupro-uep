package ht.uep.edupro_uep.reference;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ht.uep.edupro_uep.dto.ReferenceOptionResponse;
import ht.uep.edupro_uep.dto.RubriqueBudgetaireDetailResponse;
import ht.uep.edupro_uep.dto.SourceFinancementOptionResponse;

/**
 * Référentiels de classification du FIOP (Grand Chantier / Programme /
 * Sous-Programme, Exercice budgétaire, Rubrique budgétaire, Source de
 * financement). Lecture seule.
 */
@RestController
@RequestMapping("/api/reference")
public class ReferenceController {

    private final GrandChantierRepository grandChantierRepository;
    private final ProgrammeRepository programmeRepository;
    private final SousProgrammeRepository sousProgrammeRepository;
    private final ExerciceBudgetaireRepository exerciceBudgetaireRepository;
    private final RubriqueBudgetaireRepository rubriqueBudgetaireRepository;
    private final SourceFinancementRepository sourceFinancementRepository;

    public ReferenceController(
            GrandChantierRepository grandChantierRepository,
            ProgrammeRepository programmeRepository,
            SousProgrammeRepository sousProgrammeRepository,
            ExerciceBudgetaireRepository exerciceBudgetaireRepository,
            RubriqueBudgetaireRepository rubriqueBudgetaireRepository,
            SourceFinancementRepository sourceFinancementRepository) {
        this.grandChantierRepository = grandChantierRepository;
        this.programmeRepository = programmeRepository;
        this.sousProgrammeRepository = sousProgrammeRepository;
        this.exerciceBudgetaireRepository = exerciceBudgetaireRepository;
        this.rubriqueBudgetaireRepository = rubriqueBudgetaireRepository;
        this.sourceFinancementRepository = sourceFinancementRepository;
    }

    @GetMapping("/grand-chantiers")
    public List<ReferenceOptionResponse> grandChantiers() {
        return grandChantierRepository.findAllByOrderByLibelleAsc().stream()
                .map(g -> new ReferenceOptionResponse(g.getId(), g.getLibelle()))
                .toList();
    }

    @GetMapping("/programmes")
    public List<ReferenceOptionResponse> programmes(@RequestParam Integer grandChantierId) {
        return programmeRepository.findByIdGrandChantierOrderByLibelleAsc(grandChantierId).stream()
                .map(p -> new ReferenceOptionResponse(p.getId(), p.getLibelle()))
                .toList();
    }

    @GetMapping("/sous-programmes")
    public List<ReferenceOptionResponse> sousProgrammes(@RequestParam Integer programmeId) {
        return sousProgrammeRepository.findByIdProgrammeOrderByLibelleAsc(programmeId).stream()
                .map(s -> new ReferenceOptionResponse(s.getId(), s.getLibelle()))
                .toList();
    }

    @GetMapping("/exercices")
    public List<ReferenceOptionResponse> exercices() {
        return exerciceBudgetaireRepository.findAllByOrderByLibelleDesc().stream()
                .map(e -> new ReferenceOptionResponse(e.getId(), e.getLibelle()))
                .toList();
    }

    /** Libellé préfixé du libellé parent pour les sous-rubriques (niveau 2), afin de rester lisible en liste plate. */
    @GetMapping("/rubriques-budgetaires")
    public List<ReferenceOptionResponse> rubriquesBudgetaires() {
        List<RubriqueBudgetaire> toutes = rubriqueBudgetaireRepository.findAllByOrderByCodeAsc();
        Map<Integer, String> libellesParId = toutes.stream()
                .collect(Collectors.toMap(RubriqueBudgetaire::getId, RubriqueBudgetaire::getLibelle));
        return toutes.stream()
                .map(r -> new ReferenceOptionResponse(r.getId(), r.getNiveau() > 1 && r.getIdRubriqueParent() != null
                        ? libellesParId.getOrDefault(r.getIdRubriqueParent(), "") + " > " + r.getLibelle()
                        : r.getLibelle()))
                .toList();
    }

    /**
     * Rubriques de niveau 1 uniquement (les 6 grandes masses budgétaires), sans les sous-rubriques
     * — utilisé pour les tableaux à lignes fixes du canevas FIOP (§34, §35).
     */
    @GetMapping("/rubriques-budgetaires-principales")
    public List<ReferenceOptionResponse> rubriquesBudgetairesPrincipales() {
        return rubriqueBudgetaireRepository.findByNiveauOrderByCodeAsc((short) 1).stream()
                .map(r -> new ReferenceOptionResponse(r.getId(), r.getLibelle()))
                .toList();
    }

    /**
     * Rubriques et sous-rubriques avec leur hiérarchie complète — utilisé par le tableau §34 du
     * canevas FIOP pour regrouper les sous-rubriques sous leur rubrique principale et calculer les
     * sous-totaux, contrairement à /rubriques-budgetaires (libellés aplatis) et
     * /rubriques-budgetaires-principales (niveau 1 seulement).
     */
    @GetMapping("/rubriques-budgetaires-hierarchie")
    public List<RubriqueBudgetaireDetailResponse> rubriquesBudgetairesHierarchie() {
        return rubriqueBudgetaireRepository.findAllByOrderByCodeAsc().stream()
                .map(r -> new RubriqueBudgetaireDetailResponse(
                        r.getId(), r.getCode(), r.getLibelle(), r.getNiveau(), r.getIdRubriqueParent()))
                .toList();
    }

    @GetMapping("/sources-financement")
    public List<SourceFinancementOptionResponse> sourcesFinancement() {
        return sourceFinancementRepository.findAllByOrderByCategorieAscLibelleAsc().stream()
                .map(s -> new SourceFinancementOptionResponse(s.getId(), s.getLibelle(), s.getCategorie()))
                .toList();
    }
}
