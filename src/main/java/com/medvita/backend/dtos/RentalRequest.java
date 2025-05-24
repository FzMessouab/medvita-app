package com.medvita.backend.dtos;


import lombok.Data;
import java.time.LocalDate;

@Data
public class RentalRequest {
    private Long clientId;
    private Long equipmentId;
    private int quantity;
    private LocalDate startDate;
    private LocalDate endDate;
}
