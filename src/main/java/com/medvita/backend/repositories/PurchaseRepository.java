package com.medvita.backend.repositories;


import com.medvita.backend.dto.PurchaseResponseDTO;
import com.medvita.backend.entities.Purchase;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PurchaseRepository extends AbstractRepository<Purchase,Long> {

    @Query("SELECT p FROM Purchase p WHERE p.client.id = :clientId AND p.purchaseDate >= :cutoffDate")
    List<Purchase> findByClientIdAndPurchaseDateAfter(
            @Param("clientId") Long clientId,
            @Param("cutoffDate") LocalDateTime cutoffDate);}
