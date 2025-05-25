package com.medvita.backend.entities;

import com.medvita.backend.enums.PaymentMethod;
import com.medvita.backend.enums.PaymentStatus;
import com.medvita.backend.enums.RentalStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;

@Entity
@Table(name = "locations")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rental extends BaseEntity<Long> {

    @Column(nullable = false,unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rentalId;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne
    @JoinColumn(name = "equipement_id", nullable = false)
    private Equipment equipment;

    @Column(name = "date_location", nullable = false)
    private LocalDate rentalDate;

    @Column(name = "qte",nullable = false)
    private Integer quantity=1;

    @Column(name = "date_debut", nullable = false)
    private LocalDate startDate;

    @Column(name = "date_fin", nullable = false)
    private LocalDate endDate;

    @Column(name = "montant_total", nullable = false)
    private double totalAmount;

    @Column(name = "statut_paiement", nullable = false)
    private PaymentStatus paymentStatus;

    @Column(name = "statut_location", nullable = false)
    private RentalStatus rentalStatus;

    @Column(name = "mode_paiement", nullable = false)
    private PaymentMethod paymentMethod;

    @Override
    public Long getId() {
        return rentalId;
    }
}
