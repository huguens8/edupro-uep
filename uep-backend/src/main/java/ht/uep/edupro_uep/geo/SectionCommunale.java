package ht.uep.edupro_uep.geo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Table présente dans le schéma mais non peuplée à ce jour (voir le
 * cahier des charges / ppr_schema.sql) : les sections communales d'Haïti
 * n'ont pas encore été importées. Le endpoint reste fonctionnel et
 * renverra la liste réelle dès que les données seront ajoutées.
 */
@Entity
@Table(name = "section_communale")
public class SectionCommunale {

    @Id
    @Column(name = "id_section_communale")
    private String id;

    @Column(name = "id_commune", nullable = false)
    private String idCommune;

    @Column(nullable = false)
    private String libelle;

    public String getId() {
        return id;
    }

    public String getIdCommune() {
        return idCommune;
    }

    public String getLibelle() {
        return libelle;
    }
}
