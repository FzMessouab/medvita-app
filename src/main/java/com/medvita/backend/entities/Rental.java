package com.medvita.backend.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "locations")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rental extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne
    @JoinColumn(name = "equipement_id", nullable = false)
    private Equipment equipment;

    @Column(name = "date_debut", nullable = false)
    private LocalDate startDate;

    @Column(name = "date_fin", nullable = false)
    private LocalDate endDate;

    @Column(name = "montant_total", nullable = false)
    private double totalAmount;

    @Column(name = "statut_paiement", nullable = false)
    private String paymentStatus;

    @Column(name = "statut_location", nullable = false)
    private String rentalStatus;
}
