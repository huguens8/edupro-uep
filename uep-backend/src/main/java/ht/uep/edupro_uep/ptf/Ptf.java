package ht.uep.edupro_uep.ptf;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

/** Partenaire technique et financier et son protocole d'accord (UC-D1 à UC-D3). */
@Entity
@Table(name = "ptf")
public class Ptf {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ptf_seq")
    @SequenceGenerator(name = "ptf_seq", sequenceName = "ptf_id_ptf_seq", allocationSize = 1)
    @Column(name = "id_ptf")
    private Integer id;

    @Column(nullable = false, length = 200)
    private String nom;

    @Column(length = 100)
    private String pays;

    @Column(length = 30)
    private String telephone;

    @Column(length = 100)
    private String courriel;

    @Column(name = "date_protocole")
    private LocalDate dateProtocole;

    @Column(name = "reference_protocole", length = 100)
    private String referenceProtocole;

    /** Chemin du protocole archivé, relatif au dossier de stockage (voir FileStorageService). */
    @Column(name = "document_archive_url", length = 500)
    private String documentArchiveUrl;

    public Integer getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPays() {
        return pays;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getCourriel() {
        return courriel;
    }

    public void setCourriel(String courriel) {
        this.courriel = courriel;
    }

    public LocalDate getDateProtocole() {
        return dateProtocole;
    }

    public void setDateProtocole(LocalDate dateProtocole) {
        this.dateProtocole = dateProtocole;
    }

    public String getReferenceProtocole() {
        return referenceProtocole;
    }

    public void setReferenceProtocole(String referenceProtocole) {
        this.referenceProtocole = referenceProtocole;
    }

    public String getDocumentArchiveUrl() {
        return documentArchiveUrl;
    }

    public void setDocumentArchiveUrl(String documentArchiveUrl) {
        this.documentArchiveUrl = documentArchiveUrl;
    }
}
