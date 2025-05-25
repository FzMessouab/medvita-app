package com.medvita.backend.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "factures")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Invoice extends BaseEntity<Long> {

    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long invoiceId;

    @OneToOne
    @JoinColumn(name = "achat_id")
    private Purchase purchase;

    @OneToOne
    @JoinColumn(name = "location_id")
    private Rental rental;

    @Column(name = "numero_facture", unique = true, nullable = false)
    private String invoiceNumber;

    @Column(name = "date_emission", nullable = false)
    private LocalDate issueDate;

    @Column(name = "montant", nullable = false)
    private double amount;

    @Column(name = "statut", nullable = false)
    private String status;

    @Column(name = "chemin_fichier")
    private String filePath;

    @Override
    public Long getId() {
        return invoiceId;
    }
}
