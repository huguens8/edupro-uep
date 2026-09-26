package ht.uep.edupro_uep.audit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Écriture des événements d'audit. Volontairement best-effort : si
 * l'écriture échoue (ex. contrainte en base), on logge l'erreur mais on ne
 * relance jamais — journaliser une action ne doit pas empêcher de l'effectuer.
 */
@Service
public class AuditLogService {

    private static final Logger log = LoggerFactory.getLogger(AuditLogService.class);

    private final AuditLogRepository auditLogRepository;
    // Transaction séparée : un échec d'écriture du journal ne doit pas annuler
    // (rollback-only) la transaction de l'action métier qui l'appelle.
    private final TransactionTemplate separateTransaction;

    public AuditLogService(AuditLogRepository auditLogRepository, PlatformTransactionManager transactionManager) {
        this.auditLogRepository = auditLogRepository;
        this.separateTransaction = new TransactionTemplate(transactionManager);
        this.separateTransaction.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
    }

    public void log(String username, AuditAction action, String targetType, Integer targetId, String detail) {
        try {
            AuditLog entry = new AuditLog(username, action, targetType, targetId, detail, currentIp());
            separateTransaction.executeWithoutResult(status -> auditLogRepository.saveAndFlush(entry));
        } catch (Exception ex) {
            log.error("Échec de l'écriture du journal d'audit ({}, {}) : {}", username, action, ex.getMessage());
        }
    }

    private String currentIp() {
        var attributes = RequestContextHolder.getRequestAttributes();
        if (attributes instanceof ServletRequestAttributes servletAttributes) {
            HttpServletRequest request = servletAttributes.getRequest();
            return request.getRemoteAddr();
        }
        return null;
    }
}
