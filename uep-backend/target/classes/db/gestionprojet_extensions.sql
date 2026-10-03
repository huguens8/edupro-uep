-- Extension applicative du schéma GestionProjetUEP (ppr_schema.sql).
--
-- Le schéma d'origine ne prévoit pas de champ pour motiver un rejet, alors
-- que le cahier des charges l'exige explicitement (UC-B7 : "Un motif de
-- rejet est obligatoire pour assurer la traçabilité du workflow"). On ajoute
-- donc uniquement cette colonne, sans toucher au reste du schéma existant.

ALTER TABLE projet ADD COLUMN IF NOT EXISTS motif_rejet TEXT;

-- Champ 30 : phase actuelle du projet. À l'origine deux colonnes sur projet
-- (phase_actuelle, periode_phase_actuelle), remplacées par un historique
-- (projet_phase_actuelle) : un projet traverse plusieurs phases dans le
-- temps (Elaboration -> Planification -> Exécution -> Evaluation d'Impact),
-- la phase "actuelle" étant la ligne la plus récente pour ce projet.

CREATE TABLE IF NOT EXISTS projet_phase_actuelle (
    id_phase_actuelle   SERIAL PRIMARY KEY,
    id_projet           INTEGER NOT NULL REFERENCES projet(id_projet) ON DELETE CASCADE,
    phase               VARCHAR(30) NOT NULL
                             CHECK (phase IN ('Elaboration', 'Planification', 'Execution', 'Evaluation Impact')),
    periode             VARCHAR(50),
    date_changement     TIMESTAMP NOT NULL DEFAULT now()
);

CREATE INDEX IF NOT EXISTS idx_projet_phase_actuelle_id_projet ON projet_phase_actuelle(id_projet);

-- Migration des valeurs déjà saisies sur projet (une ligne d'historique par projet concerné).
-- En dynamique car periode_phase_actuelle n'existe pas forcément encore (ajoutée par
-- Hibernate ddl-auto seulement après le premier démarrage avec ce champ).
DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM information_schema.columns
               WHERE table_name = 'projet' AND column_name = 'phase_actuelle') THEN
        EXECUTE format(
            'INSERT INTO projet_phase_actuelle (id_projet, phase, periode) '
            'SELECT id_projet, phase_actuelle, %s FROM projet WHERE phase_actuelle IS NOT NULL',
            CASE WHEN EXISTS (SELECT 1 FROM information_schema.columns
                               WHERE table_name = 'projet' AND column_name = 'periode_phase_actuelle')
                 THEN 'periode_phase_actuelle' ELSE 'NULL' END
        );
    END IF;
END $$;

ALTER TABLE projet DROP COLUMN IF EXISTS phase_actuelle;
ALTER TABLE projet DROP COLUMN IF EXISTS periode_phase_actuelle;

-- Champ 34 (colonne "# d'ordre des Activités") : une valeur par sous-rubrique et par projet (pas
-- par année, contrairement aux montants), donc une table dédiée plutôt qu'une colonne sur
-- projet_calendrier_depense_annuelle qui a une ligne par année.
CREATE TABLE IF NOT EXISTS projet_calendrier_rubrique_activites (
    id_ordre_activites  SERIAL PRIMARY KEY,
    id_projet           INTEGER NOT NULL REFERENCES projet(id_projet) ON DELETE CASCADE,
    id_rubrique         INTEGER NOT NULL REFERENCES rubrique_budgetaire(id_rubrique),
    ordre_activites     VARCHAR(150),
    UNIQUE (id_projet, id_rubrique)
);

-- Champ 35 : "Coûts récurrents du projet" (Budget de Fonctionnement Post Livrable), sur les 6
-- rubriques principales seulement (comme l'ancien §34 avant son détail par sous-rubrique).
CREATE TABLE IF NOT EXISTS projet_cout_recurrent (
    id_cout_recurrent  SERIAL PRIMARY KEY,
    id_projet          INTEGER NOT NULL REFERENCES projet(id_projet) ON DELETE CASCADE,
    id_rubrique        INTEGER NOT NULL REFERENCES rubrique_budgetaire(id_rubrique),
    annee_numero       SMALLINT NOT NULL CHECK (annee_numero BETWEEN 1 AND 5),
    montant            NUMERIC(18,2) NOT NULL DEFAULT 0
);

CREATE INDEX IF NOT EXISTS idx_projet_cout_recurrent_id_projet ON projet_cout_recurrent(id_projet);

-- Rôle Visiteur (enum Role.VISITEUR) : compte créé par l'administrateur, limité à la consultation
-- du tableau de bord. Sans cette valeur, la contrainte refusait la création d'un tel compte.
ALTER TABLE utilisateur DROP CONSTRAINT IF EXISTS utilisateur_role_check;
ALTER TABLE utilisateur ADD CONSTRAINT utilisateur_role_check
    CHECK (role IN ('Administrateur', 'Superviseur_UEP', 'Superviseur_MPCE', 'Porteur_PROJET', 'Visiteur'));

-- Champ 44 (TROISIEME PARTIE, onglet "Annuelle") : dépenses prévisionnelles de l'exercice par
-- source de financement. Une tranche annuelle par exercice, comme les autres tables de la
-- troisième partie déjà présentes (activite_chronogramme_mensuel,
-- projet_calendrier_depense_trimestrielle) : pas de table d'en-tête, la tranche est
-- l'ensemble des lignes d'un couple (projet, exercice).
CREATE TABLE IF NOT EXISTS projet_tranche_depense_source (
    id_tranche_depense  SERIAL PRIMARY KEY,
    id_projet           INTEGER NOT NULL REFERENCES projet(id_projet) ON DELETE CASCADE,
    id_exercice         INTEGER NOT NULL REFERENCES exercice_budgetaire(id_exercice),
    id_source           INTEGER NOT NULL REFERENCES source_financement(id_source),
    prevision_total     NUMERIC(18,2) NOT NULL DEFAULT 0,
    UNIQUE (id_projet, id_exercice, id_source)
);

CREATE INDEX IF NOT EXISTS idx_projet_tranche_depense_projet_exercice
    ON projet_tranche_depense_source(id_projet, id_exercice);

-- Champ 47 (TROISIEME PARTIE, onglet "R.dépenses") : voies et moyens pour la réalisation des
-- activités — lignes libres (codes et articles budgétaires) rattachées à une sous-rubrique, avec
-- les activités servies ("01, 04, 06" dans le canevas) et la répartition par source (T.P, AFC,
-- FP, Bilatéral, Multilatéral : les 5 colonnes du canevas). activite_depense_detail, pré-existante,
-- ne convient pas : une seule source et une seule activité par ligne, sans exercice.
CREATE TABLE IF NOT EXISTS projet_tranche_voie_moyen (
    id_voie_moyen         SERIAL PRIMARY KEY,
    id_projet             INTEGER NOT NULL REFERENCES projet(id_projet) ON DELETE CASCADE,
    id_exercice           INTEGER NOT NULL REFERENCES exercice_budgetaire(id_exercice),
    id_rubrique           INTEGER NOT NULL REFERENCES rubrique_budgetaire(id_rubrique),
    ordre                 SMALLINT NOT NULL,
    code_article          VARCHAR(20),
    designation           VARCHAR(255),
    ordre_activites       VARCHAR(150),
    unite_mesure          VARCHAR(50),
    quantite              NUMERIC(14,2),
    cout_unitaire         NUMERIC(18,2),
    montant_tresor_public NUMERIC(18,2),
    montant_afc           NUMERIC(18,2),
    montant_fonds_propres NUMERIC(18,2),
    montant_bilateral     NUMERIC(18,2),
    montant_multilateral  NUMERIC(18,2)
);

CREATE INDEX IF NOT EXISTS idx_projet_tranche_voie_moyen_projet_exercice
    ON projet_tranche_voie_moyen(id_projet, id_exercice);

-- Champs 48-49 (TROISIEME PARTIE, onglet "Calendrier") : calendrier prévisionnel d'utilisation
-- des ressources financières NATIONALES (48) et EXTERNES (49), par sous-rubrique et trimestre.
-- La table pré-existante n'avait pas de quoi distinguer les deux ; le champ 50 (nationales et
-- externes) n'est pas stocké, c'est la somme des deux.
ALTER TABLE projet_calendrier_depense_trimestrielle
    ADD COLUMN IF NOT EXISTS categorie_financement VARCHAR(10) NOT NULL DEFAULT 'NATIONAL';

ALTER TABLE projet_calendrier_depense_trimestrielle
    DROP CONSTRAINT IF EXISTS projet_calendrier_depense_trimestrielle_categorie_check;
ALTER TABLE projet_calendrier_depense_trimestrielle
    ADD CONSTRAINT projet_calendrier_depense_trimestrielle_categorie_check
    CHECK (categorie_financement IN ('NATIONAL', 'EXTERNE'));

ALTER TABLE projet_calendrier_depense_trimestrielle
    DROP CONSTRAINT IF EXISTS projet_calendrier_depense_trimestrielle_unique;
ALTER TABLE projet_calendrier_depense_trimestrielle
    ADD CONSTRAINT projet_calendrier_depense_trimestrielle_unique
    UNIQUE (id_projet, id_exercice, id_rubrique, categorie_financement, trimestre);
