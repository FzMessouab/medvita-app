package com.medvita.backend.mappers;

/*import com.medvita.backend.dto.DeliveryResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface DeliveryMapper extends GenericMapper<Delivery, DeliveryResponseDTO> {

    @Mapping(target = "trackingUrl",
            expression = "java(entity.getTrackingInfo() != null ? \"https://tracking.service/?id=\" + entity.getTrackingInfo() : null)")
    DeliveryResponseDTO toDto(Delivery entity);
}*/