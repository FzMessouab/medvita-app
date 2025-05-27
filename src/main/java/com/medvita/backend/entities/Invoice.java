package com.medvita.backend.entities;

import com.medvita.backend.enums.InvoiceStatus;
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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "invoice_id")  // Explicit column name
    private Long id;

    @OneToOne
    @JoinColumn(name = "purchase_id")
    private Purchase purchase;

    @OneToOne
    @JoinColumn(name = "rental_id")
    private Rental rental;

    @Column(name = "numero_facture", unique = true, nullable = false)
    private String invoiceNumber;

    @Column(name = "date_emission", nullable = false)
    private LocalDate issueDate;

    @Column(name = "montant", nullable = false)
    private double amount;

    @Column(name = "statut", nullable = false)
    @Enumerated(EnumType.STRING)
    private InvoiceStatus status;

    @Column(name = "chemin_fichier")
    private String filePath;

    @Override
    public Long getId() {
        return this.id;
    }
}
