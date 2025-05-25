package com.medvita.backend.mappers;

import com.medvita.backend.dto.RentalRequestDTO;
import com.medvita.backend.dto.RentalResponseDTO;
import com.medvita.backend.entities.Rental;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Mapper(componentModel = "spring", uses = {ClientMapper.class, EquipmentMapper.class})
public abstract class RentalMapper implements AbstractMapper<Rental, RentalResponseDTO,Long> {

    @Autowired
    protected ClientMapper clientMapper;

    @Autowired
    protected EquipmentMapper equipmentMapper;


    public abstract RentalResponseDTO toDto(Rental entity);


    public abstract Rental toEntity(RentalRequestDTO dto);

    protected BigDecimal calculateTotalAmount(Rental rental) {
        long days = rental.getStartDate().until(rental.getEndDate()).getDays() + 1;

        BigDecimal dailyPrice = BigDecimal.valueOf(rental.getEquipment().getDailyRentalPrice());
        BigDecimal quantity = BigDecimal.valueOf(rental.getQuantity());
        BigDecimal daysBD = BigDecimal.valueOf(days);

        return dailyPrice.multiply(daysBD).multiply(quantity);
    }

}
