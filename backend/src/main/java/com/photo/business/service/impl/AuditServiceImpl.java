package com.photo.business.service.impl;

import com.photo.business.repository.AuditRepository;
import com.photo.business.repository.model.AuditDAO;
import com.photo.business.service.AuditService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuditServiceImpl implements AuditService {

    private final AuditRepository auditRepository;

    public AuditServiceImpl(AuditRepository auditRepository) {
        this.auditRepository = auditRepository;
    }

    public void logChange(Long adminId, String actionType, String tableName, Long recordId, String dataBefore, String dataAfter) {
        AuditDAO audit = new AuditDAO(adminId, actionType, tableName, recordId, dataBefore, dataAfter);
        auditRepository.save(audit);
    }

    public List<AuditDAO> getAllAudits() {
        return auditRepository.findAllByOrderByCreatedAtDesc();
    }
}
