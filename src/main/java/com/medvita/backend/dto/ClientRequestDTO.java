package com.medvita.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientRequestDTO extends BaseRequestDTO {
    @NotBlank
    @Size(max = 100)
    private String fullName;

    @NotBlank @Email
    @Size(max = 100)
    private String email;

    @NotBlank @Pattern(regexp = "^\\+?[0-9]{10,15}$")
    private String phone;

    @NotBlank @Size(max = 200)
    private String address;

    @NotBlank @Size(max = 100)
    private String city;

    @NotBlank @Pattern(regexp = "^[0-9]{5}$")
    private String postalCode;

    @NotBlank @Size(max = 100)
    private String country;

    @Pattern(regexp = "^[0-9]{15}$")
    private String socialSecurityNumber;
}