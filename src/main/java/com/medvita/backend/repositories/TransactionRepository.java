package com.medvita.backend.repositories;


import com.medvita.backend.entities.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends AbstractRepository<Transaction,Long> {
    Page<Transaction> findByClientId(Long clientId, Pageable pageable);
}