package ht.uep.edupro_uep.archive;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

/**
 * Document archivé dans le dossier d'un projet (UC-E1). Un nouveau téléversement portant le même
 * nom de document crée une nouvelle version ; les précédentes sont conservées avec actif = false.
 */
@Entity
@Table(name = "archive_projet")
public class ArchiveProjet {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "archive_projet_seq")
    @SequenceGenerator(name = "archive_projet_seq", sequenceName = "archive_projet_id_archive_seq", allocationSize = 1)
    @Column(name = "id_archive")
    private Integer id;

    @Column(name = "id_projet", nullable = false)
    private Integer idProjet;

    /** Nom d'origine du fichier téléversé. */
    @Column(name = "nom_document", nullable = false)
    private String nomDocument;

    /** Chemin relatif au dossier de stockage (voir FileStorageService). */
    @Column(name = "chemin_stockage", nullable = false, length = 500)
    private String cheminStockage;

    @Column(name = "type_document", nullable = false, length = 100)
    private String typeDocument;

    private Short version;

    private String description;

    @Column(name = "id_utilisateur_ajout")
    private Integer idUtilisateurAjout;

    @Column(name = "date_ajout")
    private LocalDateTime dateAjout;

    private Boolean actif;

    public Integer getId() {
        return id;
    }

    public Integer getIdProjet() {
        return idProjet;
    }

    public void setIdProjet(Integer idProjet) {
        this.idProjet = idProjet;
    }

    public String getNomDocument() {
        return nomDocument;
    }

    public void setNomDocument(String nomDocument) {
        this.nomDocument = nomDocument;
    }

    public String getCheminStockage() {
        return cheminStockage;
    }

    public void setCheminStockage(String cheminStockage) {
        this.cheminStockage = cheminStockage;
    }

    public String getTypeDocument() {
        return typeDocument;
    }

    public void setTypeDocument(String typeDocument) {
        this.typeDocument = typeDocument;
    }

    public Short getVersion() {
        return version;
    }

    public void setVersion(Short version) {
        this.version = version;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getIdUtilisateurAjout() {
        return idUtilisateurAjout;
    }

    public void setIdUtilisateurAjout(Integer idUtilisateurAjout) {
        this.idUtilisateurAjout = idUtilisateurAjout;
    }

    public LocalDateTime getDateAjout() {
        return dateAjout;
    }

    public void setDateAjout(LocalDateTime dateAjout) {
        this.dateAjout = dateAjout;
    }

    public Boolean getActif() {
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }
}
