package com.medvita.backend.config;

import com.medvita.backend.entities.*;
import com.medvita.backend.enums.*;
import com.medvita.backend.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
@Profile("dev")
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
        // Only load data if database is empty
        if (clientRepository.count() == 0) {
            loadTestData();
        }
    }

    private void loadTestData() {
        // Create Clients
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

        // Create Medical Equipment
        Equipment equipment1 = new Equipment();
        equipment1.setName("Wheelchair Standard");
        equipment1.setDescription("Standard wheelchair for adult patients");
        equipment1.setPurchasePrice(199.99);
        equipment1.setStockQuantity(10);
        equipment1.setCategory("Mobility");
        equipment1.setDailyRentalPrice(69.76);
        equipment1.setSafetyStandard("ISO 7176-1");

        Equipment equipment2 = new Equipment();
        equipment2.setName("Oxygen Concentrator");
        equipment2.setDescription("5L Oxygen Concentrator with Nebulizer");
        equipment2.setDailyRentalPrice(209.36);
        equipment2.setPurchasePrice(899.99);
        equipment2.setStockQuantity(5);
        equipment2.setCategory("Respiratory");
        equipment2.setSafetyStandard("ISO 80601-2-69");

        List<Equipment> equipment = equipmentRepository.saveAll(List.of(equipment1, equipment2));

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
}