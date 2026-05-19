package com.medvita.backend.mappers;

import com.medvita.backend.dto.PurchaseRequestDTO;
import com.medvita.backend.dto.PurchaseResponseDTO;
import com.medvita.backend.entities.*;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        uses = {ClientMapper.class, EquipmentMapper.class, InvoiceMapper.class})
public interface PurchaseMapper {
    Purchase toEntity(PurchaseRequestDTO dto);

    @Mapping(target = "invoiceNumber", source = "invoice.invoiceNumber")
    PurchaseResponseDTO toDto(Purchase entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(PurchaseRequestDTO dto, @MappingTarget Purchase entity);
}
