package com.medvita.backend.entities;

import com.medvita.backend.enums.TransactionStatus;
import com.medvita.backend.enums.TransactionType;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @Column(name = "montant", nullable = false)
    private double amount;

    @Column(name = "date_transaction", nullable = false)
    private LocalDateTime transactionDate;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionType type; // "ACHAT" or "LOCATION"

    @Column(name = "reference", nullable = false, unique = true)
    private String reference;

    @Column(name = "statut", nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionStatus status; // "COMPLETED", "FAILED", "PENDING"
}