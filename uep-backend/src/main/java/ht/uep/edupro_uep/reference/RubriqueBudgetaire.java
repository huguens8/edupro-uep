package ht.uep.edupro_uep.reference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/** Référentiel du plan de dépenses budgétaire (2 niveaux : rubrique / sous-rubrique). */
@Entity
@Table(name = "rubrique_budgetaire")
public class RubriqueBudgetaire {

    @Id
    @Column(name = "id_rubrique")
    private Integer id;

    @Column(name = "id_rubrique_parent")
    private Integer idRubriqueParent;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private String libelle;

    @Column(nullable = false)
    private Short niveau;

    public Integer getId() {
        return id;
    }

    public Integer getIdRubriqueParent() {
        return idRubriqueParent;
    }

    public String getCode() {
        return code;
    }

    public String getLibelle() {
        return libelle;
    }

    public Short getNiveau() {
        return niveau;
    }
}
