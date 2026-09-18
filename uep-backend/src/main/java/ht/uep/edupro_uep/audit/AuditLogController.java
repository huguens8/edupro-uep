package ht.uep.edupro_uep.audit;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ht.uep.edupro_uep.dto.AuditLogResponse;

/**
 * Consultation du journal d'audit. Réservé au rôle ADM (voir SecurityConfig).
 */
@RestController
@RequestMapping("/api/audit-logs")
public class AuditLogController {

    private final AuditLogRepository auditLogRepository;

    public AuditLogController(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @GetMapping
    public List<AuditLogResponse> listRecent() {
        return auditLogRepository.findTop200ByOrderByCreatedAtDesc().stream()
                .map(AuditLogResponse::new)
                .toList();
    }
}
