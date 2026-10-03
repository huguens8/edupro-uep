package ht.uep.edupro_uep.rapport;

/** Paramètre de rapport refusé (format inconnu, période manquante...) : renvoyé en 400 avec son message. */
public class ParametreRapportInvalideException extends RuntimeException {

    public ParametreRapportInvalideException(String message) {
        super(message);
    }
}
