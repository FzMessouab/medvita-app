package com.medvita.backend.entities;

import com.medvita.backend.enums.PaymentMethod;
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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "purchase_id")  // Explicit column name
    private Long id;

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
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @OneToOne(mappedBy = "purchase", cascade = CascadeType.ALL, orphanRemoval = true)
    private Invoice invoice;

    @Override
    public Long getId() {
        return this.id;
    }
}
