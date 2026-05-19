package com.medvita.backend.controllers;

import com.medvita.backend.services.EquipmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final EquipmentService equipmentService;

    public CategoryController(EquipmentService equipmentService) {
        this.equipmentService = equipmentService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<String>> getAllCategories() {
        return ResponseEntity.ok(equipmentService.getAllCategories());
    }
}
