package com.medvita.backend.mappers;

import com.medvita.backend.dto.ClientRequestDTO;
import com.medvita.backend.dto.ClientResponseDTO;
import com.medvita.backend.entities.Client;
import org.mapstruct.*;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface ClientMapper extends AbstractMapper<Client, ClientResponseDTO, Long> {

     ClientResponseDTO toDto(Client entity);

   Client toEntity(ClientRequestDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(ClientRequestDTO dto, @MappingTarget Client entity);
}
