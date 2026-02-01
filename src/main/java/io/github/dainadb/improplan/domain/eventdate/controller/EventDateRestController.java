package io.github.dainadb.improplan.domain.eventdate.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.dainadb.improplan.common.response.ApiResponse;
import io.github.dainadb.improplan.domain.eventdate.dto.EventDateResponseDto;
import io.github.dainadb.improplan.domain.eventdate.service.IEventDateService;
import io.github.dainadb.improplan.domain.generic.controller.GenericRestController;

/**
 * Controlador REST para consultar las fechas de un evento específico.
 * Estos endpoints son de solo lectura, ya que la gestión de fechas se realiza
 * a través de la creación/actualización del evento principal.
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/events/{eventId}/dates")
public class EventDateRestController extends GenericRestController {

    @Autowired
    private IEventDateService eventDateService;

    /**
     * Obtiene la lista completa de todas las fechas asociadas a un evento, ordenadas cronológicamente.
     *
     * @param eventId El ID del evento para el cual se solicitan las fechas.
     * @return ResponseEntity con la lista de fechas y un mensaje de éxito.
     */
   
    @GetMapping
    public ResponseEntity<ApiResponse<List<EventDateResponseDto>>> getAllDatesForEvent(@PathVariable Long eventId) {
        List<EventDateResponseDto> dates = eventDateService.getAllDatesByEventId(eventId);
        return success(dates, "Fechas del evento obtenidas con éxito.");
    }

    /**
     * Obtiene solo las fechas futuras (y de hoy) de un evento específico, ordenadas cronológicamente.
     * Es útil para mostrar al usuario solo las próximas fechas disponibles.
     *
     * @param eventId El ID del evento para el cual se solicitan las fechas futuras.
     * @return ResponseEntity con la lista de fechas futuras y un mensaje de éxito.
     */
   
    @GetMapping("/upcoming")
    public ResponseEntity<ApiResponse<List<EventDateResponseDto>>> getUpcomingDatesForEvent(@PathVariable Long eventId) {
        List<EventDateResponseDto> dates = eventDateService.getUpcomingDatesByEventId(eventId);
        return success(dates, "Próximas fechas del evento obtenidas con éxito.");
    }
}
