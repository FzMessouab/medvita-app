package com.medvita.backend.repositories;

import com.medvita.backend.entities.Equipment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EquipmentRepository extends AbstractRepository<Equipment,Long> {
    Page<Equipment> findAllByActiveTrue(Pageable pageable);

    @Query("SELECT e FROM Equipment e WHERE e.active = true AND " +
            "(LOWER(e.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(e.description) LIKE LOWER(CONCAT('%', :query, '%')))")
    Page<Equipment> searchActiveEquipment(String query, Pageable pageable);

    List<Equipment> findAllByActiveTrue();
}