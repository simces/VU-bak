package com.photo.business.repository.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@Entity
@Table(name = "admin_audit_logs")
public class AuditDAO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "admin_id", nullable = false)
    private Long adminId;

    @Column(name = "action_type", nullable = false, length = 50)
    private String actionType;

    @Column(name = "table_name", nullable = false, length = 50)
    private String tableName;

    @Column(name = "record_id", nullable = false)
    private Long recordId;

    @Column(name = "data_before", columnDefinition = "JSON")
    private String dataBefore;

    @Column(name = "data_after", columnDefinition = "JSON")
    private String dataAfter;

    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;

    public AuditDAO(Long adminId, String actionType, String tableName, Long recordId, String dataBefore, String dataAfter) {
        this.adminId = adminId;
        this.actionType = actionType;
        this.tableName = tableName;
        this.recordId = recordId;
        this.dataBefore = dataBefore;
        this.dataAfter = dataAfter;
        this.createdAt = new Timestamp(System.currentTimeMillis());
    }
}