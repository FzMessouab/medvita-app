package com.medvita.backend.services;


import com.medvita.backend.dto.RentalRequestDTO;
import com.medvita.backend.dto.RentalResponseDTO;
import com.medvita.backend.entities.Client;
import com.medvita.backend.entities.Equipment;
import com.medvita.backend.entities.Rental;
import com.medvita.backend.enums.PaymentMethod;
import com.medvita.backend.enums.PaymentStatus;
import com.medvita.backend.enums.RentalStatus;
import com.medvita.backend.repositories.RentalRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class RentalService extends AbstractService
<Rental,Long,RentalRequestDTO, RentalResponseDTO,RentalRepository>
{

    private final RentalRepository rentalRepository;
    private final EquipmentService equipmentService;
    private final ClientService clientService;
    private final InvoiceService invoiceService;

    public RentalService(RentalRepository rentalRepository,
                         EquipmentService equipmentService,
                         ClientService clientService,
                         InvoiceService invoiceService) {
        super(rentalRepository);
        this.rentalRepository = rentalRepository;
        this.equipmentService = equipmentService;
        this.clientService = clientService;
        this.invoiceService = invoiceService;
    }

    @Transactional
    public Rental createRental(RentalRequestDTO request) {
        Client client = clientService.getById(request.getClientId());
        Equipment equipment = equipmentService.getById(request.getEquipmentId());

        checkRentalAvailability(equipment.getId(), request.getStartDate(), request.getEndDate());
        equipmentService.checkEquipmentAvailability(equipment.getId(), request.getQuantity());

        long rentalDays = ChronoUnit.DAYS.between(request.getStartDate(), request.getEndDate());
        double totalAmount = rentalDays * equipment.getDailyRentalPrice() * request.getQuantity();

        Rental rental = Rental.builder()
                .client(client)
                .equipment(equipment)
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .totalAmount(totalAmount)
                .paymentStatus(PaymentStatus.PENDING)
                .rentalStatus(RentalStatus.ACTIVE)
                .quantity(request.getQuantity())
                .rentalDate(LocalDate.now())
                .paymentMethod(PaymentMethod.valueOf(request.getPaymentMethod()))
                .build();

        Rental savedRental = rentalRepository.save(rental);
        invoiceService.generateRentalInvoice(savedRental);

        return savedRental;
    }

    private void checkRentalAvailability(Long equipmentId, LocalDate startDate, LocalDate endDate) {
        List<Rental> conflicts = rentalRepository.findConflictingRentals(
                equipmentId, startDate, endDate);

        if (!conflicts.isEmpty()) {
            throw new RuntimeException(
                    "L'équipement n'est pas disponible pour la période demandée");
        }
    }

    public List<Rental> getClientRentals(Long clientId) {
        return rentalRepository.findByClientId(clientId);
    }

    @Transactional
    public Rental cancelRental(Long rentalId) {
        Rental rental = this.getById(rentalId);

        if (rental.getPaymentStatus() == PaymentStatus.PENDING) {
            rental.setPaymentStatus(PaymentStatus.CANCELLED);
            return rentalRepository.save(rental);
        } else {
            throw new IllegalStateException("Cannot cancel a rental that is not in PENDING status.");
        }
    }










}