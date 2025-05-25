package com.medvita.backend.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter @Setter
public class RentalResponseDTO extends BaseResponseDTO {
    private Long clientId;
    private String clientName;
    private Long equipmentId;
    private String equipmentName;
    private Integer quantity;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal totalAmount;
    private String paymentStatus;
    private String rentalStatus;
}
