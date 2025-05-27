package com.medvita.backend.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "equipements")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Equipment extends BaseEntity<Long> {

    @Column(name = "equipement_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(name = "nom", nullable = false)
    private String name;

    @Column(name = "categorie", nullable = false)
    private String category;

    @Column(name = "norme_securite", nullable = false)
    private String safetyStandard;

    @Column(name = "prix_location_jour", nullable = false)
    private double dailyRentalPrice;

    @Column(name = "prix_achat", nullable = false)
    private double purchasePrice;

    @Column(name = "quantite_stock", nullable = false)
    private int stockQuantity;

    @Column(name = "nom_image")
    private String imageName;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "active", nullable = false)
    private boolean active = true;

    @Override
    public Long getId() {
        return id;
    }
}