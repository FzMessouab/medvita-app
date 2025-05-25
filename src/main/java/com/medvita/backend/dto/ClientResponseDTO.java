package com.medvita.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientResponseDTO extends BaseResponseDTO {
    private String fullName;
    private String email;
    private String phone;
    private String completeAddress;
    private Boolean active;
}
