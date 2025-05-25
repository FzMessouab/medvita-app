package com.medvita.backend.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter @Setter
public class InvoiceResponseDTO extends BaseResponseDTO {
    private String invoiceNumber;
    private LocalDate issueDate;
    private BigDecimal amount;
    private String status;
    private String fileUrl;
    private String downloadUrl;
}
