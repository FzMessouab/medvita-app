package com.medvita.backend.config;

import com.medvita.backend.entities.*;
import com.medvita.backend.enums.*;
import com.medvita.backend.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Component
public class TestDataLoader implements CommandLineRunner {

    private final ClientRepository clientRepository;
    private final EquipmentRepository equipmentRepository;
    private final PurchaseRepository purchaseRepository;
    private final RentalRepository rentalRepository;
    private final InvoiceRepository invoiceRepository;

    public TestDataLoader(ClientRepository clientRepository,
                          EquipmentRepository equipmentRepository,
                          PurchaseRepository purchaseRepository,
                          RentalRepository rentalRepository,
                          InvoiceRepository invoiceRepository) {
        this.clientRepository = clientRepository;
        this.equipmentRepository = equipmentRepository;
        this.purchaseRepository = purchaseRepository;
        this.rentalRepository = rentalRepository;
        this.invoiceRepository = invoiceRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        seedEquipmentCatalog();

        if (clientRepository.count() == 0) {
            loadDemoTransactions();
        }
    }

    private void loadDemoTransactions() {
        Client client1 = new Client();
        client1.setFullName("John Doe");
        client1.setEmail("john.doe@example.com");
        client1.setTelephone("+1234567890");
        client1.setAdresse("123 Main St");
        client1.setVille("New York");
        client1.setCodePostal("10001");
        client1.setPays("USA");
        client1.setNumeroSecu("123456789012345");
        client1.setCreatedAt(LocalDateTime.now());
        client1.setUpdatedAt(LocalDateTime.now());
        client1.setActif(true);

        Client client2 = new Client();
        client2.setFullName("Jane Smith");
        client2.setEmail("jane.smith@example.com");
        client2.setTelephone("+9876543210");
        client2.setAdresse("456 Oak Ave");
        client2.setVille("Los Angeles");
        client2.setCodePostal("90001");
        client2.setPays("USA");
        client2.setCreatedAt(LocalDateTime.now());
        client2.setUpdatedAt(LocalDateTime.now());
        client2.setNumeroSecu("987654321098765");
        client2.setActif(true);

        List<Client> clients = clientRepository.saveAll(List.of(client1, client2));
        List<Equipment> equipment = equipmentRepository.findAll();

        // Create Purchases
        Purchase purchase1 = new Purchase();
        purchase1.setClient(clients.get(0));
        purchase1.setEquipment(equipment.get(0));
        purchase1.setQuantity(1);
        purchase1.setTotalAmount(199.99);
        purchase1.setPurchaseDate(LocalDateTime.now().minusDays(5));
        purchase1.setPaymentMethod(PaymentMethod.CARTE);

        Purchase purchase2 = new Purchase();
        purchase2.setClient(clients.get(1));
        purchase2.setEquipment(equipment.get(1));
        purchase2.setQuantity(1);
        purchase2.setTotalAmount(899.99);
        purchase2.setPurchaseDate(LocalDateTime.now().minusDays(2));
        purchase2.setPaymentMethod(PaymentMethod.VIREMENT);

        List<Purchase> purchases = purchaseRepository.saveAll(List.of(purchase1, purchase2));

        // Create Rentals
        Rental rental1 = new Rental();
        rental1.setClient(clients.get(0));
        rental1.setEquipment(equipment.get(1));
        rental1.setRentalDate(LocalDate.now().minusDays(10));
        rental1.setStartDate(LocalDate.now().minusDays(10));
        rental1.setEndDate(LocalDate.now().plusDays(20));
        rental1.setQuantity(1);
        rental1.setTotalAmount(299.97); // 30 days at 9.99/day
        rental1.setPaymentStatus(PaymentStatus.PAID);
        rental1.setRentalStatus(RentalStatus.ACTIVE);
        rental1.setPaymentMethod(PaymentMethod.CARTE);

        Rental rental2 = new Rental();
        rental2.setClient(clients.get(1));
        rental2.setEquipment(equipment.get(0));
        rental2.setRentalDate(LocalDate.now().minusDays(5));
        rental2.setStartDate(LocalDate.now().minusDays(5));
        rental2.setEndDate(LocalDate.now().plusDays(25));
        rental2.setQuantity(1);
        rental2.setTotalAmount(199.98); // 30 days at 6.66/day
        rental2.setPaymentStatus(PaymentStatus.PENDING);
        rental2.setRentalStatus(RentalStatus.ACTIVE);
        rental2.setPaymentMethod(PaymentMethod.VIREMENT);

        List<Rental> rentals = rentalRepository.saveAll(List.of(rental1, rental2));

        // Create Invoices
        Invoice invoice1 = new Invoice();
        invoice1.setPurchase(purchases.get(0));
        invoice1.setInvoiceNumber("INV-2023-001");
        invoice1.setIssueDate(LocalDate.now().minusDays(4));
        invoice1.setAmount(199.99);
        invoice1.setStatus(InvoiceStatus.PAID);
        invoice1.setFilePath("/invoices/INV-2023-001.pdf");

        Invoice invoice2 = new Invoice();
        invoice2.setRental(rentals.get(0));
        invoice2.setInvoiceNumber("INV-2023-002");
        invoice2.setIssueDate(LocalDate.now().minusDays(9));
        invoice2.setAmount(299.97);
        invoice2.setStatus(InvoiceStatus.PAID);
        invoice2.setFilePath("/invoices/INV-2023-002.pdf");

        invoiceRepository.saveAll(List.of(invoice1, invoice2));
    }

    private void seedEquipmentCatalog() {
        List<Equipment> existingEquipment = equipmentRepository.findAll();
        List<String> existingNames = existingEquipment.stream()
                .map(Equipment::getName)
                .map(name -> name.toLowerCase(Locale.ROOT))
                .toList();

        List<Equipment> catalog = new ArrayList<>();
        addIfMissing(catalog, existingNames, createEquipment(
                "Wheelchair Standard",
                "Mobility",
                "ISO 7176-1",
                69.76,
                199.99,
                100,
                "Standard wheelchair for adult patients with foldable frame and reinforced wheels."
        ));
        addIfMissing(catalog, existingNames, createEquipment(
                "Oxygen Concentrator 5L",
                "Respiratory",
                "ISO 80601-2-69",
                209.36,
                899.99,
                60,
                "Stationary oxygen concentrator with nebulizer support for clinics and home-care needs."
        ));
        addIfMissing(catalog, existingNames, createEquipment(
                "Patient Monitor Pro",
                "Monitoring",
                "IEC 60601-1",
                180.00,
                1450.00,
                24,
                "Multi-parameter monitor for ECG, SpO2, non-invasive blood pressure and temperature tracking."
        ));
        addIfMissing(catalog, existingNames, createEquipment(
                "Infusion Pump SmartFlow",
                "Infusion",
                "IEC 60601-2-24",
                95.00,
                790.00,
                40,
                "Programmable infusion pump designed for precise flow control in hospital wards and treatment rooms."
        ));
        addIfMissing(catalog, existingNames, createEquipment(
                "Hospital Bed Electric Comfort",
                "Hospital Furniture",
                "IEC 60601-2-52",
                140.00,
                1290.00,
                18,
                "Electric medical bed with adjustable height, backrest and leg support for inpatient care."
        ));
        addIfMissing(catalog, existingNames, createEquipment(
                "Suction Unit Portable",
                "Respiratory",
                "ISO 10079-1",
                75.00,
                420.00,
                35,
                "Portable medical suction device for emergency rooms, ambulances and minor procedure areas."
        ));
        addIfMissing(catalog, existingNames, createEquipment(
                "ECG Machine 12 Lead",
                "Diagnostics",
                "IEC 60601-2-25",
                155.00,
                1180.00,
                22,
                "Twelve-lead electrocardiograph with integrated printer for routine cardiac assessment."
        ));
        addIfMissing(catalog, existingNames, createEquipment(
                "Defibrillator Rescue AED",
                "Emergency",
                "IEC 60601-2-4",
                260.00,
                2100.00,
                14,
                "Automated external defibrillator for rapid emergency response in public or clinical settings."
        ));
        addIfMissing(catalog, existingNames, createEquipment(
                "Ultrasound Portable Scan",
                "Imaging",
                "IEC 60601-2-37",
                320.00,
                4200.00,
                10,
                "Portable ultrasound platform suitable for point-of-care imaging and bedside examinations."
        ));
        addIfMissing(catalog, existingNames, createEquipment(
                "Examination Table Premium",
                "Consultation Room",
                "EN 60601-1",
                55.00,
                390.00,
                30,
                "Adjustable examination table for consultation rooms, outpatient care and general practice."
        ));
        addIfMissing(catalog, existingNames, createEquipment(
                "Nebulizer Compact Air",
                "Respiratory",
                "EN 13544-1",
                35.00,
                140.00,
                80,
                "Compact nebulizer system for aerosol therapy with reliable daily performance."
        ));
        addIfMissing(catalog, existingNames, createEquipment(
                "Sterilizer Autoclave 23L",
                "Sterilization",
                "EN 13060",
                210.00,
                1890.00,
                12,
                "Class B autoclave for sterilization of instruments in practices, labs and procedure rooms."
        ));

        if (!catalog.isEmpty()) {
            equipmentRepository.saveAll(catalog);
        }
    }

    private void addIfMissing(List<Equipment> catalog, List<String> existingNames, Equipment equipment) {
        if (!existingNames.contains(equipment.getName().toLowerCase(Locale.ROOT))) {
            catalog.add(equipment);
        }
    }

    private Equipment createEquipment(String name,
                                      String category,
                                      String safetyStandard,
                                      double dailyRentalPrice,
                                      double purchasePrice,
                                      int stockQuantity,
                                      String description) {
        Equipment equipment = new Equipment();
        equipment.setName(name);
        equipment.setCategory(category);
        equipment.setSafetyStandard(safetyStandard);
        equipment.setDailyRentalPrice(dailyRentalPrice);
        equipment.setPurchasePrice(purchasePrice);
        equipment.setStockQuantity(stockQuantity);
        equipment.setDescription(description);
        equipment.setActive(true);
        return equipment;
    }
}
