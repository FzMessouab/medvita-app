package com.medvita.backend.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter @Setter
public class EquipmentResponseDTO extends BaseResponseDTO {
    private String name;
    private String type;
    private String safetyNorm;
    private BigDecimal dailyRentalPrice;
    private BigDecimal purchasePrice;
    private Integer availableQuantity;
    private String imageUrl;
    private String description;
    private Integer stockQuantity;
}
