package com.photo.model.audits;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuditDTO {

    private Long id;
    private Long adminId;
    private String actionType;
    private String tableName;
    private Long recordId;
    private String dataBefore;
    private String dataAfter;
    private Timestamp createdAt;
}