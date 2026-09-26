package ht.uep.edupro_uep.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import ht.uep.edupro_uep.user.User;

/**
 * E-mails liés aux comptes utilisateurs.
 *
 * En production (app.mail.enabled=true + paramètres spring.mail.*), le message est
 * envoyé par SMTP. En développement (app.mail.enabled=false, par défaut), aucun
 * serveur SMTP n'est requis : le message est écrit dans les logs du serveur.
 */
@Service
public class AccountMailService {

    private static final Logger log = LoggerFactory.getLogger(AccountMailService.class);

    private final ObjectProvider<JavaMailSender> mailSender;
    private final boolean enabled;
    private final String from;
    private final String frontendUrl;

    public AccountMailService(
            ObjectProvider<JavaMailSender> mailSender,
            @Value("${app.mail.enabled:false}") boolean enabled,
            @Value("${app.mail.from:no-reply@edupro-uep.local}") String from,
            @Value("${app.frontend-url:http://localhost:3000}") String frontendUrl) {
        this.mailSender = mailSender;
        this.enabled = enabled;
        this.from = from;
        this.frontendUrl = frontendUrl;
    }

    /**
     * Envoie ses identifiants au titulaire d'un compte créé par l'administrateur.
     * Lève {@link IllegalStateException} si l'envoi échoue, pour annuler la création du compte.
     */
    public void sendAccountCreated(User user, String temporaryPassword) {
        String subject = "EduPro-UEP — Votre compte a été créé";
        String body = "Bonjour " + user.getUsername() + ",\n\n"
                + "L'administrateur d'EduPro-UEP vous a créé un compte (rôle : " + user.getRole().getLibelle() + ").\n\n"
                + "Adresse de connexion : " + frontendUrl + "/login\n"
                + "Email : " + user.getEmail() + "\n"
                + "Mot de passe temporaire : " + temporaryPassword + "\n\n"
                + "À votre première connexion, vous devrez choisir un nouveau mot de passe ; "
                + "le mot de passe temporaire ne fonctionnera plus ensuite.\n\n"
                + "Si vous n'attendiez pas ce message, ignorez-le et prévenez l'UEP.\n\n"
                + "— EduPro-UEP, MENFP";

        if (!enabled) {
            log.warn("[MODE DÉVELOPPEMENT] E-mail non envoyé (app.mail.enabled=false). Destinataire : {}\n"
                    + "Objet : {}\n{}", user.getEmail(), subject, body);
            return;
        }

        JavaMailSender sender = mailSender.getIfAvailable();
        if (sender == null) {
            throw new IllegalStateException(
                    "L'envoi d'e-mails est activé mais aucun serveur SMTP n'est configuré (spring.mail.host).");
        }
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(user.getEmail());
        message.setSubject(subject);
        message.setText(body);
        try {
            sender.send(message);
        } catch (MailException ex) {
            log.error("Échec de l'envoi de l'e-mail de création de compte à {}", user.getEmail(), ex);
            throw new IllegalStateException(
                    "L'e-mail n'a pas pu être envoyé à " + user.getEmail() + " : le compte n'a pas été créé.");
        }
    }
}
