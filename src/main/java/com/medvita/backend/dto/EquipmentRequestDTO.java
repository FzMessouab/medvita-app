package com.medvita.backend.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class EquipmentRequestDTO extends BaseRequestDTO {
    @NotBlank
    @Size(max = 100)
    private String name;

    @NotBlank @Size(max = 50)
    private String type;

    @NotBlank @Size(max = 50)
    private String safetyNorm;

    @NotNull
    @Positive
    private BigDecimal dailyRentalPrice;

    @NotNull @Positive
    private BigDecimal purchasePrice;

    @NotNull @Min(0)
    private Integer stockQuantity;

    @Size(max = 255)
    private String imageName;

    private String description;
}
