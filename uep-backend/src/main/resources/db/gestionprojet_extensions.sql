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
