package com.medvita.backend.services;


import com.medvita.backend.dtos.PurchaseRequest;
import com.medvita.backend.entities.Client;
import com.medvita.backend.entities.Equipment;
import com.medvita.backend.entities.Invoice;
import com.medvita.backend.entities.Purchase;
import com.medvita.backend.enums.TransactionType;
import com.medvita.backend.repositories.PurchaseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final EquipmentService equipmentService;
    private final ClientService clientService;
    private final InvoiceService invoiceService;
    private final TransactionService transactionService;

    public PurchaseService(PurchaseRepository purchaseRepository,
                           EquipmentService equipmentService,
                           ClientService clientService,
                           InvoiceService invoiceService,
                           TransactionService transactionService) {
        this.purchaseRepository = purchaseRepository;
        this.equipmentService = equipmentService;
        this.clientService = clientService;
        this.invoiceService = invoiceService;
        this.transactionService = transactionService;
    }

    @Transactional
    public Purchase processPurchase(PurchaseRequest request) {
        Client client = clientService.getById(request.getClientId());
        Equipment equipment = equipmentService.getById(request.getEquipmentId());

        if (equipment.getStockQuantity() < request.getQuantity()) {
            throw new RuntimeException("Stock insuffisant pour cet équipement");
        }

        double totalAmount = equipment.getPurchasePrice() * request.getQuantity();

        // Update stock
        equipment.setStockQuantity(equipment.getStockQuantity() - request.getQuantity());
        equipmentService.update(equipment);

        Purchase purchase = Purchase.builder()
                .client(client)
                .equipment(equipment)
                .quantity(request.getQuantity())
                .totalAmount(totalAmount)
                .purchaseDate(LocalDateTime.now())
                .paymentMethod(request.getPaymentMethod())
                .build();

        Purchase savedPurchase = purchaseRepository.save(purchase);

        // Generate invoice
        Invoice invoice = invoiceService.generatePurchaseInvoice(savedPurchase);

        // Record transaction
        transactionService.recordTransaction(
                client,
                totalAmount,
                TransactionType.ACHAT,
                "Paiement pour achat d'équipement: " + equipment.getName()
        );

        return savedPurchase;
    }
}
