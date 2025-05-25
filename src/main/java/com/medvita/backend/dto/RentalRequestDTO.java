package com.medvita.backend.dto;


import lombok.Data;
import java.time.LocalDate;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter
public class RentalRequestDTO extends BaseRequestDTO {
    @NotNull
    private Long clientId;

    @NotNull
    private Long equipmentId;

    @NotNull @Min(1)
    private Integer quantity;

    @NotNull @FutureOrPresent
    private LocalDate startDate;

    @NotNull @Future
    private LocalDate endDate;

    @NotBlank @Size(max = 50)
    private String paymentMethod;
}
