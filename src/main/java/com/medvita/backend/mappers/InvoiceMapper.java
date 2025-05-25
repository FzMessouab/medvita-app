package com.medvita.backend.mappers;

import com.medvita.backend.dto.InvoiceResponseDTO;
import com.medvita.backend.entities.Invoice;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface InvoiceMapper extends AbstractMapper<Invoice, InvoiceResponseDTO,Long> {
    InvoiceResponseDTO toDto(Invoice entity);
}
