package com.medvita.backend.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "achats")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Purchase extends BaseEntity<Long> {

    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long purchaseId;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne
    @JoinColumn(name = "equipement_id", nullable = false)
    private Equipment equipment;

    @Column(name = "quantite", nullable = false)
    private int quantity;

    @Column(name = "montant_total", nullable = false)
    private double totalAmount;

    @Column(name = "date_achat", nullable = false)
    private LocalDateTime purchaseDate;

    @Column(name = "methode_paiement", nullable = false)
    private String paymentMethod;

    @OneToOne(mappedBy = "purchase", cascade = CascadeType.ALL)
    private Invoice invoice;

    @Override
    public Long getId() {
        return purchaseId;
    }
}
