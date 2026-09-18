package ht.uep.edupro_uep.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import ht.uep.edupro_uep.user.Role;
import ht.uep.edupro_uep.user.User;
import ht.uep.edupro_uep.user.UserRepository;

/**
 * Crée un premier compte ADM au démarrage si aucun n'existe encore,
 * pour amorcer la création des comptes suivants depuis l'interface.
 */
@Component
public class AdminBootstrap implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(AdminBootstrap.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final String bootstrapUsername;
    private final String bootstrapEmail;
    private final String bootstrapPassword;

    public AdminBootstrap(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            @Value("${app.bootstrap-admin.username:admin}") String bootstrapUsername,
            @Value("${app.bootstrap-admin.email:admin@edupro-uep.local}") String bootstrapEmail,
            @Value("${app.bootstrap-admin.password:admin123}") String bootstrapPassword) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.bootstrapUsername = bootstrapUsername;
        this.bootstrapEmail = bootstrapEmail;
        this.bootstrapPassword = bootstrapPassword;
    }

    @Override
    public void run(String... args) {
        boolean hasAdmin = userRepository.findAll().stream().anyMatch(u -> u.getRole() == Role.ADMINISTRATEUR);
        if (hasAdmin) {
            return;
        }

        User admin = new User(bootstrapUsername, passwordEncoder.encode(bootstrapPassword), bootstrapEmail, Role.ADMINISTRATEUR);
        userRepository.save(admin);
        log.warn("Aucun compte ADM trouvé : un compte '{}' a été créé avec le mot de passe par défaut. "
                + "Connectez-vous et changez ce mot de passe dès que possible.", bootstrapUsername);
    }
}
