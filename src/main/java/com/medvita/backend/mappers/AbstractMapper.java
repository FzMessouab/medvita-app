package com.medvita.backend.mappers;


import com.medvita.backend.dto.BaseDTO;
import com.medvita.backend.entities.BaseEntity;

import java.io.Serializable;

public interface AbstractMapper<E extends BaseEntity<ID>, D extends BaseDTO, ID extends Serializable> {
    D toDto(E entity);
    E toEntity(D dto);

    default void updateEntityFromDto(D dto, E entity) {
        // Default implementation can be overridden
        throw new UnsupportedOperationException("Update operation not supported");
    }
}
