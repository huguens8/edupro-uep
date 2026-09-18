package ht.uep.edupro_uep.bilan;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ht.uep.edupro_uep.dto.BilanActiviteExecutionRequest;
import ht.uep.edupro_uep.dto.BilanActiviteLigneDto;
import ht.uep.edupro_uep.dto.BilanRequest;
import ht.uep.edupro_uep.dto.BilanResponse;
import ht.uep.edupro_uep.dto.BilanRubriqueExecutionRequest;
import ht.uep.edupro_uep.dto.BilanRubriqueLigneDto;
import ht.uep.edupro_uep.dto.BilanSourceLigneDto;
import ht.uep.edupro_uep.fiop.Projet;
import ht.uep.edupro_uep.fiop.ProjetCalendrierDepenseAnnuelle;
import ht.uep.edupro_uep.fiop.ProjetCalendrierDepenseAnnuelleRepository;
import ht.uep.edupro_uep.fiop.ProjetFinancement;
import ht.uep.edupro_uep.fiop.ProjetFinancementRepository;
import ht.uep.edupro_uep.fiop.ProjetRepository;
import ht.uep.edupro_uep.reference.ExerciceBudgetaireRepository;
import ht.uep.edupro_uep.reference.RubriqueBudgetaireRepository;
import ht.uep.edupro_uep.reference.SourceFinancementRepository;

/**
 * Bilan d'Exécution (DEUXIEME PARTIE DE LA FIOP : "Bilan Physique et Financier du Projet", §§ 35
 * et 39/41-43 du canevas FIOP). Le "prévu" (activités, budget par rubrique, sources de
 * financement) est TOUJOURS lu depuis le plan du projet (PREMIERE PARTIE de la FIOP, §§ 29/31/33/34,
 * géré par {@code ProjetService}) — jamais ressaisi ici, exactement comme dans le classeur Excel
 * d'origine (ex. {@code Bilan Suite 2!E15 = 'Info Générales'!L178}). Seule la partie exécution
 * (résultats obtenus, dépenses de l'exercice) est saisie dans ce module.
 */
@Service
public class BilanService {

    private final BilanExerciceRepository bilanExerciceRepository;
    private final BilanAvancementPhysiqueRepository avancementPhysiqueRepository;
    private final BilanAvancementFinancierRepository avancementFinancierRepository;
    private final BilanAvancementBudgetNationalRepository avancementBudgetRepository;
    private final BilanDepensePrevisionnelleSourceRepository depensePrevisionnelleRepository;
    private final ActiviteRepository activiteRepository;
    private final ActivitePlanificationAnnuelleRepository activitePlanificationAnnuelleRepository;
    private final ProjetRepository projetRepository;
    private final ProjetCalendrierDepenseAnnuelleRepository calendrierDepenseAnnuelleRepository;
    private final ProjetFinancementRepository projetFinancementRepository;
    private final ExerciceBudgetaireRepository exerciceBudgetaireRepository;
    private final RubriqueBudgetaireRepository rubriqueBudgetaireRepository;
    private final SourceFinancementRepository sourceFinancementRepository;

    public BilanService(
            BilanExerciceRepository bilanExerciceRepository,
            BilanAvancementPhysiqueRepository avancementPhysiqueRepository,
            BilanAvancementFinancierRepository avancementFinancierRepository,
            BilanAvancementBudgetNationalRepository avancementBudgetRepository,
            BilanDepensePrevisionnelleSourceRepository depensePrevisionnelleRepository,
            ActiviteRepository activiteRepository,
            ActivitePlanificationAnnuelleRepository activitePlanificationAnnuelleRepository,
            ProjetRepository projetRepository,
            ProjetCalendrierDepenseAnnuelleRepository calendrierDepenseAnnuelleRepository,
            ProjetFinancementRepository projetFinancementRepository,
            ExerciceBudgetaireRepository exerciceBudgetaireRepository,
            RubriqueBudgetaireRepository rubriqueBudgetaireRepository,
            SourceFinancementRepository sourceFinancementRepository) {
        this.bilanExerciceRepository = bilanExerciceRepository;
        this.avancementPhysiqueRepository = avancementPhysiqueRepository;
        this.avancementFinancierRepository = avancementFinancierRepository;
        this.avancementBudgetRepository = avancementBudgetRepository;
        this.depensePrevisionnelleRepository = depensePrevisionnelleRepository;
        this.activiteRepository = activiteRepository;
        this.activitePlanificationAnnuelleRepository = activitePlanificationAnnuelleRepository;
        this.projetRepository = projetRepository;
        this.calendrierDepenseAnnuelleRepository = calendrierDepenseAnnuelleRepository;
        this.projetFinancementRepository = projetFinancementRepository;
        this.exerciceBudgetaireRepository = exerciceBudgetaireRepository;
        this.rubriqueBudgetaireRepository = rubriqueBudgetaireRepository;
        this.sourceFinancementRepository = sourceFinancementRepository;
    }

    public List<BilanResponse> listerBilans(Integer projetId) {
        findProjetOrThrow(projetId);
        return bilanExerciceRepository.findByIdProjetOrderByIdExerciceDesc(projetId).stream()
                .map(this::toResponse)
                .toList();
    }

    public BilanResponse getBilan(Integer id) {
        return toResponse(findBilanOrThrow(id));
    }

    @Transactional
    public BilanResponse creerBilan(Integer projetId, BilanRequest request) {
        // Le Bilan d'Exécution n'est pas une étape distincte qui ne s'ouvrirait qu'une fois le
        // FIOP validé : c'est un onglet du même document que "Info Générales" (comme dans le
        // classeur FIOP d'origine), rempli et validé avec le reste en un seul cycle de lecture.
        // Aucune condition sur le statut du projet n'est donc imposée ici.
        Projet projet = findProjetOrThrow(projetId);
        if (bilanExerciceRepository.existsByIdProjetAndIdExercice(projetId, request.getIdExercice())) {
            throw new IllegalStateException("Un bilan existe déjà pour cet exercice sur ce projet.");
        }

        BilanExercice bilan = new BilanExercice();
        bilan.setIdProjet(projetId);
        applyHeader(bilan, request, projet);
        bilanExerciceRepository.save(bilan);

        appliquerLignes(bilan.getId(), projetId, request);
        bilanExerciceRepository.flush();
        return toResponse(bilan);
    }

    @Transactional
    public BilanResponse modifierBilan(Integer id, BilanRequest request) {
        BilanExercice bilan = findBilanOrThrow(id);
        Projet projet = findProjetOrThrow(bilan.getIdProjet());

        if (bilanExerciceRepository.existsByIdProjetAndIdExerciceAndIdNot(
                bilan.getIdProjet(), request.getIdExercice(), id)) {
            throw new IllegalStateException("Un bilan existe déjà pour cet exercice sur ce projet.");
        }

        avancementPhysiqueRepository.deleteByIdBilan(id);
        avancementFinancierRepository.deleteByIdBilan(id);
        avancementBudgetRepository.deleteByIdBilan(id);
        depensePrevisionnelleRepository.deleteByIdBilan(id);

        applyHeader(bilan, request, projet);
        bilanExerciceRepository.save(bilan);

        appliquerLignes(bilan.getId(), bilan.getIdProjet(), request);
        bilanExerciceRepository.flush();
        return toResponse(bilan);
    }

    @Transactional
    public void supprimerBilan(Integer id) {
        BilanExercice bilan = findBilanOrThrow(id);
        avancementPhysiqueRepository.deleteByIdBilan(id);
        avancementFinancierRepository.deleteByIdBilan(id);
        avancementBudgetRepository.deleteByIdBilan(id);
        depensePrevisionnelleRepository.deleteByIdBilan(id);
        bilanExerciceRepository.delete(bilan);
    }

    private void applyHeader(BilanExercice bilan, BilanRequest request, Projet projet) {
        exerciceBudgetaireRepository.findById(request.getIdExercice())
                .orElseThrow(() -> new NoSuchElementException("Exercice budgétaire introuvable."));

        bilan.setIdExercice(request.getIdExercice());
        bilan.setDateDemarrageEffective(request.getDateDemarrageEffective());

        Integer duree = request.getDureeTotaleProjetMois() != null
                ? request.getDureeTotaleProjetMois() : projet.getDureeTotaleMois();
        bilan.setDureeTotaleProjetMois(duree);

        if (request.getDateDemarrageEffective() != null && duree != null) {
            long ecoule = ChronoUnit.MONTHS.between(request.getDateDemarrageEffective(), LocalDate.now());
            int tempsEcoule = (int) Math.max(0, Math.min(ecoule, duree));
            bilan.setTempsEcouleMois(tempsEcoule);
            bilan.setTempsRestantMois(Math.max(0, duree - tempsEcoule));
        } else {
            bilan.setTempsEcouleMois(null);
            bilan.setTempsRestantMois(null);
        }
    }

    /**
     * Reconstruit les lignes d'avancement du bilan à partir du plan ACTUEL du projet (activités,
     * calendrier budgétaire par rubrique, sources de financement) : une ligne par élément du plan,
     * complétée par la saisie d'exécution transmise (le cas échéant) pour cet élément.
     */
    private void appliquerLignes(Integer idBilan, Integer idProjet, BilanRequest request) {
        Map<Integer, BilanActiviteExecutionRequest> executionParActivite = new HashMap<>();
        if (request.getActivites() != null) {
            for (BilanActiviteExecutionRequest exec : request.getActivites()) {
                if (exec != null && exec.getIdActivite() != null) {
                    executionParActivite.put(exec.getIdActivite(), exec);
                }
            }
        }

        for (Activite activite : activiteRepository.findByIdProjetOrderByOrdreSequentielAsc(idProjet)) {
            BilanActiviteExecutionRequest exec = executionParActivite.get(activite.getId());

            BigDecimal quantite = activite.getQuantiteResultatAttendu();
            BigDecimal obtenus = exec != null ? parseDecimal(exec.getResultatsObtenus()) : null;

            BilanAvancementPhysique physique = new BilanAvancementPhysique();
            physique.setIdBilan(idBilan);
            physique.setIdActivite(activite.getId());
            physique.setResultatsPrevus(formatResultatPrevu(quantite, activite.getUniteResultat()));
            physique.setResultatsObtenus(exec != null ? exec.getResultatsObtenus() : null);
            if (quantite != null && obtenus != null) {
                physique.setEcarts(quantite.subtract(obtenus).toPlainString());
                if (quantite.compareTo(BigDecimal.ZERO) > 0) {
                    physique.setPctAvancementPhysique(
                            obtenus.divide(quantite, 4, RoundingMode.HALF_UP)
                                    .multiply(BigDecimal.valueOf(100))
                                    .setScale(2, RoundingMode.HALF_UP));
                }
            }
            avancementPhysiqueRepository.save(physique);

            BigDecimal montantPrevu = montantPrevuActivite(activite);
            BigDecimal depensesAnt = exec != null ? nvl(exec.getDepensesAnterieuresN2()) : BigDecimal.ZERO;
            BigDecimal depensesEx = exec != null ? nvl(exec.getDepensesExercice()) : BigDecimal.ZERO;
            BigDecimal cumulees = depensesAnt.add(depensesEx);

            BilanAvancementFinancier financier = new BilanAvancementFinancier();
            financier.setIdBilan(idBilan);
            financier.setIdActivite(activite.getId());
            financier.setMontantPrevu(montantPrevu);
            financier.setDepensesAnterieuresN2(depensesAnt);
            financier.setDepensesExercice(depensesEx);
            financier.setDepensesCumulees(cumulees);
            financier.setBalancePrevisionnelle(nvl(montantPrevu).subtract(cumulees));
            avancementFinancierRepository.save(financier);
        }

        Map<Integer, BilanRubriqueExecutionRequest> executionParRubrique = new HashMap<>();
        if (request.getRubriques() != null) {
            for (BilanRubriqueExecutionRequest exec : request.getRubriques()) {
                if (exec != null && exec.getIdRubrique() != null) {
                    executionParRubrique.put(exec.getIdRubrique(), exec);
                }
            }
        }

        for (Map.Entry<Integer, BigDecimal> entry : montantPrevuParRubrique(idProjet).entrySet()) {
            Integer idRubrique = entry.getKey();
            BigDecimal montantPrevu = entry.getValue();
            BilanRubriqueExecutionRequest exec = executionParRubrique.get(idRubrique);
            BigDecimal depensesAnt = exec != null ? nvl(exec.getDepensesAnterieuresN2()) : BigDecimal.ZERO;
            BigDecimal depensesEx = exec != null ? nvl(exec.getDepensesExercice()) : BigDecimal.ZERO;
            BigDecimal cumulees = depensesAnt.add(depensesEx);

            BilanAvancementBudgetNational budget = new BilanAvancementBudgetNational();
            budget.setIdBilan(idBilan);
            budget.setIdRubrique(idRubrique);
            budget.setMontantPrevu(montantPrevu);
            budget.setDepensesAnterieuresN2(depensesAnt);
            budget.setDepensesExercice(depensesEx);
            budget.setDepensesCumulees(cumulees);
            budget.setBalancePrevisionnelle(montantPrevu.subtract(cumulees));
            avancementBudgetRepository.save(budget);
        }

        // Sources de financement : entièrement dérivées du plan de financement du projet (§ 29),
        // aucune saisie d'exécution attendue (voir le commentaire de classe).
        List<ProjetFinancement> financements = projetFinancementRepository.findByIdProjet(idProjet);
        BigDecimal totalPrevisions = financements.stream()
                .map(f -> nvl(f.getPrevisionTotal()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        for (ProjetFinancement financement : financements) {
            BilanDepensePrevisionnelleSource source = new BilanDepensePrevisionnelleSource();
            source.setIdBilan(idBilan);
            source.setIdSource(financement.getIdSource());
            BigDecimal previsionTotal = nvl(financement.getPrevisionTotal());
            source.setPrevisionTotal(previsionTotal);
            if (totalPrevisions.compareTo(BigDecimal.ZERO) > 0) {
                source.setPoidsPct(previsionTotal.divide(totalPrevisions, 4, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_UP));
            }
            depensePrevisionnelleRepository.save(source);
        }
    }

    /** Montant prévu d'une activité : somme de son coût réparti sur 5 ans (§31), à défaut son coût total (§33). */
    private BigDecimal montantPrevuActivite(Activite activite) {
        BigDecimal sommeAnnuelle = activitePlanificationAnnuelleRepository
                .findByIdActiviteOrderByAnneeNumeroAsc(activite.getId()).stream()
                .map(ActivitePlanificationAnnuelle::getCoutAnnee)
                .filter(java.util.Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        if (sommeAnnuelle.compareTo(BigDecimal.ZERO) > 0) {
            return sommeAnnuelle;
        }
        return nvl(activite.getCoutTotal());
    }

    /** Montant prévu par rubrique : somme du calendrier des dépenses prévisionnelles (§34) sur 5 ans. */
    private Map<Integer, BigDecimal> montantPrevuParRubrique(Integer idProjet) {
        Map<Integer, BigDecimal> totaux = new java.util.LinkedHashMap<>();
        for (ProjetCalendrierDepenseAnnuelle ligne : calendrierDepenseAnnuelleRepository.findByIdProjet(idProjet)) {
            totaux.merge(ligne.getIdRubrique(), nvl(ligne.getMontantPrevisionnel()), BigDecimal::add);
        }
        return totaux;
    }

    private BilanResponse toResponse(BilanExercice bilan) {
        BilanResponse r = new BilanResponse();
        r.setId(bilan.getId());
        r.setIdProjet(bilan.getIdProjet());
        projetRepository.findById(bilan.getIdProjet()).ifPresent(p -> r.setProjetTitre(p.getTitre()));
        r.setIdExercice(bilan.getIdExercice());
        exerciceBudgetaireRepository.findById(bilan.getIdExercice())
                .ifPresent(e -> r.setExerciceLibelle(e.getLibelle()));
        r.setDateDemarrageEffective(bilan.getDateDemarrageEffective());
        r.setDureeTotaleProjetMois(bilan.getDureeTotaleProjetMois());
        r.setTempsEcouleMois(bilan.getTempsEcouleMois());
        r.setTempsRestantMois(bilan.getTempsRestantMois());
        if (bilan.getDateDemarrageEffective() != null && bilan.getDureeTotaleProjetMois() != null) {
            r.setDateAchevementPrevue(bilan.getDateDemarrageEffective().plusMonths(bilan.getDureeTotaleProjetMois()));
        }
        r.setDateCreation(bilan.getDateCreation());

        List<Activite> activites = activiteRepository.findByIdProjetOrderByOrdreSequentielAsc(bilan.getIdProjet());
        List<BilanAvancementPhysique> physiques = avancementPhysiqueRepository.findByIdBilan(bilan.getId());
        List<BilanAvancementFinancier> financiers = avancementFinancierRepository.findByIdBilan(bilan.getId());

        r.setActivites(physiques.stream()
                .sorted(Comparator.comparing(p -> activiteOrdre(activites, p.getIdActivite())))
                .map(p -> toActiviteLigne(p, findFinancier(financiers, p.getIdActivite()), findActivite(activites, p.getIdActivite())))
                .toList());

        r.setRubriques(avancementBudgetRepository.findByIdBilan(bilan.getId()).stream()
                .map(this::toRubriqueLigne)
                .toList());

        r.setSources(depensePrevisionnelleRepository.findByIdBilan(bilan.getId()).stream()
                .map(this::toSourceLigne)
                .toList());

        return r;
    }

    private BilanActiviteLigneDto toActiviteLigne(BilanAvancementPhysique physique, BilanAvancementFinancier financier, Activite activite) {
        BilanActiviteLigneDto dto = new BilanActiviteLigneDto();
        dto.setIdActivite(physique.getIdActivite());
        if (activite != null) {
            dto.setLibelle(activite.getLibelle());
            dto.setUniteResultat(activite.getUniteResultat());
            dto.setQuantiteResultatAttendu(activite.getQuantiteResultatAttendu());
        }
        dto.setResultatsPrevus(physique.getResultatsPrevus());
        dto.setResultatsObtenus(physique.getResultatsObtenus());
        dto.setEcarts(physique.getEcarts());
        dto.setPctAvancementPhysique(physique.getPctAvancementPhysique());
        if (financier != null) {
            dto.setMontantPrevu(financier.getMontantPrevu());
            dto.setDepensesAnterieuresN2(financier.getDepensesAnterieuresN2());
            dto.setDepensesExercice(financier.getDepensesExercice());
            dto.setDepensesCumulees(financier.getDepensesCumulees());
            dto.setBalancePrevisionnelle(financier.getBalancePrevisionnelle());
        }
        return dto;
    }

    private BilanRubriqueLigneDto toRubriqueLigne(BilanAvancementBudgetNational budget) {
        BilanRubriqueLigneDto dto = new BilanRubriqueLigneDto();
        dto.setIdRubrique(budget.getIdRubrique());
        rubriqueBudgetaireRepository.findById(budget.getIdRubrique()).ifPresent(r -> dto.setRubriqueLibelle(r.getLibelle()));
        dto.setMontantPrevu(budget.getMontantPrevu());
        dto.setDepensesAnterieuresN2(budget.getDepensesAnterieuresN2());
        dto.setDepensesExercice(budget.getDepensesExercice());
        dto.setDepensesCumulees(budget.getDepensesCumulees());
        dto.setBalancePrevisionnelle(budget.getBalancePrevisionnelle());
        return dto;
    }

    private BilanSourceLigneDto toSourceLigne(BilanDepensePrevisionnelleSource source) {
        BilanSourceLigneDto dto = new BilanSourceLigneDto();
        dto.setIdSource(source.getIdSource());
        sourceFinancementRepository.findById(source.getIdSource()).ifPresent(s -> dto.setSourceLibelle(s.getLibelle()));
        dto.setPrevisionTotal(source.getPrevisionTotal());
        dto.setPoidsPct(source.getPoidsPct());
        return dto;
    }

    private Activite findActivite(List<Activite> activites, Integer id) {
        return activites.stream().filter(a -> a.getId().equals(id)).findFirst().orElse(null);
    }

    private int activiteOrdre(List<Activite> activites, Integer idActivite) {
        Activite a = findActivite(activites, idActivite);
        return a != null && a.getOrdreSequentiel() != null ? a.getOrdreSequentiel() : Integer.MAX_VALUE;
    }

    private BilanAvancementFinancier findFinancier(List<BilanAvancementFinancier> list, Integer idActivite) {
        return list.stream().filter(f -> f.getIdActivite().equals(idActivite)).findFirst().orElse(null);
    }

    private String formatResultatPrevu(BigDecimal quantite, String unite) {
        if (quantite == null && (unite == null || unite.isBlank())) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        if (quantite != null) {
            sb.append(quantite.stripTrailingZeros().toPlainString());
        }
        if (unite != null && !unite.isBlank()) {
            if (sb.length() > 0) {
                sb.append(' ');
            }
            sb.append(unite);
        }
        return sb.toString();
    }

    private BigDecimal parseDecimal(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return new BigDecimal(value.trim().replace(',', '.'));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private BigDecimal nvl(BigDecimal value) {
        return value != null ? value : BigDecimal.ZERO;
    }

    private Projet findProjetOrThrow(Integer id) {
        return projetRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("FIOP introuvable."));
    }

    private BilanExercice findBilanOrThrow(Integer id) {
        return bilanExerciceRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Bilan d'exécution introuvable."));
    }
}
