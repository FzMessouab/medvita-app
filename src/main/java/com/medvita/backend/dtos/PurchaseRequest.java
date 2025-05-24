package com.medvita.backend.dtos;

import lombok.Data;

@Data
public class PurchaseRequest {
    private Long clientId;
    private Long equipmentId;
    private int quantity;
    private String paymentMethod; // "CARTE", "VIREMENT", "ESPÈCES"
}
