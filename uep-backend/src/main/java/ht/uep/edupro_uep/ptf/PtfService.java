package ht.uep.edupro_uep.ptf;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import ht.uep.edupro_uep.dto.PtfRequest;
import ht.uep.edupro_uep.dto.PtfResponse;
import ht.uep.edupro_uep.storage.FileStorageService;
import ht.uep.edupro_uep.storage.Telechargement;

@Service
public class PtfService {

    private final PtfRepository ptfRepository;
    private final FileStorageService storage;

    public PtfService(PtfRepository ptfRepository, FileStorageService storage) {
        this.ptfRepository = ptfRepository;
        this.storage = storage;
    }

    public List<PtfResponse> lister() {
        return ptfRepository.findAllByOrderByNomAsc().stream().map(this::toResponse).toList();
    }

    @Transactional
    public PtfResponse creer(PtfRequest request) {
        Ptf ptf = new Ptf();
        apply(ptf, request);
        return toResponse(ptfRepository.save(ptf));
    }

    @Transactional
    public PtfResponse modifier(Integer id, PtfRequest request) {
        Ptf ptf = findOrThrow(id);
        apply(ptf, request);
        return toResponse(ptfRepository.save(ptf));
    }

    /** UC-D3 : refusé tant que le PTF est engagé sur un projet (intervenant bailleur). */
    @Transactional
    public void supprimer(Integer id) {
        Ptf ptf = findOrThrow(id);
        if (ptfRepository.isEngageSurUnProjet(id)) {
            throw new IllegalStateException(
                    "Ce partenaire est encore engagé sur un projet : la suppression est refusée.");
        }
        String protocole = ptf.getDocumentArchiveUrl();
        ptfRepository.delete(ptf);
        storage.supprimer(protocole);
    }

    /** Remplace le protocole d'accord archivé (l'ancien fichier est supprimé du disque). */
    @Transactional
    public PtfResponse archiverProtocole(Integer id, MultipartFile fichier) {
        Ptf ptf = findOrThrow(id);
        String ancien = ptf.getDocumentArchiveUrl();
        ptf.setDocumentArchiveUrl(storage.enregistrer(fichier, "ptf/" + id));
        ptfRepository.save(ptf);
        storage.supprimer(ancien);
        return toResponse(ptf);
    }

    public ResponseEntity<Resource> telechargerProtocole(Integer id) {
        Ptf ptf = findOrThrow(id);
        if (ptf.getDocumentArchiveUrl() == null) {
            throw new NoSuchElementException("Aucun protocole archivé pour ce partenaire.");
        }
        String chemin = ptf.getDocumentArchiveUrl();
        String extension = chemin.substring(chemin.lastIndexOf('.'));
        String reference = ptf.getReferenceProtocole() != null ? ptf.getReferenceProtocole() : ptf.getNom();
        return Telechargement.reponse(storage.charger(chemin),
                "Protocole - " + reference.replaceAll("[\\\\/:*?\"<>|]", "_") + extension);
    }

    private Ptf findOrThrow(Integer id) {
        return ptfRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Partenaire introuvable."));
    }

    private void apply(Ptf ptf, PtfRequest request) {
        ptf.setNom(request.getNom().trim());
        ptf.setPays(blankToNull(request.getPays()));
        ptf.setTelephone(blankToNull(request.getTelephone()));
        ptf.setCourriel(blankToNull(request.getCourriel()));
        ptf.setDateProtocole(request.getDateProtocole());
        ptf.setReferenceProtocole(blankToNull(request.getReferenceProtocole()));
    }

    private static String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }

    private PtfResponse toResponse(Ptf ptf) {
        PtfResponse r = new PtfResponse();
        r.setId(ptf.getId());
        r.setNom(ptf.getNom());
        r.setPays(ptf.getPays());
        r.setTelephone(ptf.getTelephone());
        r.setCourriel(ptf.getCourriel());
        r.setDateProtocole(ptf.getDateProtocole());
        r.setReferenceProtocole(ptf.getReferenceProtocole());
        r.setProtocoleArchive(ptf.getDocumentArchiveUrl() != null);
        r.setEngageSurUnProjet(ptf.getId() != null && ptfRepository.isEngageSurUnProjet(ptf.getId()));
        return r;
    }
}
