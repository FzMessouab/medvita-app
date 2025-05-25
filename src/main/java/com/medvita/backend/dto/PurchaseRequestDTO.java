package com.medvita.backend.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class PurchaseRequestDTO extends BaseRequestDTO {
    private Long clientId;
    private Long equipmentId;
    private int quantity;
    private String paymentMethod; // "CARTE", "VIREMENT", "ESPÈCES"
}
