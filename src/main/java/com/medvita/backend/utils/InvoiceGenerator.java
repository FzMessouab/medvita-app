package com.medvita.backend.utils;


import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.medvita.backend.entities.Invoice;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Component
public class InvoiceGenerator {

    private static final String INVOICE_DIR = "invoices/";

    static {
        new File(INVOICE_DIR).mkdirs();
    }

    public String generatePurchaseInvoice(Invoice invoice) {
        String fileName = INVOICE_DIR + "purchase_" + invoice.getInvoiceNumber() + ".pdf";

        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(fileName));
            document.open();

            document.add(new Paragraph("Facture d'achat: " + invoice.getInvoiceNumber()));
            document.add(new Paragraph("Date: " + invoice.getIssueDate()));
            document.add(new Paragraph("Montant: " + invoice.getAmount() + " €"));

            if (invoice.getPurchase() != null) {
                document.add(new Paragraph("Client: " + invoice.getPurchase().getClient().getFullName()));
                document.add(new Paragraph("Équipement: " + invoice.getPurchase().getEquipment().getName()));
                document.add(new Paragraph("Quantité: " + invoice.getPurchase().getQuantity()));
            }

            document.close();
            return fileName;
        } catch (DocumentException | IOException e) {
            throw new RuntimeException("Erreur lors de la génération de la facture", e);
        }
    }

    public byte[] getInvoicePdf(String filePath) throws IOException {
        return Files.readAllBytes(Paths.get(filePath));
    }
}