package com.photo.business.mappers;

import com.photo.business.repository.model.AuditDAO;
import com.photo.model.AuditDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuditMapper {

    AuditDTO toDTO(AuditDAO auditDAO);

    AuditDAO toEntity(AuditDTO auditDTO);
}

