package ht.uep.edupro_uep.archive;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ArchiveProjetRepository extends JpaRepository<ArchiveProjet, Integer> {

    List<ArchiveProjet> findByIdProjetAndActifTrueOrderByDateAjoutDesc(Integer idProjet);

    List<ArchiveProjet> findByIdProjetAndNomDocumentIgnoreCase(Integer idProjet, String nomDocument);
}
