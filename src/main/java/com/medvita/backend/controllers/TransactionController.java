package com.medvita.backend.controllers;


import com.medvita.backend.dto.TransactionResponseDTO;
import com.medvita.backend.mappers.TransactionMapper;
import com.medvita.backend.services.TransactionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;
    private final TransactionMapper transactionMapper;

    public TransactionController(TransactionService transactionService,
                                 TransactionMapper transactionMapper) {
        this.transactionService = transactionService;
        this.transactionMapper = transactionMapper;
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponseDTO> getTransaction(@PathVariable Long id) {
        return ResponseEntity.ok(
                transactionMapper.toDto(transactionService.getById(id))
        );
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<Page<TransactionResponseDTO>> getClientTransactions(
            @PathVariable Long clientId,
            Pageable pageable) {
        return ResponseEntity.ok(
                transactionService.getClientTransactions(clientId, pageable)
                        .map(transactionMapper::toDto)
        );
    }

}