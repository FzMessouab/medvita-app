package com.medvita.backend.services;


import com.medvita.backend.entities.Invoice;
import com.medvita.backend.entities.Purchase;
import com.medvita.backend.entities.Rental;
import com.medvita.backend.repositories.InvoiceRepository;
import com.medvita.backend.utils.InvoiceGenerator;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final InvoiceGenerator invoiceGenerator;

    public InvoiceService(InvoiceRepository invoiceRepository,
                          InvoiceGenerator invoiceGenerator) {
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
                .status(rental.getPaymentStatus().equals("PAYÉ") ? "PAYÉE" : "EN_ATTENTE")
                .build();

        // Generate PDF
        String filePath = invoiceGenerator.generatePurchaseInvoice(invoice);
        invoice.setFilePath(filePath);

        return invoiceRepository.save(invoice);
    }
}
