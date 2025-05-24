package com.medvita.backend.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "clients")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Client extends BaseEntity {
    @Column(name = "nom_complet", nullable = false)
    private String fullName;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "telephone", nullable = false)
    private String phone;

    @Column(name = "adresse", nullable = false)
    private String address;

    @Column(name = "actif", nullable = false)
    private boolean active = true;
}
