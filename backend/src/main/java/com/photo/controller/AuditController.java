package com.photo.controller;

import com.photo.business.repository.model.AuditDAO;
import com.photo.business.service.AuditService;
import com.photo.model.AuditDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/audits")
public class AuditController {

    private final AuditService auditService;

    public AuditController(AuditService auditService) {
        this.auditService = auditService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<AuditDTO>> getAllAudits() {
        List<AuditDTO> audits = auditService.getAllAudits();
        return ResponseEntity.ok(audits);
    }
}
