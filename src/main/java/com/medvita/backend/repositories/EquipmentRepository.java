package com.medvita.backend.repositories;

import com.medvita.backend.entities.Equipment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EquipmentRepository extends AbstractRepository<Equipment,Long> {
    Page<Equipment> findAllByActiveTrue(Pageable pageable);

    @Query("SELECT e FROM Equipment e WHERE e.active = true AND " +
            "(LOWER(e.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(e.description) LIKE LOWER(CONCAT('%', :query, '%')))")
    Page<Equipment> searchActiveEquipment(String query, Pageable pageable);

    List<Equipment> findAllByActiveTrue();

    @Query("SELECT DISTINCT e.category FROM Equipment e WHERE e.active = true ORDER BY e.category ASC")
    List<String> findAllActiveCategories();

    @Query("SELECT e FROM Equipment e " +
            "WHERE e.active = true AND e.deleted = false AND LOWER(e.name) = LOWER(:name)")
    Optional<Equipment> findActiveByName(String name);
}
