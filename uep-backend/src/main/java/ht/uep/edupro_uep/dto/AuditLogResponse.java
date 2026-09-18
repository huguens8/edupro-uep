package ht.uep.edupro_uep.dto;

import java.time.LocalDateTime;

import ht.uep.edupro_uep.audit.AuditAction;
import ht.uep.edupro_uep.audit.AuditLog;

public class AuditLogResponse {

    private final Integer id;
    private final String username;
    private final AuditAction action;
    private final String targetType;
    private final Integer targetId;
    private final String detail;
    private final String ipAddress;
    private final LocalDateTime createdAt;

    public AuditLogResponse(AuditLog log) {
        this.id = log.getId();
        this.username = log.getUsername();
        this.action = log.getAction();
        this.targetType = log.getTargetType();
        this.targetId = log.getTargetId();
        this.detail = log.getDetail();
        this.ipAddress = log.getIpAddress();
        this.createdAt = log.getCreatedAt();
    }

    public Integer getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public AuditAction getAction() {
        return action;
    }

    public String getTargetType() {
        return targetType;
    }

    public Integer getTargetId() {
        return targetId;
    }

    public String getDetail() {
        return detail;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
