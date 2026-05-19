package com.medvita.backend.services;

import com.medvita.backend.dto.EquipmentRequestDTO;
import com.medvita.backend.dto.EquipmentResponseDTO;
import com.medvita.backend.entities.Equipment;
import com.medvita.backend.repositories.EquipmentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EquipmentService extends AbstractService<Equipment,
        Long,
        EquipmentRequestDTO,
        EquipmentResponseDTO,
        EquipmentRepository> {

    private final EquipmentRepository equipmentRepository;

    public EquipmentService(EquipmentRepository equipmentRepository) {
        super(equipmentRepository);
        this.equipmentRepository = equipmentRepository;
    }

    public Page<Equipment> getAvailableEquipment(Pageable pageable) {
        return equipmentRepository.findAllByActiveTrue(pageable);
    }

    public Page<Equipment> searchEquipment(String query, Pageable pageable) {
        return equipmentRepository.searchActiveEquipment(query, pageable);
    }

    public List<Equipment> getAllAvailableEquipment() {
        return equipmentRepository.findAllByActiveTrue();
    }

    public List<String> getAllCategories() {
        return equipmentRepository.findAllActiveCategories();
    }

    public Equipment getByName(String name) {
        return equipmentRepository.findActiveByName(name)
                .orElseThrow(() -> new RuntimeException("Equipement non trouve: " + name));
    }

    public void checkEquipmentAvailability(Long equipmentId, int quantity) {
        Equipment equipment = getById(equipmentId);
        if (equipment.getStockQuantity() < quantity) {
            throw new RuntimeException(
                    "Stock insuffisant pour l'équipement: " + equipment.getName());
        }
    }
}
