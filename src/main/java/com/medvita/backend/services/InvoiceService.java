package com.medvita.backend.services;


import com.itextpdf.text.DocumentException;
import com.medvita.backend.dto.InvoiceResponseDTO;
import com.medvita.backend.entities.Client;
import com.medvita.backend.entities.Invoice;
import com.medvita.backend.entities.Purchase;
import com.medvita.backend.entities.Rental;
import com.medvita.backend.enums.InvoiceStatus;
import com.medvita.backend.enums.PaymentStatus;
import com.medvita.backend.repositories.InvoiceRepository;
import com.medvita.backend.utils.InvoiceGenerator;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
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
                .status(InvoiceStatus.PAID)
                .build();

        // Generate PDF
        String filePath = null;
        try {
            filePath = invoiceGenerator.generatePurchaseInvoice(invoice);
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
                .status(rental.getPaymentStatus().equals(PaymentStatus.PAID) ? InvoiceStatus.PAID : InvoiceStatus.PENDING)
                .build();

        // Generate PDF
        String filePath = null;
        try {
            filePath = invoiceGenerator.generateRentalInvoice(invoice);
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        invoice.setFilePath(filePath);

        return invoiceRepository.save(invoice);
    }

    public Invoice generateCartInvoice(Client client, List<Purchase> purchases, List<Rental> rentals) {
        String invoiceNumber = "INV-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        double totalAmount = purchases.stream().mapToDouble(Purchase::getTotalAmount).sum()
                + rentals.stream().mapToDouble(Rental::getTotalAmount).sum();

        boolean hasPendingRental = rentals.stream()
                .anyMatch(rental -> rental.getPaymentStatus() != PaymentStatus.PAID);

        Invoice invoice = Invoice.builder()
                .invoiceNumber(invoiceNumber)
                .issueDate(LocalDate.now())
                .amount(totalAmount)
                .status(hasPendingRental ? InvoiceStatus.PENDING : InvoiceStatus.PAID)
                .build();

        Invoice savedInvoice = invoiceRepository.save(invoice);

        try {
            String filePath = invoiceGenerator.generateCartInvoice(savedInvoice, client, purchases, rentals);
            savedInvoice.setFilePath(filePath);
        } catch (DocumentException | IOException e) {
            throw new RuntimeException(e);
        }

        return invoiceRepository.save(savedInvoice);
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

    public Invoice getByPurchaseId(Long purchaseId) {
        Invoice invoice = invoiceRepository.findByPurchaseId(purchaseId);
        if (invoice == null) {
            throw new EntityNotFoundException("Invoice not found for purchase id: " + purchaseId);
        }
        return invoice;
    }

    public Invoice getByRentalId(Long rentalId) {
        Invoice invoice = invoiceRepository.findByRentalRentalId(rentalId);
        if (invoice == null) {
            throw new EntityNotFoundException("Invoice not found for rental id: " + rentalId);
        }
        return invoice;
    }
}
