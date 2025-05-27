package com.medvita.backend.services;

import com.medvita.backend.dto.PurchaseRequestDTO;
import com.medvita.backend.dto.PurchaseResponseDTO;
import com.medvita.backend.entities.*;
import com.medvita.backend.enums.TransactionType;
import com.medvita.backend.mappers.PurchaseMapper;
import com.medvita.backend.repositories.PurchaseRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PurchaseService extends AbstractService<
        Purchase,
        Long,
        PurchaseRequestDTO,
        PurchaseResponseDTO,
        PurchaseRepository> {

    private final EquipmentService equipmentService;
    private final ClientService clientService;
    private final InvoiceService invoiceService;
    private final TransactionService transactionService;
    private final PurchaseMapper purchaseMapper;

    public PurchaseService(PurchaseRepository purchaseRepository,
                           EquipmentService equipmentService,
                           ClientService clientService,
                           InvoiceService invoiceService,
                           TransactionService transactionService,
                           PurchaseMapper purchaseMapper) {
        super(purchaseRepository);
        this.equipmentService = equipmentService;
        this.clientService = clientService;
        this.invoiceService = invoiceService;
        this.transactionService = transactionService;
        this.purchaseMapper = purchaseMapper;
    }

    @Transactional
    public PurchaseResponseDTO processPurchase(PurchaseRequestDTO request) {
        // 1. Validate and fetch entities
        Client client = clientService.getById(request.getClientId());
        Equipment equipment = equipmentService.getById(request.getEquipmentId());

        // 2. Check stock
        if (equipment.getStockQuantity() < request.getQuantity()) {
            throw new RuntimeException("Stock insuffisant pour cet équipement");
        }

        // 3. Convert DTO to entity
        Purchase purchase = purchaseMapper.toEntity(request);
        purchase.setClient(client);
        purchase.setEquipment(equipment);
        purchase.setTotalAmount(equipment.getPurchasePrice() * request.getQuantity());
        purchase.setPurchaseDate(LocalDateTime.now());

        // 4. Update stock
        equipment.setStockQuantity(equipment.getStockQuantity() - request.getQuantity());
        equipmentService.update(equipment);

        // 5. Save purchase
        Purchase savedPurchase = repository.save(purchase);

        // 6. Generate invoice
        Invoice invoice = invoiceService.generatePurchaseInvoice(savedPurchase);
        savedPurchase.setInvoice(invoice);

        // 7. Record transaction
        transactionService.recordTransaction(
                client,
                savedPurchase.getTotalAmount(),
                TransactionType.ACHAT,
                "Achat d'équipement: " + equipment.getName()
        );

        // 8. Convert to response DTO
        return purchaseMapper.toDto(savedPurchase);
    }

    public List<Purchase> findByClientId(Long clientId, Integer lastDays) {
        if (lastDays != null && lastDays > 0) {
            LocalDateTime cutoffDate = LocalDateTime.now().minusDays(lastDays);
            return repository.findByClientIdAndPurchaseDateAfter(clientId, cutoffDate);
        }
        return new ArrayList<Purchase>();
    }


}