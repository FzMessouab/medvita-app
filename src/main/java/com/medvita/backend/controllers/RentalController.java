package com.medvita.backend.controllers;

import com.medvita.backend.dto.RentalRequestDTO;
import com.medvita.backend.dto.RentalResponseDTO;
import com.medvita.backend.entities.Rental;
import com.medvita.backend.mappers.RentalMapper;
import com.medvita.backend.services.RentalService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/rentals")
public class RentalController {

    private final RentalService rentalService;
    private final RentalMapper rentalMapper;

    public RentalController(RentalService rentalService, RentalMapper rentalMapper) {
        this.rentalService = rentalService;
        this.rentalMapper = rentalMapper;
    }

    @PostMapping
    public ResponseEntity<RentalResponseDTO> createRental(
            @Valid @RequestBody RentalRequestDTO requestDTO) {
        Rental createdRental = rentalService.createRental(requestDTO);
        RentalResponseDTO response = rentalMapper.toDto(createdRental);
        URI location = URI.create("/api/rentals/" + createdRental.getId());
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<RentalResponseDTO>> getClientRentals(
            @PathVariable Long clientId) {
        return ResponseEntity.ok(
                rentalService.getClientRentals(clientId).stream()
                        .map(rentalMapper::toDto)
                        .toList()
        );
    }

    @GetMapping
    public ResponseEntity<Page<RentalResponseDTO>> getAllRentals(Pageable pageable) {
        return ResponseEntity.ok(
                rentalService.findAll(pageable)
                        .map(rentalMapper::toDto)
        );
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<RentalResponseDTO> cancelRental(@PathVariable Long id) {
        Rental canceledRental=rentalService.cancelRental(id);
        RentalResponseDTO response = rentalMapper.toDto(canceledRental);
        URI location = URI.create("/api/rentals/" + canceledRental.getId());
        return ResponseEntity.created(location).body(response);
    }
}
