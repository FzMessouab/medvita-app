package com.medvita.backend.controllers;

import com.medvita.backend.dto.ClientRequestDTO;
import com.medvita.backend.dto.ClientResponseDTO;
import com.medvita.backend.entities.Client;
import com.medvita.backend.mappers.ClientMapper;
import com.medvita.backend.services.ClientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Pageable;
import java.util.List;

@RestController
@RequestMapping("/api/clients")
public class ClientController extends AbstractController<
        Client,
        Long,
        ClientRequestDTO,
        ClientResponseDTO,
        ClientService
        > {

    private final ClientService clientService;
    private final ClientMapper clientMapper;

    public ClientController(ClientService clientService, ClientMapper clientMapper) {
        super(clientService);
        this.clientService = clientService;
        this.clientMapper = clientMapper;
    }

    @GetMapping("/active")
    public ResponseEntity<List<ClientResponseDTO>> getActiveClients() {
        List<Client> activeClients = clientService.getActifClients();
        List<ClientResponseDTO> dtos = activeClients.stream()
                .map(clientMapper::toDto)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ClientResponseDTO>> searchClients(
            @RequestParam String query,
            Pageable pageable) {
        List<Client> clients = clientService.searchClients(query);
        List<ClientResponseDTO> dtos = clients.stream()
                .map(clientMapper::toDto)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivateClient(@PathVariable Long id) {
        clientService.deactivateClient(id);
        return ResponseEntity.noContent().build();
    }
}
