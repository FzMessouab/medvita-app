package com.medvita.backend.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter
public class DeliveryResponseDTO extends BaseResponseDTO {
    private LocalDate expectedDeliveryDate;
    private LocalDate actualDeliveryDate;
    private String status;
    private String trackingInfo;
    private String trackingUrl;
}
