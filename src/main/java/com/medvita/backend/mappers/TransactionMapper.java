package com.medvita.backend.mappers;

import com.medvita.backend.dto.TransactionResponseDTO;
import com.medvita.backend.entities.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring", uses = ClientMapper.class)
public interface TransactionMapper extends AbstractMapper<Transaction, TransactionResponseDTO,Long> {

    TransactionResponseDTO toDto(Transaction entity);
}
