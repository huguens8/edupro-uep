package ht.uep.edupro_uep.tranche;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ht.uep.edupro_uep.bilan.Activite;
import ht.uep.edupro_uep.bilan.ActiviteRepository;
import ht.uep.edupro_uep.dto.TrancheAnnuelleRequest;
import ht.uep.edupro_uep.dto.TrancheAnnuelleResponse;
import ht.uep.edupro_uep.dto.TrancheCalendrierLigneDto;
import ht.uep.edupro_uep.dto.TrancheChronogrammeLigneDto;
import ht.uep.edupro_uep.dto.TrancheSourceLigneDto;
import ht.uep.edupro_uep.dto.TrancheSourceRequest;
import ht.uep.edupro_uep.dto.TrancheVoieMoyenLigneDto;
import ht.uep.edupro_uep.fiop.ProjetRepository;
import ht.uep.edupro_uep.reference.ExerciceBudgetaireRepository;
import ht.uep.edupro_uep.reference.RubriqueBudgetaire;
import ht.uep.edupro_uep.reference.RubriqueBudgetaireRepository;
import ht.uep.edupro_uep.reference.SourceFinancement;
import ht.uep.edupro_uep.reference.SourceFinancementRepository;

/**
 * Tranche Annuelle du projet (TROISIEME PARTIE DE LA FIOP, onglet "Annuelle" du canevas, § 44 :
 * "Dépenses prévisionnelles (Exercice Actuel)"). Une tranche par exercice ; comme le Bilan
 * d'Exécution, c'est un onglet du même document que "Info Générales", sans condition de statut.
 *
 * Contrairement au Bilan (§ 39), la prévision par source est SAISIE ici : sur la feuille
 * "Annuelle" du canevas, seule la ligne Trésor public renvoie à une autre feuille (le total
 * Année 1 du § 34, 'Info Générales'!G282) et les six autres sont des saisies — ce lien isolé ne
 * correspond à aucune source en particulier, il n'est donc pas repris.
 *
 * Le § 45 (feuille "Activités") n'a rien de propre à la tranche : il ne fait que reprendre le § 33
 * d'Info Générales, affiché tel quel côté client. Le § 46 (feuille "Chronogramme") enregistre
 * les mois où chaque activité du plan est planifiée pour l'exercice. Le § 47 (feuille
 * "R.dépenses") est une liste libre de codes et articles budgétaires par sous-rubrique, avec leur
 * répartition par source — entièrement saisie, aucune cellule n'y renvoie ailleurs. Les §§ 48-49
 * (feuille "Calendrier") répartissent par trimestre les ressources nationales puis externes de
 * chaque sous-rubrique ; le § 50 (nationales et externes), saisi à part dans le canevas, n'en est
 * que la somme et est calculé côté client.
 */
@Service
public class TrancheAnnuelleService {

    /** Colonnes 1 à 12 du § 46 : l'exercice fiscal haïtien va d'octobre à septembre. */
    private static final List<String> MOIS_EXERCICE = List.of(
            "OCT", "NOV", "DEC", "JANV", "FEV", "MARS", "AVRIL", "MAI", "JUIN", "JUILLET", "AOUT", "SEPT");

    private final ProjetTrancheDepenseSourceRepository trancheRepository;
    private final ActiviteChronogrammeMensuelRepository chronogrammeRepository;
    private final ProjetCalendrierDepenseTrimestrielleRepository calendrierTrimestrielRepository;
    private final ProjetTrancheVoieMoyenRepository voieMoyenRepository;
    private final ActiviteRepository activiteRepository;
    private final RubriqueBudgetaireRepository rubriqueBudgetaireRepository;
    private final ProjetRepository projetRepository;
    private final ExerciceBudgetaireRepository exerciceBudgetaireRepository;
    private final SourceFinancementRepository sourceFinancementRepository;

    public TrancheAnnuelleService(
            ProjetTrancheDepenseSourceRepository trancheRepository,
            ActiviteChronogrammeMensuelRepository chronogrammeRepository,
            ProjetCalendrierDepenseTrimestrielleRepository calendrierTrimestrielRepository,
            ProjetTrancheVoieMoyenRepository voieMoyenRepository,
            ActiviteRepository activiteRepository,
            RubriqueBudgetaireRepository rubriqueBudgetaireRepository,
            ProjetRepository projetRepository,
            ExerciceBudgetaireRepository exerciceBudgetaireRepository,
            SourceFinancementRepository sourceFinancementRepository) {
        this.trancheRepository = trancheRepository;
        this.chronogrammeRepository = chronogrammeRepository;
        this.calendrierTrimestrielRepository = calendrierTrimestrielRepository;
        this.voieMoyenRepository = voieMoyenRepository;
        this.activiteRepository = activiteRepository;
        this.rubriqueBudgetaireRepository = rubriqueBudgetaireRepository;
        this.projetRepository = projetRepository;
        this.exerciceBudgetaireRepository = exerciceBudgetaireRepository;
        this.sourceFinancementRepository = sourceFinancementRepository;
    }

    public List<TrancheAnnuelleResponse> listerTranches(Integer projetId) {
        findProjetOrThrow(projetId);
        TreeSet<Integer> exercices = new TreeSet<>(Comparator.reverseOrder());
        trancheRepository.findByIdProjet(projetId).forEach(l -> exercices.add(l.getIdExercice()));
        return exercices.stream().map(idExercice -> toResponse(projetId, idExercice)).toList();
    }

    public TrancheAnnuelleResponse getTranche(Integer projetId, Integer idExercice) {
        findProjetOrThrow(projetId);
        if (!trancheRepository.existsByIdProjetAndIdExercice(projetId, idExercice)) {
            throw new NoSuchElementException("Tranche annuelle introuvable pour cet exercice.");
        }
        return toResponse(projetId, idExercice);
    }

    /** Comme {@link #getTranche}, mais vide (au lieu d'une erreur 404) quand l'exercice n'a pas de tranche. */
    public java.util.Optional<TrancheAnnuelleResponse> trouverTranche(Integer projetId, Integer idExercice) {
        if (!trancheRepository.existsByIdProjetAndIdExercice(projetId, idExercice)) {
            return java.util.Optional.empty();
        }
        return java.util.Optional.of(toResponse(projetId, idExercice));
    }

    @Transactional
    public TrancheAnnuelleResponse creerTranche(Integer projetId, TrancheAnnuelleRequest request) {
        findProjetOrThrow(projetId);
        Integer idExercice = request.getIdExercice();
        exerciceBudgetaireRepository.findById(idExercice)
                .orElseThrow(() -> new NoSuchElementException("Exercice budgétaire introuvable."));
        if (trancheRepository.existsByIdProjetAndIdExercice(projetId, idExercice)) {
            throw new IllegalStateException("Une tranche annuelle existe déjà pour cet exercice sur ce projet.");
        }
        // Un chronogramme orphelin (tranche supprimée hors application) ne doit pas bloquer la création.
        chronogrammeRepository.deleteByIdActiviteInAndIdExercice(idActivitesDuProjet(projetId), idExercice);
        voieMoyenRepository.deleteByIdProjetAndIdExercice(projetId, idExercice);
        calendrierTrimestrielRepository.deleteByIdProjetAndIdExercice(projetId, idExercice);
        calendrierTrimestrielRepository.flush();
        chronogrammeRepository.flush();
        voieMoyenRepository.flush();
        appliquerSources(projetId, idExercice, request.getSources());
        appliquerChronogramme(projetId, idExercice, request.getChronogramme());
        appliquerVoiesMoyens(projetId, idExercice, request.getVoiesMoyens());
        appliquerCalendrier(projetId, idExercice, ProjetCalendrierDepenseTrimestrielle.NATIONAL, request.getCalendrierNational());
        appliquerCalendrier(projetId, idExercice, ProjetCalendrierDepenseTrimestrielle.EXTERNE, request.getCalendrierExterne());
        return toResponse(projetId, idExercice);
    }

    @Transactional
    public TrancheAnnuelleResponse modifierTranche(Integer projetId, Integer idExercice, TrancheAnnuelleRequest request) {
        getTranche(projetId, idExercice);
        trancheRepository.deleteByIdProjetAndIdExercice(projetId, idExercice);
        chronogrammeRepository.deleteByIdActiviteInAndIdExercice(idActivitesDuProjet(projetId), idExercice);
        // flush() immédiat : sinon Hibernate exécute les INSERT avant les DELETE et viole les
        // contraintes uniques (projet, exercice, source) / (activité, exercice, mois) — voir
        // BilanService.modifierBilan.
        voieMoyenRepository.deleteByIdProjetAndIdExercice(projetId, idExercice);
        calendrierTrimestrielRepository.deleteByIdProjetAndIdExercice(projetId, idExercice);
        calendrierTrimestrielRepository.flush();
        trancheRepository.flush();
        chronogrammeRepository.flush();
        voieMoyenRepository.flush();
        appliquerSources(projetId, idExercice, request.getSources());
        appliquerChronogramme(projetId, idExercice, request.getChronogramme());
        appliquerVoiesMoyens(projetId, idExercice, request.getVoiesMoyens());
        appliquerCalendrier(projetId, idExercice, ProjetCalendrierDepenseTrimestrielle.NATIONAL, request.getCalendrierNational());
        appliquerCalendrier(projetId, idExercice, ProjetCalendrierDepenseTrimestrielle.EXTERNE, request.getCalendrierExterne());
        return toResponse(projetId, idExercice);
    }

    @Transactional
    public void supprimerTranche(Integer projetId, Integer idExercice) {
        getTranche(projetId, idExercice);
        trancheRepository.deleteByIdProjetAndIdExercice(projetId, idExercice);
        chronogrammeRepository.deleteByIdActiviteInAndIdExercice(idActivitesDuProjet(projetId), idExercice);
        voieMoyenRepository.deleteByIdProjetAndIdExercice(projetId, idExercice);
        calendrierTrimestrielRepository.deleteByIdProjetAndIdExercice(projetId, idExercice);
        calendrierTrimestrielRepository.flush();
    }

    /** Une ligne par source de référence (les 7 du canevas), à 0 si rien n'est saisi pour elle. */
    private void appliquerSources(Integer projetId, Integer idExercice, List<TrancheSourceRequest> saisies) {
        Map<Integer, BigDecimal> previsionParSource = new HashMap<>();
        if (saisies != null) {
            for (TrancheSourceRequest saisie : saisies) {
                if (saisie != null && saisie.getIdSource() != null) {
                    previsionParSource.put(saisie.getIdSource(), nvl(saisie.getPrevisionTotal()));
                }
            }
        }
        for (SourceFinancement reference : sourceFinancementRepository.findAll()) {
            ProjetTrancheDepenseSource ligne = new ProjetTrancheDepenseSource();
            ligne.setIdProjet(projetId);
            ligne.setIdExercice(idExercice);
            ligne.setIdSource(reference.getId());
            ligne.setPrevisionTotal(previsionParSource.getOrDefault(reference.getId(), BigDecimal.ZERO));
            trancheRepository.save(ligne);
        }
        trancheRepository.flush();
    }

    /**
     * Une ligne par mois planifié. Les activités qui n'appartiennent pas au projet et les rangs
     * hors 1-12 sont ignorés plutôt que refusés, comme les saisies vides du reste du formulaire.
     */
    private void appliquerChronogramme(Integer projetId, Integer idExercice, List<TrancheChronogrammeLigneDto> lignes) {
        if (lignes == null) {
            return;
        }
        Set<Integer> activitesDuProjet = Set.copyOf(idActivitesDuProjet(projetId));
        for (TrancheChronogrammeLigneDto ligne : lignes) {
            if (ligne == null || !activitesDuProjet.contains(ligne.getIdActivite()) || ligne.getMois() == null) {
                continue;
            }
            TreeSet<Integer> rangs = new TreeSet<>();
            ligne.getMois().stream().filter(r -> r != null && r >= 1 && r <= 12).forEach(rangs::add);
            for (Integer rang : rangs) {
                ActiviteChronogrammeMensuel mois = new ActiviteChronogrammeMensuel();
                mois.setIdActivite(ligne.getIdActivite());
                mois.setIdExercice(idExercice);
                mois.setTrimestre((short) ((rang - 1) / 3 + 1));
                mois.setMois(MOIS_EXERCICE.get(rang - 1));
                mois.setPlanifie(true);
                chronogrammeRepository.save(mois);
            }
        }
        chronogrammeRepository.flush();
    }

    /**
     * Lignes du § 47 dans l'ordre de saisie. Une ligne entièrement vide (ni article, ni montant)
     * est ignorée ; une sous-rubrique doit être de niveau 2 (ex. "11-Rémunérations principales"),
     * les rubriques principales ne servant qu'aux regroupements et sous-totaux.
     */
    private void appliquerVoiesMoyens(Integer projetId, Integer idExercice, List<TrancheVoieMoyenLigneDto> lignes) {
        if (lignes == null) {
            return;
        }
        short ordre = 0;
        for (TrancheVoieMoyenLigneDto ligne : lignes) {
            if (ligne == null || ligneVide(ligne)) {
                continue;
            }
            RubriqueBudgetaire rubrique = rubriqueBudgetaireRepository.findById(ligne.getIdRubrique())
                    .filter(r -> r.getIdRubriqueParent() != null)
                    .orElseThrow(() -> new NoSuchElementException("Sous-rubrique budgétaire introuvable."));
            ProjetTrancheVoieMoyen voie = new ProjetTrancheVoieMoyen();
            voie.setIdProjet(projetId);
            voie.setIdExercice(idExercice);
            voie.setIdRubrique(rubrique.getId());
            voie.setOrdre(ordre++);
            voie.setCodeArticle(blankToNull(ligne.getCodeArticle()));
            voie.setDesignation(blankToNull(ligne.getDesignation()));
            voie.setOrdreActivites(blankToNull(ligne.getOrdreActivites()));
            voie.setUniteMesure(blankToNull(ligne.getUniteMesure()));
            voie.setQuantite(ligne.getQuantite());
            voie.setCoutUnitaire(ligne.getCoutUnitaire());
            voie.setMontantTresorPublic(ligne.getMontantTresorPublic());
            voie.setMontantAfc(ligne.getMontantAfc());
            voie.setMontantFondsPropres(ligne.getMontantFondsPropres());
            voie.setMontantBilateral(ligne.getMontantBilateral());
            voie.setMontantMultilateral(ligne.getMontantMultilateral());
            voieMoyenRepository.save(voie);
        }
        voieMoyenRepository.flush();
    }

    private boolean ligneVide(TrancheVoieMoyenLigneDto l) {
        return blankToNull(l.getCodeArticle()) == null && blankToNull(l.getDesignation()) == null
                && blankToNull(l.getOrdreActivites()) == null && blankToNull(l.getUniteMesure()) == null
                && l.getQuantite() == null && l.getCoutUnitaire() == null
                && l.getMontantTresorPublic() == null && l.getMontantAfc() == null && l.getMontantFondsPropres() == null
                && l.getMontantBilateral() == null && l.getMontantMultilateral() == null;
    }

    private TrancheVoieMoyenLigneDto toVoieMoyen(ProjetTrancheVoieMoyen voie) {
        TrancheVoieMoyenLigneDto dto = new TrancheVoieMoyenLigneDto();
        dto.setIdRubrique(voie.getIdRubrique());
        dto.setCodeArticle(voie.getCodeArticle());
        dto.setDesignation(voie.getDesignation());
        dto.setOrdreActivites(voie.getOrdreActivites());
        dto.setUniteMesure(voie.getUniteMesure());
        dto.setQuantite(voie.getQuantite());
        dto.setCoutUnitaire(voie.getCoutUnitaire());
        // Colonne "Total" du canevas : =D48*E48 (quantité × coût unitaire).
        dto.setTotal(nvl(voie.getQuantite()).multiply(nvl(voie.getCoutUnitaire())).setScale(2, RoundingMode.HALF_UP));
        dto.setMontantTresorPublic(voie.getMontantTresorPublic());
        dto.setMontantAfc(voie.getMontantAfc());
        dto.setMontantFondsPropres(voie.getMontantFondsPropres());
        dto.setMontantBilateral(voie.getMontantBilateral());
        dto.setMontantMultilateral(voie.getMontantMultilateral());
        return dto;
    }

    private String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }

    private List<Integer> idActivitesDuProjet(Integer projetId) {
        return activiteRepository.findByIdProjetOrderByOrdreSequentielAsc(projetId).stream()
                .map(Activite::getId)
                .toList();
    }

    private List<TrancheChronogrammeLigneDto> toChronogramme(Integer projetId, Integer idExercice) {
        List<Integer> idActivites = idActivitesDuProjet(projetId);
        Map<Integer, TrancheChronogrammeLigneDto> parActivite = new LinkedHashMap<>();
        for (Integer idActivite : idActivites) {
            TrancheChronogrammeLigneDto dto = new TrancheChronogrammeLigneDto();
            dto.setIdActivite(idActivite);
            parActivite.put(idActivite, dto);
        }
        if (!idActivites.isEmpty()) {
            for (ActiviteChronogrammeMensuel mois : chronogrammeRepository.findByIdActiviteInAndIdExercice(idActivites, idExercice)) {
                int rang = MOIS_EXERCICE.indexOf(mois.getMois()) + 1;
                if (rang > 0 && Boolean.TRUE.equals(mois.getPlanifie())) {
                    parActivite.get(mois.getIdActivite()).getMois().add(rang);
                }
            }
        }
        parActivite.values().forEach(dto -> dto.getMois().sort(Comparator.naturalOrder()));
        return new ArrayList<>(parActivite.values());
    }

    /**
     * §§ 48-49 : une ligne par (sous-rubrique, trimestre) dont le montant est renseigné ; les cases
     * vides ou à 0 ne sont pas stockées. Une rubrique principale est refusée, comme au § 47 : ses
     * montants sont les sous-totaux de ses sous-rubriques.
     */
    private void appliquerCalendrier(Integer projetId, Integer idExercice, String categorie, List<TrancheCalendrierLigneDto> lignes) {
        if (lignes == null) {
            return;
        }
        for (TrancheCalendrierLigneDto ligne : lignes) {
            if (ligne == null) {
                continue;
            }
            for (int rang = 1; rang <= 4; rang++) {
                BigDecimal montant = ligne.getTrimestre(rang);
                if (montant == null || montant.signum() == 0) {
                    continue;
                }
                rubriqueBudgetaireRepository.findById(ligne.getIdRubrique())
                        .filter(r -> r.getIdRubriqueParent() != null)
                        .orElseThrow(() -> new NoSuchElementException("Sous-rubrique budgétaire introuvable."));
                ProjetCalendrierDepenseTrimestrielle cellule = new ProjetCalendrierDepenseTrimestrielle();
                cellule.setIdProjet(projetId);
                cellule.setIdExercice(idExercice);
                cellule.setIdRubrique(ligne.getIdRubrique());
                cellule.setCategorieFinancement(categorie);
                cellule.setTrimestre((short) rang);
                cellule.setMontant(montant);
                calendrierTrimestrielRepository.save(cellule);
            }
        }
        calendrierTrimestrielRepository.flush();
    }

    /** Une ligne par sous-rubrique ayant au moins un trimestre renseigné, dans l'ordre des rubriques. */
    private List<TrancheCalendrierLigneDto> toCalendrier(List<ProjetCalendrierDepenseTrimestrielle> cellules, String categorie) {
        Map<Integer, TrancheCalendrierLigneDto> parRubrique = new java.util.TreeMap<>();
        for (ProjetCalendrierDepenseTrimestrielle cellule : cellules) {
            Short trimestre = cellule.getTrimestre();
            if (!categorie.equals(cellule.getCategorieFinancement()) || trimestre == null || trimestre < 1 || trimestre > 4) {
                continue;
            }
            TrancheCalendrierLigneDto dto = parRubrique.computeIfAbsent(cellule.getIdRubrique(), id -> {
                TrancheCalendrierLigneDto d = new TrancheCalendrierLigneDto();
                d.setIdRubrique(id);
                return d;
            });
            dto.setTrimestre(trimestre, nvl(dto.getTrimestre(trimestre)).add(nvl(cellule.getMontant())));
        }
        return new ArrayList<>(parRubrique.values());
    }

    /**
     * Canevas : Chronogramme!C29 = Calendrier!B59 — le TOTAL (1@5) du § 48, c'est-à-dire les seules
     * ressources NATIONALES, pas le § 50 (nationales et externes).
     */
    private List<BigDecimal> toCoutTrimestres(Integer projetId, Integer idExercice) {
        List<BigDecimal> couts = new ArrayList<>(List.of(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO));
        for (ProjetCalendrierDepenseTrimestrielle ligne : calendrierTrimestrielRepository.findByIdProjetAndIdExercice(projetId, idExercice)) {
            Short trimestre = ligne.getTrimestre();
            if (ProjetCalendrierDepenseTrimestrielle.NATIONAL.equals(ligne.getCategorieFinancement())
                    && trimestre != null && trimestre >= 1 && trimestre <= 4) {
                couts.set(trimestre - 1, couts.get(trimestre - 1).add(nvl(ligne.getMontant())));
            }
        }
        return couts;
    }

    private TrancheAnnuelleResponse toResponse(Integer projetId, Integer idExercice) {
        List<ProjetTrancheDepenseSource> lignes = trancheRepository.findByIdProjetAndIdExercice(projetId, idExercice);
        BigDecimal total = lignes.stream().map(l -> nvl(l.getPrevisionTotal())).reduce(BigDecimal.ZERO, BigDecimal::add);

        TrancheAnnuelleResponse response = new TrancheAnnuelleResponse();
        response.setIdProjet(projetId);
        response.setIdExercice(idExercice);
        exerciceBudgetaireRepository.findById(idExercice).ifPresent(e -> response.setExerciceLibelle(e.getLibelle()));
        response.setMontantTotal(total);
        response.setSources(lignes.stream()
                .sorted(Comparator.comparing(ProjetTrancheDepenseSource::getIdSource))
                .map(l -> toLigne(l, total))
                .toList());
        response.setChronogramme(toChronogramme(projetId, idExercice));
        response.setCoutTrimestres(toCoutTrimestres(projetId, idExercice));
        response.setVoiesMoyens(voieMoyenRepository.findByIdProjetAndIdExerciceOrderByOrdreAsc(projetId, idExercice).stream()
                .map(this::toVoieMoyen)
                .toList());
        List<ProjetCalendrierDepenseTrimestrielle> cellules = calendrierTrimestrielRepository.findByIdProjetAndIdExercice(projetId, idExercice);
        response.setCalendrierNational(toCalendrier(cellules, ProjetCalendrierDepenseTrimestrielle.NATIONAL));
        response.setCalendrierExterne(toCalendrier(cellules, ProjetCalendrierDepenseTrimestrielle.EXTERNE));
        return response;
    }

    private TrancheSourceLigneDto toLigne(ProjetTrancheDepenseSource ligne, BigDecimal total) {
        TrancheSourceLigneDto dto = new TrancheSourceLigneDto();
        dto.setIdSource(ligne.getIdSource());
        sourceFinancementRepository.findById(ligne.getIdSource()).ifPresent(s -> {
            dto.setSourceLibelle(s.getLibelle());
            dto.setSourceCategorie(s.getCategorie());
        });
        dto.setPrevisionTotal(ligne.getPrevisionTotal());
        // Colonne 5 "Poids" du canevas (=G15/G22) : vide plutôt que #DIV/0! quand le total est nul.
        if (total.compareTo(BigDecimal.ZERO) > 0) {
            dto.setPoidsPct(nvl(ligne.getPrevisionTotal()).divide(total, 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_UP));
        }
        return dto;
    }

    private BigDecimal nvl(BigDecimal value) {
        return value != null ? value : BigDecimal.ZERO;
    }

    private void findProjetOrThrow(Integer id) {
        projetRepository.findById(id).orElseThrow(() -> new NoSuchElementException("FIOP introuvable."));
    }
}
