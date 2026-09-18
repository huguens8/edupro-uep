-- =====================================================================
-- FIOP - Fiche d'Identification et d'Opérations de Projets
-- MENFP / UEP (Unité d'Etudes et de Programmation) - FIOP Provisoire v5.0
-- Schéma PostgreSQL généré à partir du canevas Excel "CANEVAS FIOP.xls"
--
-- Structure du document source (3 parties) :
--   PARTIE 1 : Informations Générales   (identification, budget, activités)
--   PARTIE 2 : Bilan Physique et Financier (exécution / avancement)
--   PARTIE 3 : Tranche Annuelle du projet  (exercice courant)
--
-- À exécuter sur la base "edupro_uep" (voir application.properties) :
--   psql -U postgres -d edupro_uep -f src/main/resources/db/fiop_schema.sql
-- =====================================================================

-- =====================================================================
-- 0. RÉFÉRENTIELS / TABLES DE LOOKUP
-- =====================================================================

CREATE TABLE IF NOT EXISTS departement (
    id      BIGSERIAL PRIMARY KEY,
    nom     VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS arrondissement (
    id              BIGSERIAL PRIMARY KEY,
    nom             VARCHAR(100) NOT NULL,
    departement_id  BIGINT NOT NULL REFERENCES departement(id) ON DELETE RESTRICT,
    UNIQUE (departement_id, nom)
);

CREATE TABLE IF NOT EXISTS commune (
    id                  BIGSERIAL PRIMARY KEY,
    nom                 VARCHAR(100) NOT NULL,
    arrondissement_id   BIGINT NOT NULL REFERENCES arrondissement(id) ON DELETE RESTRICT,
    UNIQUE (arrondissement_id, nom)
);

CREATE TABLE IF NOT EXISTS section_communale (
    id          BIGSERIAL PRIMARY KEY,
    nom         VARCHAR(100) NOT NULL,
    commune_id  BIGINT NOT NULL REFERENCES commune(id) ON DELETE RESTRICT,
    UNIQUE (commune_id, nom)
);

-- Champ 5 : GRAND CHANTIER (ex: "Réfondation Sociale")
CREATE TABLE IF NOT EXISTS grand_chantier (
    id  BIGSERIAL PRIMARY KEY,
    nom VARCHAR(150) NOT NULL UNIQUE
);

-- Champ 6 : PROGRAMME
CREATE TABLE IF NOT EXISTS programme (
    id  BIGSERIAL PRIMARY KEY,
    nom VARCHAR(255) NOT NULL UNIQUE
);

-- Champ 7 : SOUS-PROGRAMME
CREATE TABLE IF NOT EXISTS sous_programme (
    id           BIGSERIAL PRIMARY KEY,
    nom          VARCHAR(255) NOT NULL,
    programme_id BIGINT NOT NULL REFERENCES programme(id) ON DELETE RESTRICT,
    UNIQUE (programme_id, nom)
);

-- Champ 8 : PROJETS PSDH (Plan Stratégique de Développement d'Haïti)
CREATE TABLE IF NOT EXISTS projet_psdh (
    id     BIGSERIAL PRIMARY KEY,
    code   VARCHAR(50),
    titre  VARCHAR(255) NOT NULL
);

-- Nomenclature des dépenses budgétaires de l'État (rubriques / sous-rubriques)
-- utilisée dans les sections 28, 34, 35, 41, 44, 45, 47, 48
CREATE TABLE IF NOT EXISTS rubrique_budgetaire (
    id        BIGSERIAL PRIMARY KEY,
    code      VARCHAR(10) NOT NULL UNIQUE,
    libelle   VARCHAR(255) NOT NULL,
    parent_id BIGINT REFERENCES rubrique_budgetaire(id) ON DELETE CASCADE
);

-- Sources de financement (section 29 et suivantes)
CREATE TABLE IF NOT EXISTS source_financement (
    id         BIGSERIAL PRIMARY KEY,
    nom        VARCHAR(100) NOT NULL UNIQUE,
    categorie  VARCHAR(20) NOT NULL CHECK (categorie IN ('NATIONALE', 'EXTERNE')),
    type       VARCHAR(20) NOT NULL CHECK (type IN ('PUBLIC', 'PRET', 'DON', 'ONG'))
);

-- =====================================================================
-- 1. PARTIE 1 - IDENTIFICATION DU PROJET (table centrale)
-- =====================================================================

CREATE TABLE IF NOT EXISTS projet (
    id                              BIGSERIAL PRIMARY KEY,

    -- Champs 1 à 4
    exercice                        VARCHAR(20) NOT NULL,
    date_inscription                DATE,
    derniere_mise_a_jour            DATE,
    code_externe_bailleur           VARCHAR(50),
    code_interne_pip                VARCHAR(50) UNIQUE,

    -- Titre (Action + Nature de l'Action + Localisation)
    titre                           TEXT NOT NULL,

    -- Champs 5 à 8 : classification
    grand_chantier_id               BIGINT REFERENCES grand_chantier(id) ON DELETE SET NULL,
    programme_id                    BIGINT REFERENCES programme(id) ON DELETE SET NULL,
    sous_programme_id               BIGINT REFERENCES sous_programme(id) ON DELETE SET NULL,
    projet_psdh_id                  BIGINT REFERENCES projet_psdh(id) ON DELETE SET NULL,

    -- Champs 9 à 10
    cible_prioritaire_gouvernement  BOOLEAN,
    justification                   TEXT,

    -- Champ 14 : ministère de tutelle / chargé de projet
    ministere_tutelle               VARCHAR(255) DEFAULT 'MENFP',
    nom_charge_projet               VARCHAR(255),
    telephone_charge_projet         VARCHAR(50),
    email_charge_projet             VARCHAR(255),

    -- Champ 15 : supervision
    supervision_firme               VARCHAR(255),
    supervision_telephone           VARCHAR(50),
    supervision_courriel            VARCHAR(255),
    supervision_nom_charge          VARCHAR(255),

    -- Champ 16 : exécution
    execution_firme                 VARCHAR(255),
    execution_telephone             VARCHAR(50),
    execution_courriel              VARCHAR(255),
    execution_nom_charge            VARCHAR(255),

    -- Champ 20 à 22 : localisation
    echelon_territorial             VARCHAR(30) CHECK (echelon_territorial IN
                                        ('INTER_DEPARTEMENTAL', 'DEPARTEMENTAL', 'ARRONDISSEMENT',
                                         'COMMUNAL', 'INTER_COMMUNAL', 'SECTION_COMMUNALE')),
    localisation_gps                VARCHAR(100),
    departement_id                  BIGINT REFERENCES departement(id) ON DELETE SET NULL,
    arrondissement_id               BIGINT REFERENCES arrondissement(id) ON DELETE SET NULL,
    commune_id                      BIGINT REFERENCES commune(id) ON DELETE SET NULL,
    section_communale_id            BIGINT REFERENCES section_communale(id) ON DELETE SET NULL,
    localite                        VARCHAR(255),

    -- Champs 23 à 25
    duree_totale_mois               INTEGER,
    cout_total_gourde               NUMERIC(18,2) DEFAULT 0,
    effets_attendus                 TEXT,

    -- Champ 30 : phase actuelle du projet
    phase_actuelle                  VARCHAR(30) CHECK (phase_actuelle IN
                                        ('ELABORATION', 'PLANIFICATION', 'EXECUTION', 'EVALUATION_IMPACT')),
    periode_phase_actuelle          VARCHAR(50),

    -- Section 35 (onglet Bilan d'Exécution) : évolution temporelle
    date_demarrage_effective        DATE,
    date_achevement_prevue          DATE,

    -- Champs 36 à 37
    numero_compte_bancaire          VARCHAR(50),
    type_investissement             VARCHAR(20) CHECK (type_investissement IN
                                        ('ETUDE', 'EXECUTION', 'EVALUATION')),

    -- Traçabilité applicative
    cree_par_id                     BIGINT REFERENCES users(user_id) ON DELETE SET NULL,
    created_at                      TIMESTAMP NOT NULL DEFAULT now(),
    updated_at                      TIMESTAMP NOT NULL DEFAULT now()
);

CREATE INDEX IF NOT EXISTS idx_projet_code_interne_pip ON projet(code_interne_pip);
CREATE INDEX IF NOT EXISTS idx_projet_exercice ON projet(exercice);

-- Champ 11 : population visée (Enfants garçons/filles, Hommes, Femmes)
CREATE TABLE IF NOT EXISTS projet_population_cible (
    id          BIGSERIAL PRIMARY KEY,
    projet_id   BIGINT NOT NULL REFERENCES projet(id) ON DELETE CASCADE,
    categorie   VARCHAR(20) NOT NULL CHECK (categorie IN
                    ('ENFANTS_GARCONS', 'ENFANTS_FILLES', 'HOMMES', 'FEMMES')),
    nombre      INTEGER NOT NULL DEFAULT 0,
    UNIQUE (projet_id, categorie)
);

-- Champ 11 (suite) : nombre d'emplois créés (Homme/Femme x Pendant/Après)
CREATE TABLE IF NOT EXISTS projet_emploi_cree (
    id          BIGSERIAL PRIMARY KEY,
    projet_id   BIGINT NOT NULL REFERENCES projet(id) ON DELETE CASCADE,
    genre       VARCHAR(10) NOT NULL CHECK (genre IN ('HOMME', 'FEMME')),
    periode     VARCHAR(10) NOT NULL CHECK (periode IN ('PENDANT', 'APRES')),
    nombre      INTEGER NOT NULL DEFAULT 0,
    UNIQUE (projet_id, genre, periode)
);

-- Champ 12 : aspects légaux
CREATE TABLE IF NOT EXISTS projet_aspect_legal (
    id          BIGSERIAL PRIMARY KEY,
    projet_id   BIGINT NOT NULL REFERENCES projet(id) ON DELETE CASCADE,
    numero      INTEGER NOT NULL,
    description TEXT,
    UNIQUE (projet_id, numero)
);

-- Champ 13 : aspects institutionnels
CREATE TABLE IF NOT EXISTS projet_aspect_institutionnel (
    id          BIGSERIAL PRIMARY KEY,
    projet_id   BIGINT NOT NULL REFERENCES projet(id) ON DELETE CASCADE,
    numero      INTEGER NOT NULL,
    description TEXT,
    UNIQUE (projet_id, numero)
);

-- Champ 17 : bailleurs de fonds
CREATE TABLE IF NOT EXISTS projet_bailleur (
    id              BIGSERIAL PRIMARY KEY,
    projet_id       BIGINT NOT NULL REFERENCES projet(id) ON DELETE CASCADE,
    bailleur_pays   VARCHAR(255) NOT NULL,
    montant_tp      NUMERIC(18,2) DEFAULT 0
);

-- Champs 18-19 : agences partenaires (Agence 1, Agence 2)
CREATE TABLE IF NOT EXISTS projet_agence (
    id                  BIGSERIAL PRIMARY KEY,
    projet_id           BIGINT NOT NULL REFERENCES projet(id) ON DELETE CASCADE,
    rang                SMALLINT NOT NULL CHECK (rang IN (1, 2)),
    nom_representant    VARCHAR(255),
    telephone           VARCHAR(50),
    email               VARCHAR(255),
    personne_contact    VARCHAR(255),
    UNIQUE (projet_id, rang)
);

-- Champ 26 : indicateurs de résultats
CREATE TABLE IF NOT EXISTS projet_indicateur_resultat (
    id          BIGSERIAL PRIMARY KEY,
    projet_id   BIGINT NOT NULL REFERENCES projet(id) ON DELETE CASCADE,
    numero      INTEGER NOT NULL,
    description TEXT,
    UNIQUE (projet_id, numero)
);

-- Champ 27 : extrants (objectifs spécifiques du projet), jusqu'à 11 lignes
CREATE TABLE IF NOT EXISTS projet_extrant (
    id          BIGSERIAL PRIMARY KEY,
    projet_id   BIGINT NOT NULL REFERENCES projet(id) ON DELETE CASCADE,
    numero      INTEGER NOT NULL,
    description TEXT,
    UNIQUE (projet_id, numero)
);

-- Champ 28 : intrants - budget du projet par phase x rubrique budgétaire
CREATE TABLE IF NOT EXISTS projet_intrant_budget (
    id                      BIGSERIAL PRIMARY KEY,
    projet_id               BIGINT NOT NULL REFERENCES projet(id) ON DELETE CASCADE,
    rubrique_budgetaire_id  BIGINT NOT NULL REFERENCES rubrique_budgetaire(id) ON DELETE RESTRICT,
    phase                   VARCHAR(30) NOT NULL CHECK (phase IN
                                ('ELABORATION', 'PLANIFICATION', 'EXECUTION', 'EVALUATION_IMPACT')),
    montant                 NUMERIC(18,2) NOT NULL DEFAULT 0,
    UNIQUE (projet_id, rubrique_budgetaire_id, phase)
);

-- Champ 29 : sources de financement du projet (par PTI 1 à 5)
CREATE TABLE IF NOT EXISTS projet_financement (
    id                      BIGSERIAL PRIMARY KEY,
    projet_id               BIGINT NOT NULL REFERENCES projet(id) ON DELETE CASCADE,
    source_financement_id   BIGINT NOT NULL REFERENCES source_financement(id) ON DELETE RESTRICT,
    prevision_total         NUMERIC(18,2) DEFAULT 0,
    pti_1                   NUMERIC(18,2) DEFAULT 0,
    pti_2                   NUMERIC(18,2) DEFAULT 0,
    pti_3                   NUMERIC(18,2) DEFAULT 0,
    pti_4                   NUMERIC(18,2) DEFAULT 0,
    pti_5                   NUMERIC(18,2) DEFAULT 0,
    UNIQUE (projet_id, source_financement_id)
);

-- =====================================================================
-- 2. ACTIVITÉS DU PROJET (champs 31 à 33, réutilisées en Partie 2 et 3)
-- =====================================================================

-- Liste maîtresse des activités (# d'ordre séquentiel, jusqu'à 11-12 lignes)
CREATE TABLE IF NOT EXISTS activite (
    id                      BIGSERIAL PRIMARY KEY,
    projet_id               BIGINT NOT NULL REFERENCES projet(id) ON DELETE CASCADE,
    numero_ordre             INTEGER NOT NULL,
    libelle                 TEXT NOT NULL,
    unite                   VARCHAR(50),
    quantite                NUMERIC(14,2),
    cout_unitaire           NUMERIC(18,2),
    ressources_nationales   NUMERIC(18,2) DEFAULT 0,
    ressources_externes     NUMERIC(18,2) DEFAULT 0,
    UNIQUE (projet_id, numero_ordre)
);

CREATE INDEX IF NOT EXISTS idx_activite_projet ON activite(projet_id);

-- Champ 31 : coût prévisionnel de l'activité par année (Année 1 à 5)
CREATE TABLE IF NOT EXISTS activite_cout_annuel (
    id          BIGSERIAL PRIMARY KEY,
    activite_id BIGINT NOT NULL REFERENCES activite(id) ON DELETE CASCADE,
    annee_rang  SMALLINT NOT NULL CHECK (annee_rang BETWEEN 1 AND 5),
    montant     NUMERIC(18,2) NOT NULL DEFAULT 0,
    UNIQUE (activite_id, annee_rang)
);

-- Champ 32 : chronogramme d'exécution annuelle (durée de l'activité, Année 1 à 5)
CREATE TABLE IF NOT EXISTS activite_duree_annuelle (
    id          BIGSERIAL PRIMARY KEY,
    activite_id BIGINT NOT NULL REFERENCES activite(id) ON DELETE CASCADE,
    annee_rang  SMALLINT NOT NULL CHECK (annee_rang BETWEEN 1 AND 5),
    actif       BOOLEAN NOT NULL DEFAULT false,
    UNIQUE (activite_id, annee_rang)
);

-- =====================================================================
-- 3. DÉPENSES PRÉVISIONNELLES ET COÛTS RÉCURRENTS (champs 34 à 35)
-- =====================================================================

-- Champ 34 : calendrier des dépenses prévisionnelles annuelles par rubrique
CREATE TABLE IF NOT EXISTS projet_depense_previsionnelle (
    id                      BIGSERIAL PRIMARY KEY,
    projet_id               BIGINT NOT NULL REFERENCES projet(id) ON DELETE CASCADE,
    rubrique_budgetaire_id  BIGINT NOT NULL REFERENCES rubrique_budgetaire(id) ON DELETE RESTRICT,
    annee_rang              SMALLINT NOT NULL CHECK (annee_rang BETWEEN 1 AND 5),
    montant                 NUMERIC(18,2) NOT NULL DEFAULT 0,
    UNIQUE (projet_id, rubrique_budgetaire_id, annee_rang)
);

-- Champ 35 : coûts récurrents post-livrable (budget de fonctionnement)
CREATE TABLE IF NOT EXISTS projet_cout_recurrent (
    id                      BIGSERIAL PRIMARY KEY,
    projet_id               BIGINT NOT NULL REFERENCES projet(id) ON DELETE CASCADE,
    rubrique_budgetaire_id  BIGINT NOT NULL REFERENCES rubrique_budgetaire(id) ON DELETE RESTRICT,
    annee_rang              SMALLINT NOT NULL CHECK (annee_rang BETWEEN 1 AND 5),
    montant                 NUMERIC(18,2) NOT NULL DEFAULT 0,
    UNIQUE (projet_id, rubrique_budgetaire_id, annee_rang)
);

-- =====================================================================
-- 4. PARTIE 2 - BILAN PHYSIQUE ET FINANCIER (onglets Bilan d'Exécution,
--    Bilan Suite 1, Bilan Suite 2, Bilan suite 3)
-- =====================================================================

-- Champ 38 : programme d'investissement public (PIP) par année budgétaire
CREATE TABLE IF NOT EXISTS projet_pip_annuel (
    id                  BIGSERIAL PRIMARY KEY,
    projet_id           BIGINT NOT NULL REFERENCES projet(id) ON DELETE CASCADE,
    annee_rang          SMALLINT NOT NULL,
    exercice_fiscal     VARCHAR(20),
    budget_previsionnel NUMERIC(18,2) DEFAULT 0,
    budget_alloue       NUMERIC(18,2) DEFAULT 0,
    budget_reel         NUMERIC(18,2) DEFAULT 0,
    UNIQUE (projet_id, annee_rang)
);

-- Champ 39 : évolution financière du projet par source de financement
CREATE TABLE IF NOT EXISTS projet_evolution_financiere (
    id                      BIGSERIAL PRIMARY KEY,
    projet_id               BIGINT NOT NULL REFERENCES projet(id) ON DELETE CASCADE,
    source_financement_id   BIGINT NOT NULL REFERENCES source_financement(id) ON DELETE RESTRICT,
    prevision_total          NUMERIC(18,2) DEFAULT 0,
    decaissement_effectif    NUMERIC(18,2) DEFAULT 0,
    depense_effective        NUMERIC(18,2) DEFAULT 0,
    valeur_livres_comptables NUMERIC(18,2) DEFAULT 0,
    UNIQUE (projet_id, source_financement_id)
);

-- Champ 41 : résumé des opérations financières par rubrique budgétaire
CREATE TABLE IF NOT EXISTS projet_resume_operation (
    id                          BIGSERIAL PRIMARY KEY,
    projet_id                   BIGINT NOT NULL REFERENCES projet(id) ON DELETE CASCADE,
    rubrique_budgetaire_id      BIGINT NOT NULL REFERENCES rubrique_budgetaire(id) ON DELETE RESTRICT,
    cout_previsionnel           NUMERIC(18,2) DEFAULT 0,
    depense_anterieure_n2       NUMERIC(18,2) DEFAULT 0,
    depense_exercice_n2         NUMERIC(18,2) DEFAULT 0,
    UNIQUE (projet_id, rubrique_budgetaire_id)
);

-- Champ 42 : avancement physique des activités (résultats prévus/obtenus)
CREATE TABLE IF NOT EXISTS activite_avancement_physique (
    id                      BIGSERIAL PRIMARY KEY,
    activite_id             BIGINT NOT NULL UNIQUE REFERENCES activite(id) ON DELETE CASCADE,
    unite                   VARCHAR(50),
    quantite_prevue         NUMERIC(14,2) DEFAULT 0,
    resultat_anterieur_n2   NUMERIC(14,2) DEFAULT 0,
    resultat_exercice_n2    NUMERIC(14,2) DEFAULT 0
);

-- Champ 43 : avancement financier des activités (montant prévu / dépenses effectives)
CREATE TABLE IF NOT EXISTS activite_avancement_financier (
    id                      BIGSERIAL PRIMARY KEY,
    activite_id             BIGINT NOT NULL UNIQUE REFERENCES activite(id) ON DELETE CASCADE,
    montant_prevu           NUMERIC(18,2) DEFAULT 0,
    depense_anterieure_n2   NUMERIC(18,2) DEFAULT 0,
    depense_exercice_n2     NUMERIC(18,2) DEFAULT 0
);

-- Champs 44-45 : avancement du budget total sur financement national / externe
CREATE TABLE IF NOT EXISTS projet_avancement_budget (
    id                          BIGSERIAL PRIMARY KEY,
    projet_id                   BIGINT NOT NULL REFERENCES projet(id) ON DELETE CASCADE,
    rubrique_budgetaire_id      BIGINT NOT NULL REFERENCES rubrique_budgetaire(id) ON DELETE RESTRICT,
    categorie_financement       VARCHAR(10) NOT NULL CHECK (categorie_financement IN ('NATIONAL', 'EXTERNE')),
    montant_prevu                NUMERIC(18,2) DEFAULT 0,
    depense_anterieure_n2        NUMERIC(18,2) DEFAULT 0,
    depense_exercice_n2          NUMERIC(18,2) DEFAULT 0,
    UNIQUE (projet_id, rubrique_budgetaire_id, categorie_financement)
);

-- =====================================================================
-- 5. PARTIE 3 - TRANCHE ANNUELLE DU PROJET (onglets Annuelle, Activités,
--    Chronogramme, R.dépenses, Calendrier)
-- =====================================================================

-- Champ 44 (onglet Annuelle) : dépenses prévisionnelles de l'exercice courant
CREATE TABLE IF NOT EXISTS projet_tranche_depense_previsionnelle (
    id                      BIGSERIAL PRIMARY KEY,
    projet_id               BIGINT NOT NULL REFERENCES projet(id) ON DELETE CASCADE,
    source_financement_id   BIGINT NOT NULL REFERENCES source_financement(id) ON DELETE RESTRICT,
    prevision_total         NUMERIC(18,2) DEFAULT 0,
    UNIQUE (projet_id, source_financement_id)
);

-- Champ 45 (onglet Activités) : coût d'exécution des activités de la tranche
-- annuelle en cours (ré-estimation de l'exercice courant pour chaque activité)
CREATE TABLE IF NOT EXISTS activite_tranche_annuelle (
    id                      BIGSERIAL PRIMARY KEY,
    activite_id             BIGINT NOT NULL UNIQUE REFERENCES activite(id) ON DELETE CASCADE,
    unite                   VARCHAR(50),
    quantite                NUMERIC(14,2) DEFAULT 0,
    cout_unitaire           NUMERIC(18,2) DEFAULT 0,
    ressources_nationales   NUMERIC(18,2) DEFAULT 0,
    ressources_externes     NUMERIC(18,2) DEFAULT 0
);

-- Champ 46 (onglet Chronogramme) : chronogramme mensuel d'exécution
-- (mois 1 à 12 = OCT. à SEPT., année fiscale haïtienne)
CREATE TABLE IF NOT EXISTS activite_chronogramme_mensuel (
    id          BIGSERIAL PRIMARY KEY,
    activite_id BIGINT NOT NULL REFERENCES activite(id) ON DELETE CASCADE,
    mois_rang   SMALLINT NOT NULL CHECK (mois_rang BETWEEN 1 AND 12),
    planifie    BOOLEAN NOT NULL DEFAULT false,
    UNIQUE (activite_id, mois_rang)
);

-- Champ 47 (onglet R.dépenses) : voies et moyens - lignes budgétaires
-- détaillées par article budgétaire / activité
CREATE TABLE IF NOT EXISTS projet_ligne_budgetaire (
    id                      BIGSERIAL PRIMARY KEY,
    projet_id               BIGINT NOT NULL REFERENCES projet(id) ON DELETE CASCADE,
    activite_id             BIGINT REFERENCES activite(id) ON DELETE SET NULL,
    rubrique_budgetaire_id  BIGINT NOT NULL REFERENCES rubrique_budgetaire(id) ON DELETE RESTRICT,
    code_article            VARCHAR(20),
    designation             VARCHAR(255),
    unite_mesure            VARCHAR(50),
    quantite                NUMERIC(14,2) DEFAULT 0,
    cout_unitaire           NUMERIC(18,2) DEFAULT 0,
    montant_tresor_public   NUMERIC(18,2) DEFAULT 0,
    montant_afc             NUMERIC(18,2) DEFAULT 0,
    montant_fonds_propres   NUMERIC(18,2) DEFAULT 0,
    montant_bilateral       NUMERIC(18,2) DEFAULT 0,
    montant_multilateral    NUMERIC(18,2) DEFAULT 0
);

CREATE INDEX IF NOT EXISTS idx_ligne_budgetaire_projet ON projet_ligne_budgetaire(projet_id);

-- Champ 48 (onglet Calendrier) : calendrier prévisionnel d'utilisation des
-- ressources financières par trimestre
CREATE TABLE IF NOT EXISTS projet_calendrier_utilisation (
    id                      BIGSERIAL PRIMARY KEY,
    projet_id               BIGINT NOT NULL REFERENCES projet(id) ON DELETE CASCADE,
    rubrique_budgetaire_id  BIGINT NOT NULL REFERENCES rubrique_budgetaire(id) ON DELETE RESTRICT,
    categorie_financement   VARCHAR(10) NOT NULL CHECK (categorie_financement IN ('NATIONAL', 'EXTERNE')),
    trimestre               SMALLINT NOT NULL CHECK (trimestre BETWEEN 1 AND 4),
    montant                 NUMERIC(18,2) NOT NULL DEFAULT 0,
    UNIQUE (projet_id, rubrique_budgetaire_id, categorie_financement, trimestre)
);

-- =====================================================================
-- 6. DONNÉES DE RÉFÉRENCE (seed)
-- =====================================================================

-- Nomenclature des dépenses budgétaires de l'État (rubriques de niveau 1)
INSERT INTO rubrique_budgetaire (code, libelle, parent_id) VALUES
    ('1', 'Dépenses de Personnels', NULL),
    ('2', 'Services et Charges Diverses', NULL),
    ('3', 'Achat Biens Consommables et Petit Matériel', NULL),
    ('4', 'Immobilisations Corporelles', NULL),
    ('5', 'Immobilisations Incorporelles', NULL),
    ('7', 'Subventions, Quotes-parts et Contributions allouées, indemnités', NULL)
ON CONFLICT (code) DO NOTHING;

-- Sous-rubriques de niveau 2, rattachées à leur rubrique parente
INSERT INTO rubrique_budgetaire (code, libelle, parent_id) VALUES
    ('11', 'Rémunérations principales', (SELECT id FROM rubrique_budgetaire WHERE code = '1')),
    ('12', 'Indemnités de fonction', (SELECT id FROM rubrique_budgetaire WHERE code = '1')),
    ('13', 'Rémunérations pour travaux en heures supplémentaires', (SELECT id FROM rubrique_budgetaire WHERE code = '1')),
    ('14', 'Indemnités et primes diverses', (SELECT id FROM rubrique_budgetaire WHERE code = '1')),
    ('16', 'Boni', (SELECT id FROM rubrique_budgetaire WHERE code = '1')),
    ('17', 'Protection sociale', (SELECT id FROM rubrique_budgetaire WHERE code = '1')),
    ('19', 'Taxe sur la masse salariale', (SELECT id FROM rubrique_budgetaire WHERE code = '1')),

    ('20', 'Services de base', (SELECT id FROM rubrique_budgetaire WHERE code = '2')),
    ('21', 'Publications, Impressions, Reprographie, Reliure', (SELECT id FROM rubrique_budgetaire WHERE code = '2')),
    ('22', 'Transport et Déplacements', (SELECT id FROM rubrique_budgetaire WHERE code = '2')),
    ('23', 'Formation', (SELECT id FROM rubrique_budgetaire WHERE code = '2')),
    ('24', 'Location Immobilière et mobilière', (SELECT id FROM rubrique_budgetaire WHERE code = '2')),
    ('25', 'Entretien biens Mobiliers et Immobiliers', (SELECT id FROM rubrique_budgetaire WHERE code = '2')),
    ('26', 'Charges financières', (SELECT id FROM rubrique_budgetaire WHERE code = '2')),
    ('27', 'Fêtes et cérémonies', (SELECT id FROM rubrique_budgetaire WHERE code = '2')),
    ('29', 'Services et charges Divers', (SELECT id FROM rubrique_budgetaire WHERE code = '2')),

    ('30', 'Fournitures et petit matériel', (SELECT id FROM rubrique_budgetaire WHERE code = '3')),
    ('31', 'Produits chimiques et fournitures énergétiques', (SELECT id FROM rubrique_budgetaire WHERE code = '3')),
    ('32', 'Produits de subsistance', (SELECT id FROM rubrique_budgetaire WHERE code = '3')),
    ('33', 'Textiles et habillement', (SELECT id FROM rubrique_budgetaire WHERE code = '3')),
    ('39', 'Autres biens consommables et petit matériel', (SELECT id FROM rubrique_budgetaire WHERE code = '3')),

    ('40', 'Mobilier, matériel et outillage', (SELECT id FROM rubrique_budgetaire WHERE code = '4')),
    ('41', 'Matériel de transport', (SELECT id FROM rubrique_budgetaire WHERE code = '4')),
    ('43', 'Terrains', (SELECT id FROM rubrique_budgetaire WHERE code = '4')),
    ('44', 'Bois, forêts, plantations', (SELECT id FROM rubrique_budgetaire WHERE code = '4')),
    ('45', 'Littoral, étangs et lacs', (SELECT id FROM rubrique_budgetaire WHERE code = '4')),
    ('46', 'Bâtiments', (SELECT id FROM rubrique_budgetaire WHERE code = '4')),
    ('47', 'Voies, réseaux et ouvrages', (SELECT id FROM rubrique_budgetaire WHERE code = '4')),

    ('50', 'Immobilisations incorporelles', (SELECT id FROM rubrique_budgetaire WHERE code = '5')),

    ('70', 'Subventions d''exploitation', (SELECT id FROM rubrique_budgetaire WHERE code = '7')),
    ('71', 'Subvention en capital', (SELECT id FROM rubrique_budgetaire WHERE code = '7'))
ON CONFLICT (code) DO NOTHING;

-- Sources de financement (Ressources Nationales / Externes)
INSERT INTO source_financement (nom, categorie, type) VALUES
    ('Trésor public', 'NATIONALE', 'PUBLIC'),
    ('AFC',           'NATIONALE', 'PUBLIC'),
    ('Fonds Propres', 'NATIONALE', 'PUBLIC'),
    ('Petrocaribe',   'EXTERNE',   'PRET'),
    ('Bilatéral',     'EXTERNE',   'DON'),
    ('Multilatéral',  'EXTERNE',   'PRET'),
    ('Privé',         'EXTERNE',   'ONG')
ON CONFLICT (nom) DO NOTHING;

-- Grand Chantier observé dans le canevas
INSERT INTO grand_chantier (nom) VALUES ('Réfondation Sociale')
ON CONFLICT (nom) DO NOTHING;

-- =====================================================================
-- 7. WORKFLOW DE VALIDATION FIOP / PIP (MENFP <-> MPCE)
--
-- Cycle du FIOP : BROUILLON -> SOUMIS -> VALIDE_UEP -> APPROUVE_MPCE
--                                 \-> REJETE_UEP (retour Opérateur)
--                      VALIDE_UEP \-> REJETE_MPCE (retour Superviseur MENFP)
--
-- Cycle du PIP (créé par le MPCE une fois le FIOP APPROUVE_MPCE) :
-- BROUILLON -> SOUMIS (verrouillé : suppression interdite une fois soumis)
-- =====================================================================

ALTER TABLE projet ADD COLUMN IF NOT EXISTS statut VARCHAR(20) NOT NULL DEFAULT 'BROUILLON'
    CHECK (statut IN ('BROUILLON', 'SOUMIS', 'VALIDE_UEP', 'REJETE_UEP', 'REJETE_MPCE', 'APPROUVE_MPCE'));
ALTER TABLE projet ADD COLUMN IF NOT EXISTS motif_rejet TEXT;
ALTER TABLE projet ADD COLUMN IF NOT EXISTS date_soumission TIMESTAMP;
ALTER TABLE projet ADD COLUMN IF NOT EXISTS valide_par_id BIGINT REFERENCES users(user_id) ON DELETE SET NULL;
ALTER TABLE projet ADD COLUMN IF NOT EXISTS date_validation_uep TIMESTAMP;
ALTER TABLE projet ADD COLUMN IF NOT EXISTS approuve_par_id BIGINT REFERENCES users(user_id) ON DELETE SET NULL;
ALTER TABLE projet ADD COLUMN IF NOT EXISTS date_approbation_mpce TIMESTAMP;

CREATE TABLE IF NOT EXISTS pip (
    id              BIGSERIAL PRIMARY KEY,
    projet_id       BIGINT NOT NULL UNIQUE REFERENCES projet(id) ON DELETE RESTRICT,
    statut          VARCHAR(20) NOT NULL DEFAULT 'BROUILLON' CHECK (statut IN ('BROUILLON', 'SOUMIS')),
    budget_alloue   NUMERIC(18,2) DEFAULT 0,
    cree_par_id     BIGINT REFERENCES users(user_id) ON DELETE SET NULL,
    date_soumission TIMESTAMP,
    created_at      TIMESTAMP NOT NULL DEFAULT now(),
    updated_at      TIMESTAMP NOT NULL DEFAULT now()
);
