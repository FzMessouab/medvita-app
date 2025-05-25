package com.medvita.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BaseRequestDTO extends BaseDTO {
    // Common fields for all request DTOs
    private Integer version; // For optimistic locking
}
