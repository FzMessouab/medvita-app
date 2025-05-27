package com.medvita.backend.controllers;

import com.medvita.backend.dto.InvoiceResponseDTO;
import com.medvita.backend.entities.Invoice;
import com.medvita.backend.mappers.InvoiceMapper;
import com.medvita.backend.services.InvoiceService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController extends AbstractController<Invoice,Long,InvoiceResponseDTO,InvoiceResponseDTO,InvoiceService> {

    private final InvoiceService invoiceService;
    private final InvoiceMapper invoiceMapper;

    public InvoiceController(InvoiceService invoiceService, InvoiceMapper invoiceMapper) {
        super(invoiceService);
        this.invoiceService = invoiceService;
        this.invoiceMapper = invoiceMapper;
    }


    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> downloadInvoice(@PathVariable Long id) {
        Invoice invoice = invoiceService.getById(id);
        Resource resource = invoiceService.downloadInvoiceFile(id);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"invoice_" + invoice.getInvoiceNumber() + ".pdf\"")
                .body(resource);
    }

    @GetMapping("/rental/{rentalId}")
    public ResponseEntity<InvoiceResponseDTO> getInvoiceForRental(@PathVariable Long rentalId) {
        Invoice invoice = invoiceService.getById(rentalId);
        return ResponseEntity.ok(invoiceMapper.toDto(invoice));
    }
}