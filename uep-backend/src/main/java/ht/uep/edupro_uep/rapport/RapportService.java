package ht.uep.edupro_uep.rapport;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.stereotype.Service;

import ht.uep.edupro_uep.bilan.BilanService;
import ht.uep.edupro_uep.dto.ActivitePlanLigneDto;
import ht.uep.edupro_uep.dto.BilanResponse;
import ht.uep.edupro_uep.dto.BilanRubriqueLigneDto;
import ht.uep.edupro_uep.dto.FiopResponse;
import ht.uep.edupro_uep.dto.RubriqueCalendrierLigneDto;
import ht.uep.edupro_uep.dto.SourceFinancementPlanLigneDto;
import ht.uep.edupro_uep.dto.TrancheAnnuelleResponse;
import ht.uep.edupro_uep.dto.TrancheSourceLigneDto;
import ht.uep.edupro_uep.fiop.ProjetService;
import ht.uep.edupro_uep.rapport.Rapport.Champ;
import ht.uep.edupro_uep.rapport.Rapport.Section;
import ht.uep.edupro_uep.rapport.Rapport.Tableau;
import ht.uep.edupro_uep.reference.ExerciceBudgetaireRepository;
import ht.uep.edupro_uep.tranche.TrancheAnnuelleService;

/**
 * UC-F3 "Générer un rapport FIOP / PIP" : le superviseur choisit le périmètre (un projet, ou une
 * période) puis le format. Ce service ne fait que rassembler les données — toujours lues depuis
 * les services métier existants (FIOP, Bilan, Tranche annuelle), jamais recalculées autrement — dans
 * un {@link Rapport} neutre ; les classes *Renderer le mettent ensuite en PDF ou en Excel.
 */
@Service
public class RapportService {

    private static final DateTimeFormatter HORODATAGE = DateTimeFormatter.ofPattern("dd/MM/yyyy 'à' HH:mm");

    /** Libellés des statuts, identiques à ceux de l'interface (STATUT_META dans fiopShared.js). */
    private static final Map<String, String> STATUTS = Map.of(
            "BROUILLON", "Brouillon",
            "SOUMIS_SUPERVISEUR_UEP", "Soumis (UEP)",
            "REJETE_UEP", "Retourné (UEP)",
            "SOUMIS_MPCE", "Soumis (MPCE)",
            "REJETE_MPCE", "Retourné (MPCE)",
            "VALIDE_ACTIF", "Actif (validé)");

    private final ProjetService projetService;
    private final BilanService bilanService;
    private final TrancheAnnuelleService trancheService;
    private final ExerciceBudgetaireRepository exerciceBudgetaireRepository;

    public RapportService(ProjetService projetService, BilanService bilanService,
            TrancheAnnuelleService trancheService, ExerciceBudgetaireRepository exerciceBudgetaireRepository) {
        this.projetService = projetService;
        this.bilanService = bilanService;
        this.trancheService = trancheService;
        this.exerciceBudgetaireRepository = exerciceBudgetaireRepository;
    }

    public static String libelleStatut(String statut) {
        return statut == null ? "—" : STATUTS.getOrDefault(statut, statut);
    }

    public String libelleExercice(Integer idExercice) {
        return exerciceBudgetaireRepository.findById(idExercice)
                .orElseThrow(() -> new NoSuchElementException("Exercice budgétaire introuvable."))
                .getLibelle();
    }

    // =====================================================================================
    // Rapport 1 : fiche d'un projet (périmètre = un projet, période facultative)
    // =====================================================================================

    public Rapport ficheProjet(Integer projetId, Integer idExercice, String auteur) {
        FiopResponse f = projetService.getFiop(projetId);
        String exercice = idExercice != null ? libelleExercice(idExercice) : null;

        List<Section> sections = new ArrayList<>();
        sections.add(Section.champs("Identification", List.of(
                new Champ("Code projet", f.getCodeProjet()),
                new Champ("Code PIP", texte(f.getCodeInternePip())),
                new Champ("Titre", f.getTitre()),
                new Champ("Statut", libelleStatut(f.getStatut())),
                new Champ("Grand chantier", texte(f.getGrandChantierLibelle())),
                new Champ("Programme", texte(f.getProgrammeLibelle())),
                new Champ("Sous-programme", texte(f.getSousProgrammeLibelle())),
                new Champ("Exercice de création", texte(f.getExerciceLibelle())),
                new Champ("Ministère de tutelle", texte(f.getMinistereTutelle())),
                new Champ("Responsable du projet", contact(f.getNomChargeProjet(), f.getTelephoneChargeProjet(), f.getCourrielChargeProjet())))));

        sections.add(Section.champs("Localisation, durée et coût", List.of(
                new Champ("Département", texte(f.getDepartementLibelle())),
                new Champ("Commune", texte(f.getCommuneLibelle())),
                new Champ("Section communale", texte(f.getSectionCommunaleLibelle())),
                new Champ("Durée totale", f.getDureeTotaleMois() != null ? f.getDureeTotaleMois() + " mois" : "—"),
                new Champ("Coût total", f.getCoutTotalGourde() != null ? Montants.gourdes(f.getCoutTotalGourde()) : "—"),
                new Champ("Type d'investissement", texte(f.getTypeInvestissement())))));

        sections.add(sourcesFinancement(f.getSourcesFinancement()));
        sections.add(activites(f.getActivites()));
        sections.add(calendrierRubriques(f.getCalendrierRubriques()));

        if (idExercice != null) {
            sections.addAll(sectionsExercice(projetId, idExercice, exercice));
        }

        String sousTitre = (exercice != null ? "Exercice " + exercice + " · " : "") + metadonnees(auteur);
        return new Rapport("Fiche projet : " + f.getCodeProjet() + " · " + f.getTitre(), sousTitre, false, sections);
    }

    private Section sourcesFinancement(List<SourceFinancementPlanLigneDto> sources) {
        List<SourceFinancementPlanLigneDto> lignes = sources == null ? List.of() : sources;
        if (lignes.isEmpty()) {
            return Section.note("Sources de financement (§29)", "Aucune source de financement saisie.");
        }
        BigDecimal total = somme(lignes, SourceFinancementPlanLigneDto::getPrevisionTotal);
        List<List<Object>> rows = new ArrayList<>();
        for (SourceFinancementPlanLigneDto s : lignes) {
            rows.add(Rapport.ligne(s.getSourceLibelle(), nvl(s.getPrevisionTotal()), pourcentage(s.getPrevisionTotal(), total),
                    nvl(s.getPtiAnnee1()), nvl(s.getPtiAnnee2()), nvl(s.getPtiAnnee3())));
        }
        return Section.tableau("Sources de financement (§29)", new Tableau(
                List.of("Source", "Prévision totale", "Poids", "PTI 1", "PTI 2", "PTI 3"),
                new float[] { 3f, 2f, 1.2f, 1.8f, 1.8f, 1.8f }, rows,
                Rapport.ligne("Total", total, total.signum() > 0 ? "100 %" : "—",
                        somme(lignes, SourceFinancementPlanLigneDto::getPtiAnnee1),
                        somme(lignes, SourceFinancementPlanLigneDto::getPtiAnnee2),
                        somme(lignes, SourceFinancementPlanLigneDto::getPtiAnnee3))), null);
    }

    private Section activites(List<ActivitePlanLigneDto> activites) {
        List<ActivitePlanLigneDto> lignes = activites == null ? List.of() : activites;
        if (lignes.isEmpty()) {
            return Section.note("Activités et résultats attendus (§31-33)", "Aucune activité saisie.");
        }
        List<List<Object>> rows = new ArrayList<>();
        int ordre = 1;
        for (ActivitePlanLigneDto a : lignes) {
            rows.add(Rapport.ligne(String.format("%02d", ordre++), a.getLibelle(), texte(a.getUniteResultat()),
                    a.getQuantiteResultatAttendu(), a.getCoutUnitaire(), coutActivite(a)));
        }
        return Section.tableau("Activités et résultats attendus (§31-33)", new Tableau(
                List.of("#", "Activité", "Unité", "Quantité", "Coût unitaire", "Coût total"),
                new float[] { 0.6f, 5f, 1.6f, 1.4f, 2f, 2f }, rows,
                Rapport.ligne("", "Total", "", "", "", somme(lignes, this::coutActivite))), null);
    }

    private BigDecimal coutActivite(ActivitePlanLigneDto a) {
        if (a.getCoutTotal() != null) {
            return a.getCoutTotal();
        }
        return nvl(a.getQuantiteResultatAttendu()).multiply(nvl(a.getCoutUnitaire()));
    }

    private Section calendrierRubriques(List<RubriqueCalendrierLigneDto> rubriques) {
        List<RubriqueCalendrierLigneDto> lignes = (rubriques == null ? List.<RubriqueCalendrierLigneDto>of() : rubriques).stream()
                .filter(r -> nvl(r.getMontantTotal()).signum() != 0)
                .toList();
        if (lignes.isEmpty()) {
            return Section.note("Dépenses prévisionnelles par rubrique (§34)", "Aucune dépense prévisionnelle saisie.");
        }
        List<List<Object>> rows = new ArrayList<>();
        for (RubriqueCalendrierLigneDto r : lignes) {
            rows.add(Rapport.ligne(r.getRubriqueLibelle(), nvl(r.getMontantAnnee1()), nvl(r.getMontantAnnee2()),
                    nvl(r.getMontantAnnee3()), nvl(r.getMontantAnnee4()), nvl(r.getMontantAnnee5()), nvl(r.getMontantTotal())));
        }
        return Section.tableau("Dépenses prévisionnelles par rubrique (§34)", new Tableau(
                List.of("Sous-rubrique", "Année 1", "Année 2", "Année 3", "Année 4", "Année 5", "Total"),
                new float[] { 4f, 1.7f, 1.7f, 1.7f, 1.7f, 1.7f, 1.9f }, rows,
                Rapport.ligne("Total",
                        somme(lignes, RubriqueCalendrierLigneDto::getMontantAnnee1),
                        somme(lignes, RubriqueCalendrierLigneDto::getMontantAnnee2),
                        somme(lignes, RubriqueCalendrierLigneDto::getMontantAnnee3),
                        somme(lignes, RubriqueCalendrierLigneDto::getMontantAnnee4),
                        somme(lignes, RubriqueCalendrierLigneDto::getMontantAnnee5),
                        somme(lignes, RubriqueCalendrierLigneDto::getMontantTotal))), null);
    }

    /** Bilan d'exécution (§41) et tranche annuelle (§44) de l'exercice choisi, s'ils existent. */
    private List<Section> sectionsExercice(Integer projetId, Integer idExercice, String exercice) {
        List<Section> sections = new ArrayList<>();

        Optional<BilanResponse> bilan = bilanDeLExercice(projetId, idExercice);
        String titreBilan = "Bilan d'exécution " + exercice + " : opérations par rubrique (§41)";
        if (bilan.isEmpty() || bilan.get().getRubriques() == null || bilan.get().getRubriques().isEmpty()) {
            sections.add(Section.note(titreBilan, "Aucun bilan d'exécution saisi pour cet exercice."));
        } else {
            List<BilanRubriqueLigneDto> lignes = bilan.get().getRubriques();
            List<List<Object>> rows = new ArrayList<>();
            for (BilanRubriqueLigneDto r : lignes) {
                rows.add(Rapport.ligne(r.getRubriqueLibelle(), nvl(r.getMontantPrevu()), nvl(r.getDepensesAnterieuresN2()),
                        nvl(r.getDepensesExercice()), nvl(r.getDepensesCumulees()), nvl(r.getBalancePrevisionnelle())));
            }
            sections.add(Section.tableau(titreBilan, new Tableau(
                    List.of("Sous-rubrique", "Coût prévisionnel", "Dép. antérieures", "Dép. de l'exercice", "Dép. cumulées", "Balance"),
                    new float[] { 4f, 2f, 2f, 2f, 2f, 2f }, rows,
                    Rapport.ligne("Total",
                            somme(lignes, BilanRubriqueLigneDto::getMontantPrevu),
                            somme(lignes, BilanRubriqueLigneDto::getDepensesAnterieuresN2),
                            somme(lignes, BilanRubriqueLigneDto::getDepensesExercice),
                            somme(lignes, BilanRubriqueLigneDto::getDepensesCumulees),
                            somme(lignes, BilanRubriqueLigneDto::getBalancePrevisionnelle))), null));
        }

        Optional<TrancheAnnuelleResponse> tranche = trancheService.trouverTranche(projetId, idExercice);
        String titreTranche = "Tranche annuelle " + exercice + " : dépenses prévisionnelles par source (§44)";
        if (tranche.isEmpty()) {
            sections.add(Section.note(titreTranche, "Aucune tranche annuelle saisie pour cet exercice."));
        } else {
            List<TrancheSourceLigneDto> lignes = tranche.get().getSources();
            List<List<Object>> rows = new ArrayList<>();
            for (TrancheSourceLigneDto s : lignes) {
                rows.add(Rapport.ligne(s.getSourceLibelle(), texte(s.getSourceCategorie()), nvl(s.getPrevisionTotal()),
                        s.getPoidsPct() != null ? Montants.pourcent(s.getPoidsPct()) : "—"));
            }
            BigDecimal total = nvl(tranche.get().getMontantTotal());
            sections.add(Section.tableau(titreTranche, new Tableau(
                    List.of("Source", "Catégorie", "Prévision", "Poids"),
                    new float[] { 3f, 2f, 2.5f, 1.5f }, rows,
                    Rapport.ligne("Montant total", "", total, total.signum() > 0 ? "100 %" : "—")), null));
        }
        return sections;
    }

    // =====================================================================================
    // Rapport 2 : portefeuille de projets (périmètre = une période / un exercice)
    // =====================================================================================

    public Rapport portefeuille(Integer idExercice, String statut, String auteur) {
        if (idExercice == null) {
            throw new ParametreRapportInvalideException("Choisissez l'exercice budgétaire (la période) du rapport.");
        }
        if (statut != null && !statut.isBlank() && !STATUTS.containsKey(statut)) {
            throw new ParametreRapportInvalideException("Statut inconnu : " + statut);
        }
        String exercice = libelleExercice(idExercice);

        List<FiopResponse> projets = projetService.listFiops().stream()
                .filter(p -> statut == null || statut.isBlank() || statut.equals(p.getStatut()))
                .sorted(Comparator.comparing(FiopResponse::getId))
                .toList();

        List<List<Object>> rows = new ArrayList<>();
        BigDecimal totalCout = BigDecimal.ZERO;
        BigDecimal totalPrevision = BigDecimal.ZERO;
        BigDecimal totalDepenses = BigDecimal.ZERO;
        for (FiopResponse p : projets) {
            BigDecimal prevision = trancheService.trouverTranche(p.getId(), idExercice)
                    .map(TrancheAnnuelleResponse::getMontantTotal).orElse(null);
            BigDecimal depenses = bilanDeLExercice(p.getId(), idExercice)
                    .map(b -> somme(b.getRubriques() == null ? List.of() : b.getRubriques(), BilanRubriqueLigneDto::getDepensesExercice))
                    .orElse(null);
            rows.add(Rapport.ligne(p.getCodeProjet(), texte(p.getCodeInternePip()), p.getTitre(), libelleStatut(p.getStatut()),
                    texte(p.getDepartementLibelle()), p.getCoutTotalGourde(), prevision, depenses, taux(depenses, prevision)));
            totalCout = totalCout.add(nvl(p.getCoutTotalGourde()));
            totalPrevision = totalPrevision.add(nvl(prevision));
            totalDepenses = totalDepenses.add(nvl(depenses));
        }

        List<Section> sections = new ArrayList<>();
        sections.add(Section.champs("Périmètre", List.of(
                new Champ("Exercice budgétaire", exercice),
                new Champ("Statut des projets", statut == null || statut.isBlank() ? "Tous les statuts" : libelleStatut(statut)),
                new Champ("Nombre de projets", String.valueOf(projets.size())))));
        if (projets.isEmpty()) {
            sections.add(Section.note("Projets", "Aucun projet ne correspond à ce périmètre."));
        } else {
            sections.add(Section.tableau("Projets", new Tableau(
                    List.of("Code projet", "Code PIP", "Titre", "Statut", "Département", "Coût total",
                            "Prévision " + exercice + " (§44)", "Dépenses " + exercice + " (bilan)", "Taux d'exécution"),
                    new float[] { 1.5f, 1.8f, 5f, 1.8f, 1.8f, 2f, 2f, 2f, 1.4f }, rows,
                    Rapport.ligne("Total", "", "", "", "", totalCout, totalPrevision, totalDepenses, taux(totalDepenses, totalPrevision))),
                    "Prévision de l'exercice : montant total de la tranche annuelle (§44). Dépenses de l'exercice : total des "
                            + "dépenses de l'exercice du bilan d'exécution (§41). « — » : non saisi pour cet exercice."));
        }
        return new Rapport("Portefeuille des projets : exercice " + exercice, metadonnees(auteur), true, sections);
    }

    // =====================================================================================

    private Optional<BilanResponse> bilanDeLExercice(Integer projetId, Integer idExercice) {
        return bilanService.listerBilans(projetId).stream()
                .filter(b -> idExercice.equals(b.getIdExercice()))
                .findFirst();
    }

    private String metadonnees(String auteur) {
        return "Généré le " + LocalDateTime.now().format(HORODATAGE) + " par " + auteur;
    }

    private static String texte(String valeur) {
        return valeur == null || valeur.isBlank() ? "—" : valeur;
    }

    private static String contact(String nom, String telephone, String courriel) {
        List<String> parties = new ArrayList<>();
        for (String p : new String[] { nom, telephone, courriel }) {
            if (p != null && !p.isBlank()) {
                parties.add(p);
            }
        }
        return parties.isEmpty() ? "—" : String.join(" · ", parties);
    }

    private static BigDecimal nvl(BigDecimal v) {
        return v != null ? v : BigDecimal.ZERO;
    }

    private static <T> BigDecimal somme(List<T> lignes, Function<T, BigDecimal> valeur) {
        return lignes.stream().map(valeur).map(RapportService::nvl).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private static String pourcentage(BigDecimal part, BigDecimal total) {
        if (total == null || total.signum() <= 0) {
            return "—";
        }
        return Montants.pourcent(nvl(part).multiply(BigDecimal.valueOf(100)).divide(total, 2, RoundingMode.HALF_UP));
    }

    /** Taux d'exécution = dépenses / prévision ; "—" quand la prévision manque ou vaut 0. */
    private static String taux(BigDecimal depenses, BigDecimal prevision) {
        if (depenses == null || prevision == null || prevision.signum() <= 0) {
            return "—";
        }
        return pourcentage(depenses, prevision);
    }
}
