package com.medvita.backend.controllers;

import com.medvita.backend.dto.PurchaseRequestDTO;
import com.medvita.backend.dto.PurchaseResponseDTO;
import com.medvita.backend.entities.Purchase;
import com.medvita.backend.mappers.PurchaseMapper;
import com.medvita.backend.services.PurchaseService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/purchases")
public class PurchaseController extends AbstractController<
        Purchase,
        Long,
        PurchaseRequestDTO,
        PurchaseResponseDTO,
        PurchaseService
        > {

    private final PurchaseService purchaseService;
    private final PurchaseMapper purchaseMapper;

    public PurchaseController(PurchaseService purchaseService, PurchaseMapper purchaseMapper) {
        super(purchaseService);
        this.purchaseService = purchaseService;
        this.purchaseMapper = purchaseMapper;
    }


    @PostMapping("/process")
    public ResponseEntity<PurchaseResponseDTO> createPurchase(
            @Valid @RequestBody PurchaseRequestDTO requestDTO) {
        PurchaseResponseDTO response = purchaseService.processPurchase(requestDTO);
        URI location = URI.create("/api/purchases/" + response.getId());
        return ResponseEntity.created(location).body(response);
    }


    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<PurchaseResponseDTO>> getPurchaseByClientId(
            @PathVariable Long clientId,
            @RequestParam(required = false) Integer lastDays) {

        List<Purchase> purchases = purchaseService.findByClientId(clientId, lastDays);

        if (purchases.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<PurchaseResponseDTO> responseDTOs = purchases.stream()
                .map(purchaseMapper::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseDTOs);
    }
}