package com.medvita.backend.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class PurchaseResponseDTO extends BaseResponseDTO {
    private Long clientId;
    private Long equipmentId;
    private int quantity;
    private String paymentMethod; // "CARTE", "VIREMENT", "ESPÈCES"
}
