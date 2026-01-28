package io.github.dainadb.improplan.domain.eventdate.dto;


import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO que representa los datos de respuesta de una fecha de evento.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventDateResponseDto {
    private Long id;
    private LocalDate fullDate;
}
