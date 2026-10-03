package ht.uep.edupro_uep.storage;

import java.nio.charset.StandardCharsets;

import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;

/** Réponse HTTP de téléchargement d'une pièce, sous son nom d'origine. */
public final class Telechargement {

    private Telechargement() {
    }

    public static ResponseEntity<Resource> reponse(Resource fichier, String nomOrigine) {
        MediaType type = MediaTypeFactory.getMediaType(nomOrigine).orElse(MediaType.APPLICATION_OCTET_STREAM);
        return ResponseEntity.ok()
                .contentType(type)
                .header(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.attachment()
                        .filename(nomOrigine, StandardCharsets.UTF_8).build().toString())
                .body(fichier);
    }
}
