package com.medvita.backend.utils;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.medvita.backend.entities.*;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.time.format.DateTimeFormatter;

@Component
public class InvoiceGenerator {

    private static final String INVOICE_DIR = "invoices/";
    private static final Font TITLE_FONT = new Font(Font.FontFamily.HELVETICA, 22, Font.BOLD, BaseColor.WHITE);
    private static final Font SUBTITLE_FONT = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, new BaseColor(230, 237, 247));
    private static final Font HEADER_FONT = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD, new BaseColor(17, 32, 61));
    private static final Font LABEL_FONT = new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD, new BaseColor(95, 111, 143));
    private static final Font NORMAL_FONT = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, new BaseColor(43, 55, 79));
    private static final Font TOTAL_FONT = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD, new BaseColor(17, 32, 61));
    private static final Font FOOTER_FONT = new Font(Font.FontFamily.HELVETICA, 8, Font.ITALIC, new BaseColor(95, 111, 143));
    private static final BaseColor PRIMARY_COLOR = new BaseColor(15, 79, 168);
    private static final BaseColor PRIMARY_DARK = new BaseColor(11, 53, 115);
    private static final BaseColor ACCENT_COLOR = new BaseColor(242, 140, 56);
    private static final BaseColor SECONDARY_COLOR = new BaseColor(241, 245, 251);
    private static final BaseColor BORDER_COLOR = new BaseColor(221, 230, 240);
    private static final BaseColor MUTED_BG = new BaseColor(248, 250, 252);

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

    public String generateCartInvoice(Invoice invoice,
                                      Client client,
                                      List<Purchase> purchases,
                                      List<Rental> rentals) throws DocumentException, IOException {
        String fileName = INVOICE_DIR + "cart_" + invoice.getInvoiceNumber() + ".pdf";
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(fileName));
        document.open();

        addInvoiceHeader(document, "FACTURE PANIER", invoice, client.getFullName());
        addCartSummary(document, purchases, rentals, invoice.getAmount());
        addCartDetails(document, purchases, rentals, invoice.getAmount());
        addFooter(document);

        document.close();
        return fileName;
    }

    private void addInvoiceHeader(Document document, String title, Invoice invoice) throws DocumentException {
        addInvoiceHeader(document, title, invoice, resolveClientName(invoice));
    }

    private void addInvoiceHeader(Document document, String title, Invoice invoice, String clientName) throws DocumentException {
        PdfPTable hero = new PdfPTable(new float[]{2.8f, 1.2f});
        hero.setWidthPercentage(100);
        hero.setSpacingAfter(18);

        PdfPCell leftCell = new PdfPCell();
        leftCell.setBackgroundColor(PRIMARY_DARK);
        leftCell.setBorder(Rectangle.NO_BORDER);
        leftCell.setPadding(18);

        Paragraph brand = new Paragraph();
        brand.add(new Chunk("MEDVITA\n", TITLE_FONT));
        brand.add(new Chunk("Equipement medical professionnel\n", SUBTITLE_FONT));
        brand.add(new Chunk("Facturation achat et location", SUBTITLE_FONT));
        leftCell.addElement(brand);

        PdfPCell rightCell = new PdfPCell();
        rightCell.setBackgroundColor(PRIMARY_COLOR);
        rightCell.setBorder(Rectangle.NO_BORDER);
        rightCell.setPadding(18);

        Paragraph invoiceTitle = new Paragraph(title, new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD, BaseColor.WHITE));
        invoiceTitle.setAlignment(Element.ALIGN_RIGHT);
        rightCell.addElement(invoiceTitle);

        Paragraph invoiceRef = new Paragraph("Reference " + invoice.getInvoiceNumber(), SUBTITLE_FONT);
        invoiceRef.setAlignment(Element.ALIGN_RIGHT);
        invoiceRef.setSpacingBefore(10);
        rightCell.addElement(invoiceRef);

        hero.addCell(leftCell);
        hero.addCell(rightCell);
        document.add(hero);

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setSpacingBefore(4);
        table.setSpacingAfter(22);

        addMetaLabelCell(table, "Facture");
        addTableCell(table, invoice.getInvoiceNumber());
        addMetaLabelCell(table, "Date d'emission");
        addTableCell(table, invoice.getIssueDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        addMetaLabelCell(table, "Client");
        addTableCell(table, clientName);
        addMetaLabelCell(table, "Statut");
        addTableCell(table, invoice.getStatus().name());

        document.add(table);
    }

    private String resolveClientName(Invoice invoice) {
        if (invoice.getRental() != null) {
            return invoice.getRental().getClient().getFullName();
        }
        if (invoice.getPurchase() != null) {
            return invoice.getPurchase().getClient().getFullName();
        }
        return "Client";
    }

    private void addPurchaseDetails(Document document, Invoice invoice) throws DocumentException {
        addSectionTitle(document, "Articles achetes");
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
        PdfPCell totalCell = new PdfPCell(new Phrase("TOTAL", TOTAL_FONT));
        totalCell.setColspan(3);
        totalCell.setBorder(Rectangle.NO_BORDER);
        totalCell.setPaddingTop(10);
        table.addCell(totalCell);

        PdfPCell totalValueCell = new PdfPCell(new Phrase(
                String.format("%.2f €", invoice.getAmount()), TOTAL_FONT));
        totalValueCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        totalValueCell.setBorder(Rectangle.NO_BORDER);
        totalValueCell.setPaddingTop(10);
        table.addCell(totalValueCell);

        document.add(table);
    }

    private void addRentalDetails(Document document, Invoice invoice) throws DocumentException {
        addSectionTitle(document, "Articles loues");
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
        PdfPCell totalCell = new PdfPCell(new Phrase("TOTAL", TOTAL_FONT));
        totalCell.setColspan(3);
        totalCell.setBorder(Rectangle.NO_BORDER);
        totalCell.setPaddingTop(10);
        table.addCell(totalCell);

        PdfPCell totalValueCell = new PdfPCell(new Phrase(
                String.format("%.2f €", invoice.getAmount()), TOTAL_FONT));
        totalValueCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        totalValueCell.setBorder(Rectangle.NO_BORDER);
        totalValueCell.setPaddingTop(10);
        table.addCell(totalValueCell);

        document.add(table);
    }

    private void addCartDetails(Document document,
                                List<Purchase> purchases,
                                List<Rental> rentals,
                                double totalAmount) throws DocumentException {
        addSectionTitle(document, "Recapitulatif detaille");

        if (!purchases.isEmpty()) {
            addSubsectionTitle(document, "Achats");
            PdfPTable purchaseTable = new PdfPTable(new float[]{3, 2, 2, 2});
            purchaseTable.setWidthPercentage(100);
            purchaseTable.setHeaderRows(1);

            addTableHeaderCell(purchaseTable, "Equipement");
            addTableHeaderCell(purchaseTable, "Prix unitaire");
            addTableHeaderCell(purchaseTable, "Quantite");
            addTableHeaderCell(purchaseTable, "Total");

            for (Purchase purchase : purchases) {
                addTableCell(purchaseTable, purchase.getEquipment().getName());
                addTableCell(purchaseTable, String.format("%.2f €", purchase.getEquipment().getPurchasePrice()));
                addTableCell(purchaseTable, String.valueOf(purchase.getQuantity()));
                addTableCell(purchaseTable, String.format("%.2f €", purchase.getTotalAmount()));
            }

            document.add(purchaseTable);
            document.add(Chunk.NEWLINE);
        }

        if (!rentals.isEmpty()) {
            addSubsectionTitle(document, "Locations");
            PdfPTable rentalTable = new PdfPTable(new float[]{3, 2, 2, 1, 2});
            rentalTable.setWidthPercentage(100);
            rentalTable.setHeaderRows(1);

            addTableHeaderCell(rentalTable, "Equipement");
            addTableHeaderCell(rentalTable, "Periode");
            addTableHeaderCell(rentalTable, "Tarif journalier");
            addTableHeaderCell(rentalTable, "Qt");
            addTableHeaderCell(rentalTable, "Total");

            for (Rental rental : rentals) {
                addTableCell(rentalTable, rental.getEquipment().getName());
                addTableCell(rentalTable, rental.getStartDate() + " au " + rental.getEndDate());
                addTableCell(rentalTable, String.format("%.2f € / jour", rental.getEquipment().getDailyRentalPrice()));
                addTableCell(rentalTable, String.valueOf(rental.getQuantity()));
                addTableCell(rentalTable, String.format("%.2f €", rental.getTotalAmount()));
            }

            document.add(rentalTable);
            document.add(Chunk.NEWLINE);
        }

        PdfPTable totalTable = new PdfPTable(new float[]{4, 1});
        totalTable.setWidthPercentage(100);
        PdfPCell totalCell = new PdfPCell(new Phrase("Montant total de la demande", TOTAL_FONT));
        totalCell.setBorder(Rectangle.NO_BORDER);
        totalCell.setPaddingTop(8);
        totalCell.setPaddingBottom(8);
        totalTable.addCell(totalCell);

        PdfPCell totalValueCell = new PdfPCell(new Phrase(String.format("%.2f €", totalAmount), TOTAL_FONT));
        totalValueCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        totalValueCell.setBorder(Rectangle.NO_BORDER);
        totalValueCell.setPaddingTop(8);
        totalValueCell.setPaddingBottom(8);
        totalTable.addCell(totalValueCell);
        document.add(totalTable);
    }

    private void addFooter(Document document) throws DocumentException {
        Paragraph footer = new Paragraph(
                "\nMerci pour votre confiance.\n" +
                "Cette facture regroupe les references validees lors de votre demande.\n" +
                "Pour toute question complementaire, notre equipe commerciale reste a votre disposition.",
                FOOTER_FONT
        );
        footer.setAlignment(Element.ALIGN_CENTER);
        document.add(footer);
    }

    private void addTableHeaderCell(PdfPTable table, String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text, HEADER_FONT));
        cell.setBackgroundColor(SECONDARY_COLOR);
        cell.setBorderColor(BORDER_COLOR);
        cell.setPadding(8);
        table.addCell(cell);
    }

    private void addTableCell(PdfPTable table, String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text, NORMAL_FONT));
        cell.setBackgroundColor(MUTED_BG);
        cell.setBorderColor(BORDER_COLOR);
        cell.setPadding(8);
        table.addCell(cell);
    }

    private void addMetaLabelCell(PdfPTable table, String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text, LABEL_FONT));
        cell.setBackgroundColor(BaseColor.WHITE);
        cell.setBorderColor(BaseColor.WHITE);
        cell.setPaddingTop(6);
        cell.setPaddingBottom(6);
        table.addCell(cell);
    }

    private void addSectionTitle(Document document, String title) throws DocumentException {
        Paragraph section = new Paragraph(title, new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, PRIMARY_COLOR));
        section.setSpacingBefore(4);
        section.setSpacingAfter(10);
        document.add(section);

        LineSeparator separator = new LineSeparator();
        separator.setLineColor(ACCENT_COLOR);
        separator.setPercentage(100);
        document.add(new Chunk(separator));
        document.add(Chunk.NEWLINE);
    }

    private void addSubsectionTitle(Document document, String title) throws DocumentException {
        Paragraph section = new Paragraph(title, new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD, ACCENT_COLOR));
        section.setSpacingAfter(8);
        document.add(section);
    }

    private void addCartSummary(Document document,
                                List<Purchase> purchases,
                                List<Rental> rentals,
                                double totalAmount) throws DocumentException {
        PdfPTable summary = new PdfPTable(3);
        summary.setWidthPercentage(100);
        summary.setSpacingAfter(18);

        summary.addCell(createSummaryCell("Articles achetes", String.valueOf(purchases.size())));
        summary.addCell(createSummaryCell("Articles loues", String.valueOf(rentals.size())));
        summary.addCell(createSummaryCell("Montant total", String.format("%.2f €", totalAmount)));

        document.add(summary);
    }

    private PdfPCell createSummaryCell(String label, String value) {
        Paragraph content = new Paragraph();
        content.add(new Chunk(label + "\n", LABEL_FONT));
        content.add(new Chunk(value, new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD, PRIMARY_DARK)));

        PdfPCell cell = new PdfPCell(content);
        cell.setBackgroundColor(MUTED_BG);
        cell.setBorderColor(BORDER_COLOR);
        cell.setPadding(12);
        cell.setMinimumHeight(56);
        return cell;
    }

    public byte[] getInvoicePdf(String filePath) throws IOException {
        return Files.readAllBytes(Paths.get(filePath));
    }
}
