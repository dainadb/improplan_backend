package io.github.dainadb.improplan.domain.event.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import io.github.dainadb.improplan.domain.event.entity.Event;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de respuesta para representar los datos de un evento que se envían en la respuesta al cliente.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventResponseDto {
    private Long id;
    private String name;
    private String summary;
    private String description;
    private String placeName;
    private String address;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String image;
    private String infoUrl;
    private Boolean isFree;
    private BigDecimal price;
    private Boolean inTime; 
    private Event.StatusType status;
    private String municipalityName;
    private String themeName;
    private Set<LocalDate> eventDates;
}
