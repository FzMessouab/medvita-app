package com.medvita.backend.controllers;

import com.medvita.backend.dto.EquipmentRequestDTO;
import com.medvita.backend.dto.EquipmentResponseDTO;
import com.medvita.backend.entities.Equipment;
import com.medvita.backend.mappers.EquipmentMapper;
import com.medvita.backend.services.EquipmentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/equipment")
public class EquipmentController extends AbstractController<
        Equipment,
        Long,
        EquipmentRequestDTO,
        EquipmentResponseDTO,
        EquipmentService
        > {

    private final EquipmentService equipmentService;
    private final EquipmentMapper equipmentMapper;

    public EquipmentController(EquipmentService equipmentService, EquipmentMapper equipmentMapper) {
       super(equipmentService);
        this.equipmentService = equipmentService;
        this.equipmentMapper = equipmentMapper;
    }

    @GetMapping("/available")
    public ResponseEntity<List<EquipmentResponseDTO>> getAvailableEquipment() {
        return ResponseEntity.ok(
                equipmentService.getAllAvailableEquipment().stream()
                        .map(equipmentMapper::toDto)
                        .toList()
        );
    }

    @GetMapping("/search")
    public ResponseEntity<Page<EquipmentResponseDTO>> searchEquipment(
            @RequestParam String query,
            Pageable pageable) {
        Page<Equipment> equipment = equipmentService.searchEquipment(query, pageable);
        return ResponseEntity.ok(equipment.map(equipmentMapper::toDto));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<EquipmentResponseDTO>> getByCategory(
            @PathVariable String category) {
        return ResponseEntity.ok(
                equipmentService.searchEquipment(category,Pageable.ofSize(20)).stream()
                        .map(equipmentMapper::toDto)
                        .toList()
        );
    }

    @GetMapping("/categories/all")
    public ResponseEntity<List<String>> getAllCategories() {
        return ResponseEntity.ok(equipmentService.getAllCategories());
    }

    @GetMapping("/categories")
    public ResponseEntity<List<EquipmentResponseDTO>> getByCategoriesQuery(
            @RequestParam("categories") String category) {
        return ResponseEntity.ok(
                equipmentService.searchEquipment(category, Pageable.ofSize(20)).stream()
                        .map(equipmentMapper::toDto)
                        .toList()
        );
    }

    @GetMapping("/details/{name}")
    public ResponseEntity<EquipmentResponseDTO> getEquipmentDetails(
            @PathVariable String name) {
        try {
            return ResponseEntity.ok(equipmentMapper.toDto(equipmentService.getByName(name)));
        } catch (RuntimeException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, exception.getMessage(), exception);
        }
    }


}
