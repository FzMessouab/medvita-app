package com.medvita.backend.repositories;


import com.medvita.backend.entities.Rental;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RentalRepository extends AbstractRepository<Rental> {
    @Query("SELECT r FROM Rental r WHERE r.equipment.id = :equipmentId " +
            "AND r.rentalStatus = 'ACTIVE' " +
            "AND ((r.startDate <= :endDate AND r.endDate >= :startDate))")
    List<Rental> findConflictingRentals(Long equipmentId, LocalDate startDate, LocalDate endDate);

    List<Rental> findByClientId(Long clientId);
}
