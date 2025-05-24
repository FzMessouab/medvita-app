package com.medvita.backend.services;

import com.medvita.backend.entities.Client;
import com.medvita.backend.entities.Transaction;
import com.medvita.backend.enums.TransactionStatus;
import com.medvita.backend.enums.TransactionType;
import com.medvita.backend.repositories.TransactionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public Transaction recordTransaction(Client client,
                                         double amount,
                                         TransactionType type,
                                         String description) {
        Transaction transaction = Transaction.builder()
                .client(client)
                .amount(amount)
                .transactionDate(LocalDateTime.now())
                .type(type)
                .reference("TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                .status(TransactionStatus.COMPLETED)
                .build();

        return transactionRepository.save(transaction);
    }

    public Page<Transaction> getClientTransactions(Long clientId, Pageable pageable) {
        return transactionRepository.findByClientId(clientId, pageable);
    }
}