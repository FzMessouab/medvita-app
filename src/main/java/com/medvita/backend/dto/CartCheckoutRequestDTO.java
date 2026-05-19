package com.medvita.backend.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CartCheckoutRequestDTO extends BaseRequestDTO {
    private Long clientId;
    private Long equipmentId;
    private Integer quantity;
    private String paymentMethod;
    private String checkoutType;
    private String type;
    private LocalDate startDate;
    private LocalDate endDate;
}
