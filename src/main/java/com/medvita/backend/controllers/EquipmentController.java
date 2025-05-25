package com.medvita.backend.controllers;

import com.medvita.backend.dto.EquipmentRequestDTO;
import com.medvita.backend.dto.EquipmentResponseDTO;
import com.medvita.backend.entities.Equipment;
import com.medvita.backend.mappers.EquipmentMapper;
import com.medvita.backend.services.EquipmentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
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


}