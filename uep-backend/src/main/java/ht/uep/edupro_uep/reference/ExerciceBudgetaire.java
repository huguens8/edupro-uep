package ht.uep.edupro_uep.reference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "exercice_budgetaire")
public class ExerciceBudgetaire {

    @Id
    @Column(name = "id_exercice")
    private Integer id;

    @Column(nullable = false)
    private String libelle;

    public Integer getId() {
        return id;
    }

    public String getLibelle() {
        return libelle;
    }
}
