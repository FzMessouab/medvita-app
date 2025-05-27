package com.medvita.backend.utils;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.medvita.backend.entities.*;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;

@Component
public class InvoiceGenerator {

    private static final String INVOICE_DIR = "invoices/";
    private static final Font TITLE_FONT = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
    private static final Font HEADER_FONT = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
    private static final Font NORMAL_FONT = new Font(Font.FontFamily.HELVETICA, 10);
    private static final Font FOOTER_FONT = new Font(Font.FontFamily.HELVETICA, 8, Font.ITALIC);
    private static final BaseColor PRIMARY_COLOR = new BaseColor(0, 102, 204); // Medvita blue
    private static final BaseColor SECONDARY_COLOR = new BaseColor(241, 241, 241); // Light gray

    static {
        new File(INVOICE_DIR).mkdirs();
    }

    public String generatePurchaseInvoice(Invoice invoice) throws DocumentException, IOException {
        String fileName = INVOICE_DIR + "purchase_" + invoice.getInvoiceNumber() + ".pdf";
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(fileName));
        document.open();

        addInvoiceHeader(document, "FACTURE D'ACHAT", invoice);
        addPurchaseDetails(document, invoice);
        addFooter(document);

        document.close();
        return fileName;
    }

    public String generateRentalInvoice(Invoice invoice) throws DocumentException, IOException {
        String fileName = INVOICE_DIR + "rental_" + invoice.getInvoiceNumber() + ".pdf";
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(fileName));
        document.open();

        addInvoiceHeader(document, "FACTURE DE LOCATION", invoice);
        addRentalDetails(document, invoice);
        addFooter(document);

        document.close();
        return fileName;
    }

    private void addInvoiceHeader(Document document, String title, Invoice invoice) throws DocumentException {
        // Company Logo and Info
        Paragraph companyInfo = new Paragraph();
        companyInfo.setAlignment(Element.ALIGN_LEFT);
        companyInfo.add(new Chunk("MEDVITA\n", TITLE_FONT));
        companyInfo.add(new Chunk("123 Rue de la Santé\n", NORMAL_FONT));
        companyInfo.add(new Chunk("Paris, France\n\n", NORMAL_FONT));
        document.add(companyInfo);

        // Invoice Title
        Paragraph invoiceTitle = new Paragraph(title, TITLE_FONT);
        invoiceTitle.setAlignment(Element.ALIGN_CENTER);
        invoiceTitle.setSpacingAfter(20);
        document.add(invoiceTitle);

        // Invoice Info Table
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10);
        table.setSpacingAfter(20);

        addTableHeaderCell(table, "Facture N°:");
        addTableCell(table, invoice.getInvoiceNumber());
        addTableHeaderCell(table, "Date:");
        addTableCell(table, invoice.getIssueDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        addTableHeaderCell(table, "Client:");
        if(invoice.getRental()!=null){
            addTableCell(table, invoice.getRental().getClient().getFullName());
        }else{
            addTableCell(table, invoice.getPurchase().getClient().getFullName());
        }

        document.add(table);
    }

    private void addPurchaseDetails(Document document, Invoice invoice) throws DocumentException {
        PdfPTable table = new PdfPTable(new float[]{3, 2, 2, 2});
        table.setWidthPercentage(100);
        table.setHeaderRows(1);

        // Table Header
        addTableHeaderCell(table, "Désignation");
        addTableHeaderCell(table, "Prix Unitaire");
        addTableHeaderCell(table, "Quantité");
        addTableHeaderCell(table, "Total");

        // Purchase Items
        Purchase purchase = invoice.getPurchase();
        addTableCell(table, purchase.getEquipment().getName());
        addTableCell(table, String.format("%.2f €", purchase.getEquipment().getPurchasePrice()));
        addTableCell(table, String.valueOf(purchase.getQuantity()));
        addTableCell(table, String.format("%.2f €", purchase.getTotalAmount()));

        // Total
        PdfPCell totalCell = new PdfPCell(new Phrase("TOTAL", HEADER_FONT));
        totalCell.setColspan(3);
        totalCell.setBorder(Rectangle.NO_BORDER);
        table.addCell(totalCell);

        PdfPCell totalValueCell = new PdfPCell(new Phrase(
                String.format("%.2f €", invoice.getAmount()), HEADER_FONT));
        totalValueCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(totalValueCell);

        document.add(table);
    }

    private void addRentalDetails(Document document, Invoice invoice) throws DocumentException {
        PdfPTable table = new PdfPTable(new float[]{4, 2, 2, 2});
        table.setWidthPercentage(100);
        table.setHeaderRows(1);

        // Table Header
        addTableHeaderCell(table, "Équipement");
        addTableHeaderCell(table, "Période");
        addTableHeaderCell(table, "Tarif Journalier");
        addTableHeaderCell(table, "Total");

        // Rental Items
        Rental rental = invoice.getRental();
        addTableCell(table, rental.getEquipment().getName());
        addTableCell(table, rental.getStartDate() + " au " + rental.getEndDate());
        addTableCell(table, String.format("%.2f €/jour", rental.getEquipment().getDailyRentalPrice()));
        addTableCell(table, String.format("%.2f €", rental.getTotalAmount()));

        // Total
        PdfPCell totalCell = new PdfPCell(new Phrase("TOTAL", HEADER_FONT));
        totalCell.setColspan(3);
        totalCell.setBorder(Rectangle.NO_BORDER);
        table.addCell(totalCell);

        PdfPCell totalValueCell = new PdfPCell(new Phrase(
                String.format("%.2f €", invoice.getAmount()), HEADER_FONT));
        totalValueCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(totalValueCell);

        document.add(table);
    }

    private void addFooter(Document document) throws DocumentException {
        Paragraph footer = new Paragraph("\n\nMerci pour votre confiance.\n" +
                "Conditions de paiement: 30 jours net\n" +
                "TVA non applicable, art. 293 B du CGI", FOOTER_FONT);
        footer.setAlignment(Element.ALIGN_CENTER);
        document.add(footer);
    }

    private void addTableHeaderCell(PdfPTable table, String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text, HEADER_FONT));
        cell.setBackgroundColor(SECONDARY_COLOR);
        cell.setPadding(5);
        table.addCell(cell);
    }

    private void addTableCell(PdfPTable table, String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text, NORMAL_FONT));
        cell.setPadding(5);
        table.addCell(cell);
    }

    public byte[] getInvoicePdf(String filePath) throws IOException {
        return Files.readAllBytes(Paths.get(filePath));
    }
}