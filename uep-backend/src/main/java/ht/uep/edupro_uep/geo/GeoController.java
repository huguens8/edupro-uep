package ht.uep.edupro_uep.geo;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ht.uep.edupro_uep.dto.GeoOptionResponse;

/**
 * Référentiel géographique (découpage administratif d'Haïti) utilisé par le
 * formulaire de localisation du FIOP. Lecture seule, ouvert à tout
 * utilisateur authentifié.
 */
@RestController
@RequestMapping("/api/geo")
public class GeoController {

    private final DepartementRepository departementRepository;
    private final ArrondissementRepository arrondissementRepository;
    private final CommuneRepository communeRepository;
    private final SectionCommunaleRepository sectionCommunaleRepository;

    public GeoController(
            DepartementRepository departementRepository,
            ArrondissementRepository arrondissementRepository,
            CommuneRepository communeRepository,
            SectionCommunaleRepository sectionCommunaleRepository) {
        this.departementRepository = departementRepository;
        this.arrondissementRepository = arrondissementRepository;
        this.communeRepository = communeRepository;
        this.sectionCommunaleRepository = sectionCommunaleRepository;
    }

    @GetMapping("/departements")
    public List<GeoOptionResponse> departements() {
        return departementRepository.findAllByOrderByLibelleAsc().stream()
                .map(d -> new GeoOptionResponse(d.getId(), d.getLibelle()))
                .toList();
    }

    @GetMapping("/arrondissements")
    public List<GeoOptionResponse> arrondissements(@RequestParam String departementId) {
        return arrondissementRepository.findByIdDepartementOrderByLibelleAsc(departementId).stream()
                .map(a -> new GeoOptionResponse(a.getId(), a.getLibelle()))
                .toList();
    }

    @GetMapping("/communes")
    public List<GeoOptionResponse> communes(@RequestParam String arrondissementId) {
        return communeRepository.findByIdArrondissementOrderByLibelleAsc(arrondissementId).stream()
                .map(c -> new GeoOptionResponse(c.getId(), c.getLibelle()))
                .toList();
    }

    @GetMapping("/sections-communales")
    public List<GeoOptionResponse> sectionsCommunales(@RequestParam String communeId) {
        return sectionCommunaleRepository.findByIdCommuneOrderByLibelleAsc(communeId).stream()
                .map(s -> new GeoOptionResponse(s.getId(), s.getLibelle()))
                .toList();
    }
}
