package com.medvita.backend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "clients")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Client extends BaseEntity<Long> {

    @Column(name = "nom_complet", nullable = false)
    @NotBlank(message = "Le nom complet est obligatoire")
    private String fullName;

    @Column(name = "email", nullable = false, unique = true)
    @Email(message = "L'email doit être valide")
    @NotBlank(message = "L'email est obligatoire")
    private String email;

    @Column(name = "telephone", nullable = false)
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Le numéro de téléphone doit être valide")
    private String telephone;

    @Column(name = "adresse", nullable = false, columnDefinition = "TEXT")
    @NotBlank(message = "L'adresse est obligatoire")
    private String adresse;

    @Column(name = "ville", nullable = false)
    @NotBlank(message = "La ville est obligatoire")
    private String ville;

    @Column(name = "code_postal", nullable = false)
    @Pattern(regexp = "^[0-9]{5}$", message = "Le code postal doit contenir 5 chiffres")
    private String codePostal;

    @Column(name = "pays", nullable = false)
    @NotBlank(message = "Le pays est obligatoire")
    private String pays;

    @Column(name = "numero_secu", unique = true)
    @Pattern(regexp = "^[0-9]{15}$", message = "Le numéro de sécurité sociale doit contenir 15 chiffres")
    private String numeroSecu;

    @Column(name = "actif", nullable = false)
    private boolean actif = true;

}