package com.medvita.backend.services;


import com.medvita.backend.dto.InvoiceResponseDTO;
import com.medvita.backend.entities.Invoice;
import com.medvita.backend.entities.Purchase;
import com.medvita.backend.entities.Rental;
import com.medvita.backend.enums.PaymentStatus;
import com.medvita.backend.repositories.InvoiceRepository;
import com.medvita.backend.utils.InvoiceGenerator;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.UUID;

@Service
public class InvoiceService extends AbstractService<Invoice,Long, InvoiceResponseDTO,InvoiceResponseDTO,InvoiceRepository>
{

    private final InvoiceRepository invoiceRepository;
    private final InvoiceGenerator invoiceGenerator;

    public InvoiceService(InvoiceRepository invoiceRepository,
                          InvoiceGenerator invoiceGenerator) {
        super(invoiceRepository);
        this.invoiceRepository = invoiceRepository;
        this.invoiceGenerator = invoiceGenerator;
    }

    public Invoice generatePurchaseInvoice(Purchase purchase) {
        String invoiceNumber = "INV-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        Invoice invoice = Invoice.builder()
                .purchase(purchase)
                .invoiceNumber(invoiceNumber)
                .issueDate(LocalDate.now())
                .amount(purchase.getTotalAmount())
                .status("PAYÉE")
                .build();

        // Generate PDF
        String filePath = invoiceGenerator.generatePurchaseInvoice(invoice);
        invoice.setFilePath(filePath);

        return invoiceRepository.save(invoice);
    }

    public Invoice generateRentalInvoice(Rental rental) {
        String invoiceNumber = "INV-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        Invoice invoice = Invoice.builder()
                .rental(rental)
                .invoiceNumber(invoiceNumber)
                .issueDate(LocalDate.now())
                .amount(rental.getTotalAmount())
                .status(rental.getPaymentStatus().equals(PaymentStatus.PAID) ? "PAYÉE" : "EN_ATTENTE")
                .build();

        // Generate PDF
        String filePath = invoiceGenerator.generatePurchaseInvoice(invoice);
        invoice.setFilePath(filePath);

        return invoiceRepository.save(invoice);
    }

    public Resource downloadInvoiceFile(Long invoiceId) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new EntityNotFoundException("Invoice not found with id: " + invoiceId));

        Path path = Paths.get(invoice.getFilePath());

        if (!Files.exists(path)) {
            throw new RuntimeException("Invoice file not found at path: " + path.toString());
        }

        return new PathResource(path);
    }
}
