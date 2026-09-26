package ht.uep.edupro_uep.audit;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * Hibernate crée audit_log.action avec une contrainte CHECK listant les valeurs de
 * {@link AuditAction} au moment de la création de la table, et ddl-auto=update ne la
 * met jamais à jour : toute nouvelle action serait refusée par la base. On la
 * reconstruit donc au démarrage à partir de l'enum.
 */
@Component
public class AuditActionConstraintMigration {

    private static final Logger log = LoggerFactory.getLogger(AuditActionConstraintMigration.class);

    private final JdbcTemplate jdbcTemplate;

    public AuditActionConstraintMigration(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void syncActionCheckConstraint() {
        String values = Arrays.stream(AuditAction.values())
                .map(a -> "'" + a.name() + "'")
                .collect(Collectors.joining(", "));
        try {
            jdbcTemplate.execute("alter table audit_log drop constraint if exists audit_log_action_check");
            jdbcTemplate.execute("alter table audit_log add constraint audit_log_action_check check (action in ("
                    + values + "))");
        } catch (Exception ex) {
            log.error("Impossible de mettre à jour la contrainte audit_log_action_check : {}", ex.getMessage());
        }
    }
}
