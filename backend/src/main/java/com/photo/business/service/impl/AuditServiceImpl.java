package com.photo.business.service.impl;

import com.photo.business.mappers.AuditMapper;
import com.photo.business.repository.AuditRepository;
import com.photo.business.repository.model.AuditDAO;
import com.photo.business.service.AuditService;
import com.photo.model.AuditDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuditServiceImpl implements AuditService {

    private final AuditRepository auditRepository;
    private final AuditMapper auditMapper;

    public AuditServiceImpl(AuditRepository auditRepository, AuditMapper auditMapper) {
        this.auditRepository = auditRepository;
        this.auditMapper = auditMapper;
    }

    @Override
    public void logChange(Long adminId, String actionType, String tableName, Long recordId, String dataBefore, String dataAfter) {
        AuditDAO audit = new AuditDAO(adminId, actionType, tableName, recordId, dataBefore, dataAfter);
        auditRepository.save(audit);
    }

    @Override
    public List<AuditDTO> getAllAudits() {
        return auditRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(auditMapper::toDTO)
                .collect(Collectors.toList());
    }
}
