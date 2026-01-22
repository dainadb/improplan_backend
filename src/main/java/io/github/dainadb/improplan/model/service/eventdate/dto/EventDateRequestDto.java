package io.github.dainadb.improplan.model.service.eventdate.dto;

import java.time.LocalDate;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO que representa los datos requeridos para crear o actualizar una fecha de evento.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventDateRequestDto {
    private LocalDate fullDate;
}
