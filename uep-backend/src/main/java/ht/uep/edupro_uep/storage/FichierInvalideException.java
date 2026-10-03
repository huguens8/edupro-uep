package ht.uep.edupro_uep.storage;

/** Fichier téléversé refusé (vide, format non supporté...) : renvoyé en 400 au client. */
public class FichierInvalideException extends RuntimeException {

    public FichierInvalideException(String message) {
        super(message);
    }
}
