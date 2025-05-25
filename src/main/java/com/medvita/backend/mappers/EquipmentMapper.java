package com.medvita.backend.mappers;

import com.medvita.backend.dto.EquipmentRequestDTO;
import com.medvita.backend.dto.EquipmentResponseDTO;
import com.medvita.backend.entities.Equipment;
import org.mapstruct.*;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface EquipmentMapper extends AbstractMapper<Equipment, EquipmentResponseDTO,Long> {

     EquipmentResponseDTO toDto(Equipment entity);

   Equipment toEntity(EquipmentRequestDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(EquipmentRequestDTO dto, @MappingTarget Equipment entity);
}
