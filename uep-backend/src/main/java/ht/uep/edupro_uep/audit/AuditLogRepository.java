package ht.uep.edupro_uep.audit;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditLogRepository extends JpaRepository<AuditLog, Integer> {

    List<AuditLog> findTop200ByOrderByCreatedAtDesc();
}
