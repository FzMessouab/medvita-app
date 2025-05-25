package com.medvita.backend.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class TransactionResponseDTO extends BaseResponseDTO {
    private Long clientId;
    private String clientName;
    private BigDecimal amount;
    private LocalDateTime transactionDate;
    private String type;
    private String reference;
    private String status;
    private String description;
}