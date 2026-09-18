package ht.uep.edupro_uep.fiop;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ht.uep.edupro_uep.bilan.Activite;
import ht.uep.edupro_uep.bilan.ActivitePlanificationAnnuelle;
import ht.uep.edupro_uep.bilan.ActivitePlanificationAnnuelleRepository;
import ht.uep.edupro_uep.bilan.ActiviteRepository;
import ht.uep.edupro_uep.bilan.BilanAvancementFinancierRepository;
import ht.uep.edupro_uep.bilan.BilanAvancementPhysiqueRepository;
import ht.uep.edupro_uep.dto.ActivitePlanLigneDto;
import ht.uep.edupro_uep.dto.CoutRecurrentLigneDto;
import ht.uep.edupro_uep.dto.FiopRequest;
import ht.uep.edupro_uep.dto.FiopResponse;
import ht.uep.edupro_uep.dto.IntervenantDto;
import ht.uep.edupro_uep.dto.IntrantBudgetPhaseLigneDto;
import ht.uep.edupro_uep.dto.PhaseActuelleLigneDto;
import ht.uep.edupro_uep.dto.RubriqueCalendrierLigneDto;
import ht.uep.edupro_uep.dto.SourceFinancementPlanLigneDto;
import ht.uep.edupro_uep.geo.ArrondissementRepository;
import ht.uep.edupro_uep.geo.CommuneRepository;
import ht.uep.edupro_uep.geo.DepartementRepository;
import ht.uep.edupro_uep.geo.SectionCommunaleRepository;
import ht.uep.edupro_uep.reference.ExerciceBudgetaireRepository;
import ht.uep.edupro_uep.reference.GrandChantierRepository;
import ht.uep.edupro_uep.reference.ProgrammeRepository;
import ht.uep.edupro_uep.reference.RubriqueBudgetaire;
import ht.uep.edupro_uep.reference.RubriqueBudgetaireRepository;
import ht.uep.edupro_uep.reference.SourceFinancement;
import ht.uep.edupro_uep.reference.SourceFinancementRepository;
import ht.uep.edupro_uep.reference.SousProgrammeRepository;
import ht.uep.edupro_uep.user.Role;
import ht.uep.edupro_uep.user.User;
import ht.uep.edupro_uep.user.UserRepository;

/**
 * Cycle de vie du FIOP (= le projet lui-même, voir Projet) :
 *
 * Brouillon -> SoumisSuperviseurUep -> SoumisMpce -> ValideActif
 *                     \-> RejeteUep         \-> RejeteMpce
 *
 * Le Superviseur UEP distribue/contrôle le travail des Opérateurs et
 * transmet directement au MPCE une fois satisfait ; le Superviseur MPCE
 * approuve en dernier lieu et rend le projet actif.
 */
@Service
public class ProjetService {

    private final ProjetRepository projetRepository;
    private final UserRepository userRepository;
    private final DepartementRepository departementRepository;
    private final ArrondissementRepository arrondissementRepository;
    private final CommuneRepository communeRepository;
    private final SectionCommunaleRepository sectionCommunaleRepository;
    private final GrandChantierRepository grandChantierRepository;
    private final ProgrammeRepository programmeRepository;
    private final SousProgrammeRepository sousProgrammeRepository;
    private final ExerciceBudgetaireRepository exerciceBudgetaireRepository;
    private final AspectLegalRepository aspectLegalRepository;
    private final AspectInstitutionnelRepository aspectInstitutionnelRepository;
    private final IndicateurResultatRepository indicateurResultatRepository;
    private final ExtrantRepository extrantRepository;
    private final ProjetIntrantBudgetPhaseRepository intrantBudgetPhaseRepository;
    private final PopulationViseeRepository populationViseeRepository;
    private final EmploiCreeRepository emploiCreeRepository;
    private final IntervenantRepository intervenantRepository;
    private final ActiviteRepository activiteRepository;
    private final ActivitePlanificationAnnuelleRepository activitePlanificationAnnuelleRepository;
    private final BilanAvancementPhysiqueRepository bilanAvancementPhysiqueRepository;
    private final BilanAvancementFinancierRepository bilanAvancementFinancierRepository;
    private final ProjetCalendrierDepenseAnnuelleRepository calendrierDepenseAnnuelleRepository;
    private final ProjetCalendrierRubriqueOrdreActivitesRepository calendrierOrdreActivitesRepository;
    private final ProjetCoutRecurrentRepository coutRecurrentRepository;
    private final ProjetFinancementRepository projetFinancementRepository;
    private final RubriqueBudgetaireRepository rubriqueBudgetaireRepository;
    private final SourceFinancementRepository sourceFinancementRepository;
    private final PhaseActuelleProjetRepository phaseActuelleProjetRepository;

    public ProjetService(
            ProjetRepository projetRepository,
            UserRepository userRepository,
            DepartementRepository departementRepository,
            ArrondissementRepository arrondissementRepository,
            CommuneRepository communeRepository,
            SectionCommunaleRepository sectionCommunaleRepository,
            GrandChantierRepository grandChantierRepository,
            ProgrammeRepository programmeRepository,
            SousProgrammeRepository sousProgrammeRepository,
            ExerciceBudgetaireRepository exerciceBudgetaireRepository,
            AspectLegalRepository aspectLegalRepository,
            AspectInstitutionnelRepository aspectInstitutionnelRepository,
            IndicateurResultatRepository indicateurResultatRepository,
            ExtrantRepository extrantRepository,
            ProjetIntrantBudgetPhaseRepository intrantBudgetPhaseRepository,
            PopulationViseeRepository populationViseeRepository,
            EmploiCreeRepository emploiCreeRepository,
            IntervenantRepository intervenantRepository,
            ActiviteRepository activiteRepository,
            ActivitePlanificationAnnuelleRepository activitePlanificationAnnuelleRepository,
            BilanAvancementPhysiqueRepository bilanAvancementPhysiqueRepository,
            BilanAvancementFinancierRepository bilanAvancementFinancierRepository,
            ProjetCalendrierDepenseAnnuelleRepository calendrierDepenseAnnuelleRepository,
            ProjetCalendrierRubriqueOrdreActivitesRepository calendrierOrdreActivitesRepository,
            ProjetCoutRecurrentRepository coutRecurrentRepository,
            ProjetFinancementRepository projetFinancementRepository,
            RubriqueBudgetaireRepository rubriqueBudgetaireRepository,
            SourceFinancementRepository sourceFinancementRepository,
            PhaseActuelleProjetRepository phaseActuelleProjetRepository) {
        this.projetRepository = projetRepository;
        this.userRepository = userRepository;
        this.departementRepository = departementRepository;
        this.arrondissementRepository = arrondissementRepository;
        this.communeRepository = communeRepository;
        this.sectionCommunaleRepository = sectionCommunaleRepository;
        this.grandChantierRepository = grandChantierRepository;
        this.programmeRepository = programmeRepository;
        this.sousProgrammeRepository = sousProgrammeRepository;
        this.exerciceBudgetaireRepository = exerciceBudgetaireRepository;
        this.aspectLegalRepository = aspectLegalRepository;
        this.aspectInstitutionnelRepository = aspectInstitutionnelRepository;
        this.indicateurResultatRepository = indicateurResultatRepository;
        this.extrantRepository = extrantRepository;
        this.intrantBudgetPhaseRepository = intrantBudgetPhaseRepository;
        this.populationViseeRepository = populationViseeRepository;
        this.emploiCreeRepository = emploiCreeRepository;
        this.intervenantRepository = intervenantRepository;
        this.activiteRepository = activiteRepository;
        this.activitePlanificationAnnuelleRepository = activitePlanificationAnnuelleRepository;
        this.bilanAvancementPhysiqueRepository = bilanAvancementPhysiqueRepository;
        this.bilanAvancementFinancierRepository = bilanAvancementFinancierRepository;
        this.calendrierDepenseAnnuelleRepository = calendrierDepenseAnnuelleRepository;
        this.calendrierOrdreActivitesRepository = calendrierOrdreActivitesRepository;
        this.coutRecurrentRepository = coutRecurrentRepository;
        this.projetFinancementRepository = projetFinancementRepository;
        this.rubriqueBudgetaireRepository = rubriqueBudgetaireRepository;
        this.sourceFinancementRepository = sourceFinancementRepository;
        this.phaseActuelleProjetRepository = phaseActuelleProjetRepository;
    }

    @Transactional
    public FiopResponse creerFiop(FiopRequest request, String username) {
        User createur = findUserOrThrow(username);

        Projet projet = new Projet();
        applyRequest(projet, request);
        projet.setStatut(StatutFiop.BROUILLON);
        projet.setDateInscription(LocalDate.now());
        projet.setIdUtilisateurCreation(createur.getId());
        projet.setIdUtilisateurDerniereMaj(createur.getId());
        // Placeholder unique le temps que la séquence assigne l'id (colonne NOT NULL UNIQUE) ;
        // recalculé juste après avec l'id réel, dans la même transaction (pas de flush entre les deux).
        projet.setCodeProjet(UUID.randomUUID().toString());
        projetRepository.save(projet);
        projet.setCodeProjet("FIOP-" + projet.getId());
        applyChildren(projet.getId(), request);
        // Flush explicite : sans lui, @CreationTimestamp/@UpdateTimestamp ne sont
        // renseignés qu'au commit de la transaction, après la construction de la réponse.
        projetRepository.flush();

        return toResponse(projet);
    }

    /**
     * L'Opérateur créateur peut modifier librement tant que le FIOP est en
     * Brouillon. Le Superviseur UEP, qui contrôle le travail des Opérateurs,
     * peut aussi le modifier une fois soumis à son niveau (pas besoin de
     * rejeter pour une simple correction avant transmission au MPCE).
     */
    @Transactional
    public FiopResponse modifierFiop(Integer id, FiopRequest request, String username) {
        Projet projet = findProjetOrThrow(id);
        User user = findUserOrThrow(username);

        if (projet.getStatut() == StatutFiop.BROUILLON) {
            requireCreateur(projet, user);
        } else if (projet.getStatut() == StatutFiop.SOUMIS_SUPERVISEUR_UEP) {
            if (user.getRole() != Role.SUPERVISEUR_UEP) {
                throw new IllegalStateException("Seul le Superviseur UEP peut modifier un FIOP à ce stade.");
            }
        } else {
            throw new IllegalStateException("Cette action n'est possible qu'à l'état Brouillon ou Soumis (UEP).");
        }

        applyRequest(projet, request);
        projet.setIdUtilisateurDerniereMaj(user.getId());
        projetRepository.save(projet);
        applyChildren(projet.getId(), request);
        return toResponse(projet);
    }

    public void supprimerFiop(Integer id, String username) {
        Projet projet = findProjetOrThrow(id);
        User user = findUserOrThrow(username);

        requireBrouillon(projet);
        requireCreateur(projet, user);

        projetRepository.delete(projet);
    }

    public FiopResponse soumettreFiop(Integer id, String username) {
        Projet projet = findProjetOrThrow(id);
        User user = findUserOrThrow(username);

        requireBrouillon(projet);
        requireCreateur(projet, user);

        projet.setStatut(StatutFiop.SOUMIS_SUPERVISEUR_UEP);
        projet.setIdUtilisateurDerniereMaj(user.getId());
        projetRepository.save(projet);
        return toResponse(projet);
    }

    public FiopResponse validerFiop(Integer id, String username) {
        Projet projet = findProjetOrThrow(id);
        User user = findUserOrThrow(username);

        if (projet.getStatut() != StatutFiop.SOUMIS_SUPERVISEUR_UEP) {
            throw new IllegalStateException("Seul un FIOP Soumis au Superviseur UEP peut être validé.");
        }

        // La validation UEP transmet directement au MPCE (pas d'état intermédiaire).
        projet.setStatut(StatutFiop.SOUMIS_MPCE);
        projet.setIdUtilisateurDerniereMaj(user.getId());
        projetRepository.save(projet);
        return toResponse(projet);
    }

    public FiopResponse approuverFiop(Integer id, String username) {
        Projet projet = findProjetOrThrow(id);
        User user = findUserOrThrow(username);

        if (projet.getStatut() != StatutFiop.SOUMIS_MPCE) {
            throw new IllegalStateException("Seul un FIOP Soumis au MPCE peut être approuvé.");
        }

        projet.setStatut(StatutFiop.VALIDE_ACTIF);
        projet.setIdUtilisateurDerniereMaj(user.getId());
        projetRepository.save(projet);
        return toResponse(projet);
    }

    public FiopResponse rejeterFiop(Integer id, String motif, String username) {
        Projet projet = findProjetOrThrow(id);
        User user = findUserOrThrow(username);

        StatutFiop nouveauStatut = switch (projet.getStatut()) {
            case SOUMIS_SUPERVISEUR_UEP -> {
                if (user.getRole() != Role.SUPERVISEUR_UEP) {
                    throw new IllegalStateException("Seul le Superviseur UEP peut rejeter un FIOP à ce stade.");
                }
                yield StatutFiop.REJETE_UEP;
            }
            case SOUMIS_MPCE -> {
                if (user.getRole() != Role.SUPERVISEUR_MPCE) {
                    throw new IllegalStateException("Seul le Superviseur MPCE peut rejeter un FIOP à ce stade.");
                }
                yield StatutFiop.REJETE_MPCE;
            }
            default -> throw new IllegalStateException(
                    "Seul un FIOP Soumis (niveau UEP ou MPCE) peut être rejeté.");
        };

        projet.setStatut(nouveauStatut);
        projet.setMotifRejet(motif);
        projet.setIdUtilisateurDerniereMaj(user.getId());
        projetRepository.save(projet);
        return toResponse(projet);
    }

    @Transactional
    public FiopResponse corrigerFiop(Integer id, FiopRequest request, String username) {
        Projet projet = findProjetOrThrow(id);
        User user = findUserOrThrow(username);

        StatutFiop nouveauStatut = switch (projet.getStatut()) {
            case REJETE_UEP -> {
                requireCreateur(projet, user);
                yield StatutFiop.SOUMIS_SUPERVISEUR_UEP;
            }
            case REJETE_MPCE -> {
                if (user.getRole() != Role.SUPERVISEUR_UEP) {
                    throw new IllegalStateException("Seul le Superviseur UEP peut corriger un rejet du MPCE.");
                }
                yield StatutFiop.SOUMIS_MPCE;
            }
            default -> throw new IllegalStateException("Ce FIOP n'est pas en attente de correction.");
        };

        applyRequest(projet, request);
        projet.setStatut(nouveauStatut);
        projet.setMotifRejet(null);
        projet.setIdUtilisateurDerniereMaj(user.getId());
        projetRepository.save(projet);
        applyChildren(projet.getId(), request);
        return toResponse(projet);
    }

    /**
     * "Créer le PIP" = attribuer le code interne PIP au projet une fois actif.
     * Le code est immuable une fois attribué (aucune suppression possible).
     */
    public FiopResponse assignerCodePip(Integer id, String codeInternePip, String username) {
        Projet projet = findProjetOrThrow(id);
        User user = findUserOrThrow(username);

        if (projet.getStatut() != StatutFiop.VALIDE_ACTIF) {
            throw new IllegalStateException("Le code PIP ne peut être attribué qu'à un projet Actif.");
        }
        if (projet.getCodeInternePip() != null) {
            throw new IllegalStateException("Le code PIP a déjà été attribué et ne peut plus être modifié.");
        }

        projet.setCodeInternePip(codeInternePip);
        projet.setIdUtilisateurDerniereMaj(user.getId());
        projetRepository.save(projet);
        return toResponse(projet);
    }

    public List<FiopResponse> listFiops() {
        return projetRepository.findAll().stream().map(this::toResponse).toList();
    }

    public FiopResponse getFiop(Integer id) {
        return toResponse(findProjetOrThrow(id));
    }

    Projet findProjetOrThrow(Integer id) {
        return projetRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("FIOP introuvable."));
    }

    private void requireBrouillon(Projet projet) {
        if (projet.getStatut() != StatutFiop.BROUILLON) {
            throw new IllegalStateException("Cette action n'est possible qu'à l'état Brouillon.");
        }
    }

    private void requireCreateur(Projet projet, User user) {
        if (!user.getId().equals(projet.getIdUtilisateurCreation())) {
            throw new IllegalStateException("Seul le créateur du FIOP peut effectuer cette action.");
        }
    }

    private User findUserOrThrow(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("Utilisateur introuvable."));
    }

    private void applyRequest(Projet projet, FiopRequest request) {
        projet.setTitre(request.getTitre());
        projet.setDureeTotaleMois(request.getDureeTotaleMois());
        projet.setCoutTotalGourde(request.getCoutTotalGourde());
        projet.setNomChargeProjet(request.getNomChargeProjet());
        projet.setTelephoneChargeProjet(request.getTelephoneChargeProjet());
        projet.setCourrielChargeProjet(request.getCourrielChargeProjet());
        projet.setJustification(request.getJustification());
        projet.setEffetsAttendus(request.getEffetsAttendus());
        // "" (select non renseigné) violerait la contrainte de clé étrangère, qui n'autorise
        // qu'une vraie référence géographique ou NULL.
        projet.setIdDepartement(blankToNull(request.getIdDepartement()));
        projet.setIdArrondissement(blankToNull(request.getIdArrondissement()));
        projet.setIdCommune(blankToNull(request.getIdCommune()));
        projet.setIdSectionCommunale(blankToNull(request.getIdSectionCommunale()));
        projet.setHabitationLocaliteQuartier(request.getHabitationLocaliteQuartier());
        projet.setLocalisationGps(request.getLocalisationGps());
        projet.setCodeExterneBailleur(request.getCodeExterneBailleur());
        projet.setIdGrandChantier(request.getIdGrandChantier());
        projet.setIdProgramme(request.getIdProgramme());
        projet.setIdSousProgramme(request.getIdSousProgramme());
        projet.setIdExerciceCreation(request.getIdExerciceCreation());
        projet.setProjetPsdh(request.getProjetPsdh());
        projet.setCiblePrioritaireGvt(
                request.getCiblePrioritaireGvt() != null && request.getCiblePrioritaireGvt());
        projet.setEchelonTerritorial(request.getEchelonTerritorial());
        projet.setMinistereTutelle(
                request.getMinistereTutelle() == null || request.getMinistereTutelle().isBlank()
                        ? "MENFP" : request.getMinistereTutelle());
    }

    /**
     * §30 : multi-choix — un projet peut avoir plusieurs phases cochées à la fois (chacune avec
     * sa propre période), stockées dans projet_phase_actuelle. Remplacement intégral à chaque
     * enregistrement, comme les autres listes du formulaire (aspectsLegaux, extrants...).
     */
    private void applyPhasesActuelles(Integer projetId, FiopRequest request) {
        phaseActuelleProjetRepository.deleteByIdProjet(projetId);
        List<PhaseActuelleLigneDto> lignes = request.getPhasesActuelles();
        if (lignes == null) {
            return;
        }
        for (PhaseActuelleLigneDto ligne : lignes) {
            if (ligne == null || ligne.getPhase() == null || ligne.getPhase().isBlank()) {
                continue;
            }
            PhaseActuelleProjet p = new PhaseActuelleProjet();
            p.setIdProjet(projetId);
            p.setPhase(ligne.getPhase());
            p.setPeriode(ligne.getPeriode());
            phaseActuelleProjetRepository.save(p);
        }
    }

    private void applyChildren(Integer projetId, FiopRequest request) {
        aspectLegalRepository.deleteByIdProjet(projetId);
        saveOrderedList(request.getAspectsLegaux(), (description, ordre) -> {
            AspectLegal a = new AspectLegal();
            a.setIdProjet(projetId);
            a.setOrdre(ordre);
            a.setDescription(description);
            aspectLegalRepository.save(a);
        });

        aspectInstitutionnelRepository.deleteByIdProjet(projetId);
        saveOrderedList(request.getAspectsInstitutionnels(), (description, ordre) -> {
            AspectInstitutionnel a = new AspectInstitutionnel();
            a.setIdProjet(projetId);
            a.setOrdre(ordre);
            a.setDescription(description);
            aspectInstitutionnelRepository.save(a);
        });

        indicateurResultatRepository.deleteByIdProjet(projetId);
        saveOrderedList(request.getIndicateursResultats(), (description, ordre) -> {
            IndicateurResultat i = new IndicateurResultat();
            i.setIdProjet(projetId);
            i.setOrdre(ordre);
            i.setDescription(description);
            indicateurResultatRepository.save(i);
        });

        extrantRepository.deleteByIdProjet(projetId);
        saveOrderedList(request.getExtrants(), (description, ordre) -> {
            Extrant e = new Extrant();
            e.setIdProjet(projetId);
            e.setOrdre(ordre);
            e.setDescription(description);
            extrantRepository.save(e);
        });

        applyIntrantsBudgetPhases(projetId, request.getIntrantsBudgetPhases());

        populationViseeRepository.deleteByIdProjet(projetId);
        savePopulation(projetId, "Enfants garçons", request.getPopulationEnfantsGarcons());
        savePopulation(projetId, "Enfants filles", request.getPopulationEnfantsFilles());
        savePopulation(projetId, "Hommes", request.getPopulationHommes());
        savePopulation(projetId, "Femmes", request.getPopulationFemmes());

        emploiCreeRepository.deleteByIdProjet(projetId);
        saveEmploi(projetId, "Pendant", "Homme", request.getEmploiPendantHomme());
        saveEmploi(projetId, "Pendant", "Femme", request.getEmploiPendantFemme());
        saveEmploi(projetId, "Apres", "Homme", request.getEmploiApresHomme());
        saveEmploi(projetId, "Apres", "Femme", request.getEmploiApresFemme());

        intervenantRepository.deleteByIdProjet(projetId);
        saveIntervenant(projetId, "Supervision", request.getSupervision());
        saveIntervenant(projetId, "Execution", request.getExecution());
        saveIntervenant(projetId, "Bailleur", request.getBailleur());
        saveIntervenant(projetId, "Agence1", request.getAgence1());
        saveIntervenant(projetId, "Agence2", request.getAgence2());

        applyActivitesPlan(projetId, request.getActivites());
        applyCalendrierRubriques(projetId, request.getCalendrierRubriques());
        applyCoutsRecurrents(projetId, request.getCoutsRecurrents());
        applySourcesFinancement(projetId, request.getSourcesFinancement());
        applyPhasesActuelles(projetId, request);
    }

    /**
     * §§31/33 de la FIOP : liste des activités du projet (saisie une seule fois, référencée en
     * lecture seule par le Bilan d'Exécution). Mise à jour "en place" par position (ordre
     * séquentiel) plutôt que suppression/recréation : une activité déjà référencée par un bilan
     * existant ne doit jamais être supprimée sous peine de violer la contrainte de clé étrangère.
     */
    private void applyActivitesPlan(Integer projetId, List<ActivitePlanLigneDto> lignes) {
        List<Activite> existantes = activiteRepository.findByIdProjetOrderByOrdreSequentielAsc(projetId);
        java.util.Set<Integer> idsConserves = new java.util.HashSet<>();

        if (lignes != null) {
            short ordre = 1;
            for (ActivitePlanLigneDto ligne : lignes) {
                if (ligne == null || ligne.getLibelle() == null || ligne.getLibelle().isBlank()) {
                    continue;
                }
                Activite activite = activiteRepository.findByIdProjetAndOrdreSequentiel(projetId, ordre)
                        .orElseGet(Activite::new);
                activite.setIdProjet(projetId);
                activite.setOrdreSequentiel(ordre);
                activite.setLibelle(ligne.getLibelle());
                activite.setUniteResultat(ligne.getUniteResultat());
                activite.setQuantiteResultatAttendu(ligne.getQuantiteResultatAttendu());
                activite.setCoutUnitaire(ligne.getCoutUnitaire());
                activite.setCoutTotal(ligne.getCoutUnitaire() != null && ligne.getQuantiteResultatAttendu() != null
                        ? ligne.getCoutUnitaire().multiply(ligne.getQuantiteResultatAttendu())
                        : null);
                activite.setRessourcesNationales(ligne.getRessourcesNationales());
                activite.setRessourcesExternes(ligne.getRessourcesExternes());
                activiteRepository.save(activite);
                idsConserves.add(activite.getId());

                activitePlanificationAnnuelleRepository.deleteByIdActivite(activite.getId());
                saveAnneeActivite(activite.getId(), (short) 1, ligne.getCoutAnnee1(), ligne.getDureeAnnee1());
                saveAnneeActivite(activite.getId(), (short) 2, ligne.getCoutAnnee2(), ligne.getDureeAnnee2());
                saveAnneeActivite(activite.getId(), (short) 3, ligne.getCoutAnnee3(), ligne.getDureeAnnee3());
                saveAnneeActivite(activite.getId(), (short) 4, ligne.getCoutAnnee4(), ligne.getDureeAnnee4());
                saveAnneeActivite(activite.getId(), (short) 5, ligne.getCoutAnnee5(), ligne.getDureeAnnee5());
                ordre++;
            }
        }

        for (Activite existante : existantes) {
            if (idsConserves.contains(existante.getId())) {
                continue;
            }
            boolean referencee = bilanAvancementPhysiqueRepository.existsByIdActivite(existante.getId());
            if (referencee) {
                continue;
            }
            activitePlanificationAnnuelleRepository.deleteByIdActivite(existante.getId());
            activiteRepository.delete(existante);
        }
    }

    private void saveAnneeActivite(Integer idActivite, short annee, BigDecimal montant, String duree) {
        if (montant == null && (duree == null || duree.isBlank())) {
            return;
        }
        ActivitePlanificationAnnuelle a = new ActivitePlanificationAnnuelle();
        a.setIdActivite(idActivite);
        a.setAnneeNumero(annee);
        a.setCoutAnnee(montant);
        a.setDureeAnnee(duree);
        activitePlanificationAnnuelleRepository.save(a);
    }

    /**
     * § 28 de la FIOP : "Intrants — Budget du projet par phases". Les 5 "Voies et Moyens" sont des
     * libellés fixes (pas une table de référence) : on les recrée à l'identique à chaque
     * enregistrement, un peu comme un calendrier annuel mais avec des colonnes "phase" au lieu
     * d'années.
     */
    static final String[] VOIES_MOYENS_BUDGET = {
        "1-Dépenses de Personnels",
        "2-Serv. et Charges Diverses",
        "3-Achat Biens Consom. et Petit Matériel",
        "4-Immobilisations Corporelles",
        "5-Immobilisations Incorporelles",
    };

    private void applyIntrantsBudgetPhases(Integer projetId, List<IntrantBudgetPhaseLigneDto> lignes) {
        intrantBudgetPhaseRepository.deleteByIdProjet(projetId);
        if (lignes == null) {
            return;
        }
        for (IntrantBudgetPhaseLigneDto ligne : lignes) {
            if (ligne == null || ligne.getVoieMoyen() == null || ligne.getVoieMoyen().isBlank()) {
                continue;
            }
            saveIntrantPhase(projetId, ligne.getVoieMoyen(), "Elaboration", ligne.getMontantElaboration());
            saveIntrantPhase(projetId, ligne.getVoieMoyen(), "Planification", ligne.getMontantPlanification());
            saveIntrantPhase(projetId, ligne.getVoieMoyen(), "Exécution", ligne.getMontantExecution());
            saveIntrantPhase(projetId, ligne.getVoieMoyen(), "Evaluation d'Impact", ligne.getMontantEvaluationImpact());
        }
    }

    private void saveIntrantPhase(Integer projetId, String voieMoyen, String phase, BigDecimal montant) {
        if (montant == null) {
            return;
        }
        ProjetIntrantBudgetPhase p = new ProjetIntrantBudgetPhase();
        p.setIdProjet(projetId);
        p.setVoieMoyen(voieMoyen);
        p.setPhase(phase);
        p.setMontant(montant);
        intrantBudgetPhaseRepository.save(p);
    }

    /** § 34 de la FIOP : calendrier des dépenses prévisionnelles annuelles, par rubrique. */
    private void applyCalendrierRubriques(Integer projetId, List<RubriqueCalendrierLigneDto> lignes) {
        calendrierDepenseAnnuelleRepository.deleteByIdProjet(projetId);
        calendrierOrdreActivitesRepository.deleteByIdProjet(projetId);
        if (lignes == null) {
            return;
        }
        for (RubriqueCalendrierLigneDto ligne : lignes) {
            if (ligne == null || ligne.getIdRubrique() == null) {
                continue;
            }
            saveAnneeRubrique(projetId, ligne.getIdRubrique(), (short) 1, ligne.getMontantAnnee1());
            saveAnneeRubrique(projetId, ligne.getIdRubrique(), (short) 2, ligne.getMontantAnnee2());
            saveAnneeRubrique(projetId, ligne.getIdRubrique(), (short) 3, ligne.getMontantAnnee3());
            saveAnneeRubrique(projetId, ligne.getIdRubrique(), (short) 4, ligne.getMontantAnnee4());
            saveAnneeRubrique(projetId, ligne.getIdRubrique(), (short) 5, ligne.getMontantAnnee5());

            if (ligne.getOrdreActivites() != null && !ligne.getOrdreActivites().isBlank()) {
                ProjetCalendrierRubriqueOrdreActivites o = new ProjetCalendrierRubriqueOrdreActivites();
                o.setIdProjet(projetId);
                o.setIdRubrique(ligne.getIdRubrique());
                o.setOrdreActivites(ligne.getOrdreActivites());
                calendrierOrdreActivitesRepository.save(o);
            }
        }
    }

    /** § 35 de la FIOP : coûts récurrents du projet (budget de fonctionnement post-livrable), par rubrique principale. */
    private void applyCoutsRecurrents(Integer projetId, List<CoutRecurrentLigneDto> lignes) {
        coutRecurrentRepository.deleteByIdProjet(projetId);
        if (lignes == null) {
            return;
        }
        for (CoutRecurrentLigneDto ligne : lignes) {
            if (ligne == null || ligne.getIdRubrique() == null) {
                continue;
            }
            saveAnneeCoutRecurrent(projetId, ligne.getIdRubrique(), (short) 1, ligne.getMontantAnnee1());
            saveAnneeCoutRecurrent(projetId, ligne.getIdRubrique(), (short) 2, ligne.getMontantAnnee2());
            saveAnneeCoutRecurrent(projetId, ligne.getIdRubrique(), (short) 3, ligne.getMontantAnnee3());
            saveAnneeCoutRecurrent(projetId, ligne.getIdRubrique(), (short) 4, ligne.getMontantAnnee4());
            saveAnneeCoutRecurrent(projetId, ligne.getIdRubrique(), (short) 5, ligne.getMontantAnnee5());
        }
    }

    private void saveAnneeCoutRecurrent(Integer projetId, Integer idRubrique, short annee, BigDecimal montant) {
        if (montant == null) {
            return;
        }
        ProjetCoutRecurrent c = new ProjetCoutRecurrent();
        c.setIdProjet(projetId);
        c.setIdRubrique(idRubrique);
        c.setAnneeNumero(annee);
        c.setMontant(montant);
        coutRecurrentRepository.save(c);
    }

    private void saveAnneeRubrique(Integer projetId, Integer idRubrique, short annee, BigDecimal montant) {
        if (montant == null) {
            return;
        }
        ProjetCalendrierDepenseAnnuelle c = new ProjetCalendrierDepenseAnnuelle();
        c.setIdProjet(projetId);
        c.setIdRubrique(idRubrique);
        c.setAnneeNumero(annee);
        c.setMontantPrevisionnel(montant);
        calendrierDepenseAnnuelleRepository.save(c);
    }

    /** § 29 de la FIOP : sources de financement du projet (+ Programme Triennal d'Investissement). */
    private void applySourcesFinancement(Integer projetId, List<SourceFinancementPlanLigneDto> lignes) {
        projetFinancementRepository.deleteByIdProjet(projetId);
        if (lignes == null) {
            return;
        }
        for (SourceFinancementPlanLigneDto ligne : lignes) {
            if (ligne == null || ligne.getIdSource() == null) {
                continue;
            }
            ProjetFinancement f = new ProjetFinancement();
            f.setIdProjet(projetId);
            f.setIdSource(ligne.getIdSource());
            f.setPrevisionTotal(ligne.getPrevisionTotal() != null ? ligne.getPrevisionTotal() : BigDecimal.ZERO);
            f.setPtiAnnee1(ligne.getPtiAnnee1());
            f.setPtiAnnee2(ligne.getPtiAnnee2());
            f.setPtiAnnee3(ligne.getPtiAnnee3());
            f.setPtiAnnee4(ligne.getPtiAnnee4());
            f.setPtiAnnee5(ligne.getPtiAnnee5());
            projetFinancementRepository.save(f);
        }
    }

    private interface OrderedItemSaver {
        void save(String description, short ordre);
    }

    private void saveOrderedList(List<String> descriptions, OrderedItemSaver saver) {
        if (descriptions == null) {
            return;
        }
        short ordre = 1;
        for (String description : descriptions) {
            if (description == null || description.isBlank()) {
                continue;
            }
            saver.save(description, ordre++);
        }
    }

    private void savePopulation(Integer projetId, String categorie, Integer nombre) {
        if (nombre == null) {
            return;
        }
        PopulationVisee p = new PopulationVisee();
        p.setIdProjet(projetId);
        p.setCategorie(categorie);
        p.setNombre(nombre);
        populationViseeRepository.save(p);
    }

    private void saveEmploi(Integer projetId, String phase, String sexe, Integer nombre) {
        if (nombre == null) {
            return;
        }
        EmploiCree e = new EmploiCree();
        e.setIdProjet(projetId);
        e.setPhase(phase);
        e.setSexe(sexe);
        e.setNombre(nombre);
        emploiCreeRepository.save(e);
    }

    private void saveIntervenant(Integer projetId, String role, IntervenantDto dto) {
        if (dto == null || dto.isEmpty()) {
            return;
        }
        Intervenant i = new Intervenant();
        i.setIdProjet(projetId);
        i.setRole(role);
        i.setNomCharge(dto.getNomCharge());
        i.setTelephone(dto.getTelephone());
        i.setCourriel(dto.getCourriel());
        intervenantRepository.save(i);
    }

    private FiopResponse toResponse(Projet projet) {
        FiopResponse r = new FiopResponse();
        r.setId(projet.getId());
        r.setTitre(projet.getTitre());
        r.setCodeProjet(projet.getCodeProjet());
        r.setCodeInternePip(projet.getCodeInternePip());
        r.setDateInscription(projet.getDateInscription());
        r.setDureeTotaleMois(projet.getDureeTotaleMois());
        r.setCoutTotalGourde(projet.getCoutTotalGourde());
        r.setNomChargeProjet(projet.getNomChargeProjet());
        r.setTelephoneChargeProjet(projet.getTelephoneChargeProjet());
        r.setCourrielChargeProjet(projet.getCourrielChargeProjet());
        r.setJustification(projet.getJustification());
        r.setEffetsAttendus(projet.getEffetsAttendus());

        r.setIdDepartement(projet.getIdDepartement());
        r.setDepartementLibelle(projet.getIdDepartement() == null ? null
                : departementRepository.findById(projet.getIdDepartement()).map(d -> d.getLibelle()).orElse(null));
        r.setIdArrondissement(projet.getIdArrondissement());
        r.setArrondissementLibelle(projet.getIdArrondissement() == null ? null
                : arrondissementRepository.findById(projet.getIdArrondissement()).map(a -> a.getLibelle()).orElse(null));
        r.setIdCommune(projet.getIdCommune());
        r.setCommuneLibelle(projet.getIdCommune() == null ? null
                : communeRepository.findById(projet.getIdCommune()).map(c -> c.getLibelle()).orElse(null));
        r.setIdSectionCommunale(projet.getIdSectionCommunale());
        r.setSectionCommunaleLibelle(projet.getIdSectionCommunale() == null ? null
                : sectionCommunaleRepository.findById(projet.getIdSectionCommunale()).map(s -> s.getLibelle()).orElse(null));
        r.setHabitationLocaliteQuartier(projet.getHabitationLocaliteQuartier());
        r.setLocalisationGps(projet.getLocalisationGps());

        r.setCodeExterneBailleur(projet.getCodeExterneBailleur());
        r.setIdGrandChantier(projet.getIdGrandChantier());
        r.setGrandChantierLibelle(projet.getIdGrandChantier() == null ? null
                : grandChantierRepository.findById(projet.getIdGrandChantier()).map(g -> g.getLibelle()).orElse(null));
        r.setIdProgramme(projet.getIdProgramme());
        r.setProgrammeLibelle(projet.getIdProgramme() == null ? null
                : programmeRepository.findById(projet.getIdProgramme()).map(p -> p.getLibelle()).orElse(null));
        r.setIdSousProgramme(projet.getIdSousProgramme());
        r.setSousProgrammeLibelle(projet.getIdSousProgramme() == null ? null
                : sousProgrammeRepository.findById(projet.getIdSousProgramme()).map(s -> s.getLibelle()).orElse(null));
        r.setIdExerciceCreation(projet.getIdExerciceCreation());
        r.setExerciceLibelle(projet.getIdExerciceCreation() == null ? null
                : exerciceBudgetaireRepository.findById(projet.getIdExerciceCreation()).map(e -> e.getLibelle()).orElse(null));
        r.setProjetPsdh(projet.getProjetPsdh());
        r.setCiblePrioritaireGvt(projet.getCiblePrioritaireGvt());
        r.setEchelonTerritorial(projet.getEchelonTerritorial());
        r.setMinistereTutelle(projet.getMinistereTutelle());
        r.setPhasesActuelles(phaseActuelleProjetRepository.findByIdProjetOrderByDateChangementAsc(projet.getId())
                .stream()
                .map(p -> {
                    PhaseActuelleLigneDto dto = new PhaseActuelleLigneDto();
                    dto.setPhase(p.getPhase());
                    dto.setPeriode(p.getPeriode());
                    return dto;
                })
                .collect(Collectors.toList()));

        r.setAspectsLegaux(aspectLegalRepository.findByIdProjetOrderByOrdreAsc(projet.getId()).stream()
                .map(AspectLegal::getDescription).collect(Collectors.toList()));
        r.setAspectsInstitutionnels(aspectInstitutionnelRepository.findByIdProjetOrderByOrdreAsc(projet.getId()).stream()
                .map(AspectInstitutionnel::getDescription).collect(Collectors.toList()));
        r.setIndicateursResultats(indicateurResultatRepository.findByIdProjetOrderByOrdreAsc(projet.getId()).stream()
                .map(IndicateurResultat::getDescription).collect(Collectors.toList()));
        r.setExtrants(extrantRepository.findByIdProjetOrderByOrdreAsc(projet.getId()).stream()
                .map(Extrant::getDescription).collect(Collectors.toList()));
        r.setIntrantsBudgetPhases(buildIntrantsBudgetPhasesResponse(projet.getId()));

        List<PopulationVisee> population = populationViseeRepository.findByIdProjet(projet.getId());
        r.setPopulationEnfantsGarcons(findByCategorie(population, "Enfants garçons"));
        r.setPopulationEnfantsFilles(findByCategorie(population, "Enfants filles"));
        r.setPopulationHommes(findByCategorie(population, "Hommes"));
        r.setPopulationFemmes(findByCategorie(population, "Femmes"));

        List<EmploiCree> emplois = emploiCreeRepository.findByIdProjet(projet.getId());
        r.setEmploiPendantHomme(findEmploi(emplois, "Pendant", "Homme"));
        r.setEmploiPendantFemme(findEmploi(emplois, "Pendant", "Femme"));
        r.setEmploiApresHomme(findEmploi(emplois, "Apres", "Homme"));
        r.setEmploiApresFemme(findEmploi(emplois, "Apres", "Femme"));

        List<Intervenant> intervenants = intervenantRepository.findByIdProjet(projet.getId());
        r.setSupervision(findIntervenant(intervenants, "Supervision"));
        r.setExecution(findIntervenant(intervenants, "Execution"));
        r.setBailleur(findIntervenant(intervenants, "Bailleur"));
        r.setAgence1(findIntervenant(intervenants, "Agence1"));
        r.setAgence2(findIntervenant(intervenants, "Agence2"));

        r.setActivites(buildActivitesResponse(projet.getId()));
        r.setCalendrierRubriques(buildCalendrierRubriquesResponse(projet.getId()));
        r.setCoutsRecurrents(buildCoutsRecurrentsResponse(projet.getId()));
        r.setSourcesFinancement(buildSourcesFinancementResponse(projet.getId()));

        r.setStatut(projet.getStatut().name());
        r.setMotifRejet(projet.getMotifRejet());
        r.setIdUtilisateurCreation(projet.getIdUtilisateurCreation());
        r.setCreeParUsername(projet.getIdUtilisateurCreation() == null ? null
                : userRepository.findById(projet.getIdUtilisateurCreation()).map(User::getUsername).orElse(null));
        r.setDateCreation(projet.getDateCreation());
        r.setDateModification(projet.getDateModification());
        return r;
    }

    private Integer findByCategorie(List<PopulationVisee> list, String categorie) {
        return list.stream()
                .filter(p -> p.getCategorie().equals(categorie))
                .map(PopulationVisee::getNombre)
                .findFirst().orElse(null);
    }

    private Integer findEmploi(List<EmploiCree> list, String phase, String sexe) {
        return list.stream()
                .filter(e -> e.getPhase().equals(phase) && e.getSexe().equals(sexe))
                .map(EmploiCree::getNombre)
                .findFirst().orElse(null);
    }

    private IntervenantDto findIntervenant(List<Intervenant> list, String role) {
        return list.stream()
                .filter(i -> i.getRole().equals(role))
                .max(Comparator.comparing(Intervenant::getId))
                .map(i -> new IntervenantDto(i.getNomCharge(), i.getTelephone(), i.getCourriel()))
                .orElse(null);
    }

    private List<ActivitePlanLigneDto> buildActivitesResponse(Integer projetId) {
        return activiteRepository.findByIdProjetOrderByOrdreSequentielAsc(projetId).stream().map(a -> {
            ActivitePlanLigneDto dto = new ActivitePlanLigneDto();
            dto.setId(a.getId());
            dto.setLibelle(a.getLibelle());
            dto.setUniteResultat(a.getUniteResultat());
            dto.setQuantiteResultatAttendu(a.getQuantiteResultatAttendu());
            dto.setCoutUnitaire(a.getCoutUnitaire());
            dto.setCoutTotal(a.getCoutTotal());
            dto.setRessourcesNationales(a.getRessourcesNationales());
            dto.setRessourcesExternes(a.getRessourcesExternes());
            for (ActivitePlanificationAnnuelle an : activitePlanificationAnnuelleRepository
                    .findByIdActiviteOrderByAnneeNumeroAsc(a.getId())) {
                switch (an.getAnneeNumero()) {
                    case 1 -> { dto.setCoutAnnee1(an.getCoutAnnee()); dto.setDureeAnnee1(an.getDureeAnnee()); }
                    case 2 -> { dto.setCoutAnnee2(an.getCoutAnnee()); dto.setDureeAnnee2(an.getDureeAnnee()); }
                    case 3 -> { dto.setCoutAnnee3(an.getCoutAnnee()); dto.setDureeAnnee3(an.getDureeAnnee()); }
                    case 4 -> { dto.setCoutAnnee4(an.getCoutAnnee()); dto.setDureeAnnee4(an.getDureeAnnee()); }
                    case 5 -> { dto.setCoutAnnee5(an.getCoutAnnee()); dto.setDureeAnnee5(an.getDureeAnnee()); }
                    default -> { }
                }
            }
            return dto;
        }).toList();
    }

    private List<RubriqueCalendrierLigneDto> buildCalendrierRubriquesResponse(Integer projetId) {
        java.util.Map<Integer, RubriqueCalendrierLigneDto> parRubrique = new java.util.LinkedHashMap<>();
        for (ProjetCalendrierDepenseAnnuelle l : calendrierDepenseAnnuelleRepository.findByIdProjet(projetId)) {
            RubriqueCalendrierLigneDto dto = parRubrique.computeIfAbsent(l.getIdRubrique(), id -> {
                RubriqueCalendrierLigneDto d = new RubriqueCalendrierLigneDto();
                d.setIdRubrique(id);
                rubriqueBudgetaireRepository.findById(id).ifPresent(r -> d.setRubriqueLibelle(r.getLibelle()));
                return d;
            });
            switch (l.getAnneeNumero()) {
                case 1 -> dto.setMontantAnnee1(l.getMontantPrevisionnel());
                case 2 -> dto.setMontantAnnee2(l.getMontantPrevisionnel());
                case 3 -> dto.setMontantAnnee3(l.getMontantPrevisionnel());
                case 4 -> dto.setMontantAnnee4(l.getMontantPrevisionnel());
                case 5 -> dto.setMontantAnnee5(l.getMontantPrevisionnel());
                default -> { }
            }
        }
        for (ProjetCalendrierRubriqueOrdreActivites o : calendrierOrdreActivitesRepository.findByIdProjet(projetId)) {
            RubriqueCalendrierLigneDto dto = parRubrique.computeIfAbsent(o.getIdRubrique(), id -> {
                RubriqueCalendrierLigneDto d = new RubriqueCalendrierLigneDto();
                d.setIdRubrique(id);
                rubriqueBudgetaireRepository.findById(id).ifPresent(r -> d.setRubriqueLibelle(r.getLibelle()));
                return d;
            });
            dto.setOrdreActivites(o.getOrdreActivites());
        }
        for (RubriqueCalendrierLigneDto dto : parRubrique.values()) {
            dto.setMontantTotal(sumNullable(dto.getMontantAnnee1(), dto.getMontantAnnee2(),
                    dto.getMontantAnnee3(), dto.getMontantAnnee4(), dto.getMontantAnnee5()));
        }
        return new java.util.ArrayList<>(parRubrique.values());
    }

    private List<CoutRecurrentLigneDto> buildCoutsRecurrentsResponse(Integer projetId) {
        java.util.Map<Integer, CoutRecurrentLigneDto> parRubrique = new java.util.LinkedHashMap<>();
        for (ProjetCoutRecurrent l : coutRecurrentRepository.findByIdProjet(projetId)) {
            CoutRecurrentLigneDto dto = parRubrique.computeIfAbsent(l.getIdRubrique(), id -> {
                CoutRecurrentLigneDto d = new CoutRecurrentLigneDto();
                d.setIdRubrique(id);
                rubriqueBudgetaireRepository.findById(id).ifPresent(r -> d.setRubriqueLibelle(r.getLibelle()));
                return d;
            });
            switch (l.getAnneeNumero()) {
                case 1 -> dto.setMontantAnnee1(l.getMontant());
                case 2 -> dto.setMontantAnnee2(l.getMontant());
                case 3 -> dto.setMontantAnnee3(l.getMontant());
                case 4 -> dto.setMontantAnnee4(l.getMontant());
                case 5 -> dto.setMontantAnnee5(l.getMontant());
                default -> { }
            }
        }
        for (CoutRecurrentLigneDto dto : parRubrique.values()) {
            dto.setMontantTotal(sumNullable(dto.getMontantAnnee1(), dto.getMontantAnnee2(),
                    dto.getMontantAnnee3(), dto.getMontantAnnee4(), dto.getMontantAnnee5()));
        }
        return new java.util.ArrayList<>(parRubrique.values());
    }

    private List<IntrantBudgetPhaseLigneDto> buildIntrantsBudgetPhasesResponse(Integer projetId) {
        java.util.Map<String, IntrantBudgetPhaseLigneDto> parVoie = new java.util.LinkedHashMap<>();
        for (String voieMoyen : VOIES_MOYENS_BUDGET) {
            IntrantBudgetPhaseLigneDto dto = new IntrantBudgetPhaseLigneDto();
            dto.setVoieMoyen(voieMoyen);
            parVoie.put(voieMoyen, dto);
        }
        for (ProjetIntrantBudgetPhase l : intrantBudgetPhaseRepository.findByIdProjet(projetId)) {
            IntrantBudgetPhaseLigneDto dto = parVoie.get(l.getVoieMoyen());
            if (dto == null) {
                continue;
            }
            switch (l.getPhase()) {
                case "Elaboration" -> dto.setMontantElaboration(l.getMontant());
                case "Planification" -> dto.setMontantPlanification(l.getMontant());
                case "Exécution" -> dto.setMontantExecution(l.getMontant());
                case "Evaluation d'Impact" -> dto.setMontantEvaluationImpact(l.getMontant());
                default -> { }
            }
        }
        for (IntrantBudgetPhaseLigneDto dto : parVoie.values()) {
            dto.setMontantTotal(sumNullable(dto.getMontantElaboration(), dto.getMontantPlanification(),
                    dto.getMontantExecution(), dto.getMontantEvaluationImpact()));
        }
        return new java.util.ArrayList<>(parVoie.values());
    }

    private List<SourceFinancementPlanLigneDto> buildSourcesFinancementResponse(Integer projetId) {
        List<ProjetFinancement> lignes = projetFinancementRepository.findByIdProjet(projetId);
        BigDecimal total = lignes.stream()
                .map(ProjetFinancement::getPrevisionTotal)
                .filter(java.util.Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return lignes.stream().map(f -> {
            SourceFinancementPlanLigneDto dto = new SourceFinancementPlanLigneDto();
            dto.setIdSource(f.getIdSource());
            sourceFinancementRepository.findById(f.getIdSource()).ifPresent(s -> dto.setSourceLibelle(s.getLibelle()));
            dto.setPrevisionTotal(f.getPrevisionTotal());
            dto.setPtiAnnee1(f.getPtiAnnee1());
            dto.setPtiAnnee2(f.getPtiAnnee2());
            dto.setPtiAnnee3(f.getPtiAnnee3());
            dto.setPtiAnnee4(f.getPtiAnnee4());
            dto.setPtiAnnee5(f.getPtiAnnee5());
            if (total.compareTo(BigDecimal.ZERO) > 0 && f.getPrevisionTotal() != null) {
                dto.setPoidsPct(f.getPrevisionTotal()
                        .divide(total, 4, java.math.RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100))
                        .setScale(2, java.math.RoundingMode.HALF_UP));
            }
            return dto;
        }).toList();
    }

    private String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value;
    }

    private BigDecimal sumNullable(BigDecimal... values) {
        BigDecimal sum = null;
        for (BigDecimal v : values) {
            if (v == null) {
                continue;
            }
            sum = sum == null ? v : sum.add(v);
        }
        return sum;
    }
}
