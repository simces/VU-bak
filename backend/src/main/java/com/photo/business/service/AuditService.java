package com.photo.business.service;

import com.photo.business.repository.model.AuditDAO;

import java.util.List;

public interface AuditService {

    void logChange(Long adminId, String actionType, String tableName, Long recordId, String dataBefore, String dataAfter);

    List<AuditDAO> getAllAudits();

}
