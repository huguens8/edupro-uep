package ht.uep.edupro_uep.audit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
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

    public AuditLogService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    public void log(String username, AuditAction action, String targetType, Integer targetId, String detail) {
        try {
            auditLogRepository.save(
                    new AuditLog(username, action, targetType, targetId, detail, currentIp()));
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
