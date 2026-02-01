package io.github.dainadb.improplan.domain.eventdate.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import io.github.dainadb.improplan.domain.eventdate.dto.EventDateResponseDto;
import io.github.dainadb.improplan.domain.eventdate.entity.EventDate;

/**
 * Interfaz para el servicio que gestiona las fechas de los eventos (EventDate).
 */
public interface IEventDateService {

    /**
     * Para un conjunto de fechas (LocalDate), busca las entidades EventDate correspondientes en la base de datos.
     * Si una fecha no existe, crea una nueva entidad EventDate y la guarda en la base de datos.
     *
     * @param dates Conjunto de LocalDate a procesar.
     * @return Un conjunto de entidades EventDate persistidas.
     */
    Set<EventDate> findOrCreateDates(Set<LocalDate> dates);

      /**
     * Devuelve todas las fechas asociadas a un evento específico, ordenadas cronológicamente.
     *
     * @param eventId El ID del evento.
     * @return Una lista de DTOs de las fechas del evento.
     */
    List<EventDateResponseDto> getAllDatesByEventId(Long eventId);

    /**
     * Devuelve solo las fechas futuras (o de hoy) de un evento específico, ordenadas cronológicamente.
     *
     * @param eventId El ID del evento.
     * @return Una lista de DTOs de las fechas futuras del evento.
     */
    List<EventDateResponseDto> getUpcomingDatesByEventId(Long eventId);
}
