package io.github.dainadb.improplan.domain.event.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.github.dainadb.improplan.common.response.ApiResponse;
import io.github.dainadb.improplan.domain.event.dto.EventRequestDto;
import io.github.dainadb.improplan.domain.event.dto.EventResponseDto;
import io.github.dainadb.improplan.domain.event.entity.Event.StatusType;
import io.github.dainadb.improplan.domain.event.service.IEventService;
import io.github.dainadb.improplan.domain.generic.controller.GenericRestController;


/**
 * Controlador REST para gestionar las operaciones relacionadas con los eventos.
 */
//Nota: no he protegido los endpoints con @PreAuthorize aquí, sino en la clase de configuración de seguridad, para centralizar la gestión de permisos.
//No he creado endpoints con todos los métodos del servicio, quiero ver cuales necesito realmente en el front.
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/events")
public class EventRestController extends GenericRestController {

    @Autowired
    private IEventService eventService;


    /**
     * Crea un nuevo evento.
     * El evento se asigna al usuario autenticado y se crea con estado PENDING.
     * @param eventRequestDto DTO con los datos del evento a crear.
     * @return ResponseEntity con el evento creado.
     */
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<EventResponseDto>> createEvent(@RequestBody EventRequestDto eventRequestDto) {
        String userEmail = getEmail();
        EventResponseDto createdEvent = eventService.createEvent(eventRequestDto, userEmail);
        return created(createdEvent, "Evento creado exitosamente y pendiente de revisión.");
    }

    /**
     * Obtiene un evento por su ID.
     * @param id ID del evento.
     * @return ResponseEntity con el evento encontrado.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EventResponseDto>> getEventById(@PathVariable Long id) {
        EventResponseDto event = eventService.findById(id);
        return success(event, "Evento encontrado.");
    }

    /**
     * Actualiza un evento existente.
     * @param id              ID del evento a actualizar.
     * @param eventRequestDto DTO con los nuevos datos del evento.
     * @return ResponseEntity con el evento actualizado.
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<EventResponseDto>> updateEvent(@PathVariable Long id, @RequestBody EventRequestDto eventRequestDto) {
        EventResponseDto updatedEvent = eventService.updateEvent(id, eventRequestDto);
        return success(updatedEvent, "Evento actualizado correctamente.");
    }

    /**
     * Realiza un borrado lógico (soft delete) de un evento, cambiando su estado a DISCARDED.
     *
     * @param id ID del evento a descartar.
     * @return ResponseEntity sin contenido.
     */
    @DeleteMapping("/softdelete/{id}")
    public ResponseEntity<ApiResponse<Void>> softDeleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return success(null, "Evento marcado como descartado.");
    }

    /**
     * Realiza un borrado físico (hard delete) de un evento (solo para eventos descartados).
     * @param id ID del evento a eliminar permanentemente.
     * @return ResponseEntity sin contenido.
     */
    @DeleteMapping("/harddelete/{id}")
    public ResponseEntity<ApiResponse<Void>> hardDeleteEvent(@PathVariable Long id) {
        eventService.hardDeleteEvent(id);
        return success(null, "Evento eliminado permanentemente.");
    }


    /**
     * Publica un evento cambiando su estado a PUBLISHED.
     * @param id ID del evento a publicar.
     * @return ResponseEntity sin contenido.
     */
    @PatchMapping("/publish/{id}")
    public ResponseEntity<ApiResponse<Void>> publishEvent(@PathVariable Long id) {
        eventService.publishEvent(id);
        return success(null, "Evento publicado correctamente.");
    }

    /**
     * Obtiene todos los eventos vigentes según su estado.
     * @param status Estado de los eventos a recuperar (PENDING o PUBLISHED).
     * @return  Lista de eventos vigentes
     */
    @GetMapping("/intime/status")
    public ResponseEntity<ApiResponse<List<EventResponseDto>>> getEventsStatusInTime(@RequestParam String status) {
        List<EventResponseDto> events = eventService.findByInTimeAndStatus(true, status);
        return success(events, "Eventos vigentes recuperados con estado " + status + ".");
    }

    /**
     * Obtiene todos los eventos descartados.
     * @return Lista de eventos descartados.
     */
    @GetMapping("/discarded")
    public ResponseEntity<ApiResponse<List<EventResponseDto>>> getEventDiscarded() {
        List<EventResponseDto> events = eventService.findByStatus(StatusType.DISCARDED.name());
        return success(events, "Eventos descartados recuperados.");
    }


    /**
     * Obtiene  los eventos no descartados que han pasado su fecha (fuera de tiempo).
     * @return Lista de eventos fuera de tiempo.
     */
    @GetMapping("/outtime")
    public ResponseEntity<ApiResponse<List<EventResponseDto>>> outTimeEventNotDiscarded(){
        List<EventResponseDto> events = eventService.findOutTimeAndNotDiscarded(false, List.of(StatusType.PUBLISHED.name(), StatusType.PENDING.name()));
        return success(events, "Eventos fuera de tiempo recuperados.");
    }


    /**
     * Obtiene el número total de eventos pendientes.
     * @return Conteo de eventos con estado PENDING.
     */
    @GetMapping("/count/pending")
    public ResponseEntity<ApiResponse<Long>> countPendingEvents() {
        long count = eventService.countEventsByStatus(StatusType.PENDING.name());
        return success(count, "Conteo de eventos pendientes");
    }

    /**
     * Obtiene el número total de eventos descartados.
     * @return Conteo de eventos con estado DISCARDED.
     */
    @GetMapping("/count/discarded")
    public ResponseEntity<ApiResponse<Long>> countDiscardedEvents() {
        long count = eventService.countEventsByStatus(StatusType.DISCARDED.name());
        return success(count, "Conteo de eventos descartados");
    }


    /**
     * Obtiene el número total de eventos que han pasado su fecha (fuera de tiempo).
     * @return Conteo de eventos con inTime = false.
     */
    @GetMapping("/count/outtime")
    public ResponseEntity<ApiResponse<Long>> countOutTimeEvents() {
        long count = eventService.countEventsByInTime(false);
        return success(count, "Conteo de eventos fuera de tiempo");
    }

    
    /**
     * Búsqueda avanzada de eventos publicados y vigentes.
     *
     * @param provinceName     Nombre de la provincia (obligatorio).
     * @param eventDate        Fecha del evento (obligatorio). Se usa el @DateTimeFormat para asegurar el formato correcto.
     * @param themeName        Nombre de la temática (opcional).
     * @param municipalityName Nombre del municipio (opcional).
     * @param maxPrice         Precio máximo (opcional).
     * @return Lista de eventos que cumplen con los criterios de búsqueda.
     */
    @GetMapping("/filters")
    public ResponseEntity<ApiResponse<List<EventResponseDto>>> searchPublishedEvents(
            @RequestParam String provinceName,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate eventDate,
            @RequestParam(required = false) String themeName,
            @RequestParam(required = false) String municipalityName,
            @RequestParam(required = false) BigDecimal maxPrice) {
        
        List<EventResponseDto> events = eventService.searchPublishedEvents(provinceName, eventDate, themeName, municipalityName, maxPrice);
        return success(events, "Resultados de la búsqueda de eventos.");
    }


    /**
     * Obtiene los eventos publicados (estado PUBLISHED) favoritos de un usuario.
     *
     * @param userId ID del usuario.
     * @return Lista de eventos favoritos.
     */
    //El atributo inTime se usará en la interfaz para inhabilitar los eventos que ya hayan pasado.
    //El usuario verá en su lista de favoritos que los eventos que hayan caducado saldrán inhabilitados (sin fechas), no podrá acceder a ellos, solo eliminarlos de su lista.
    @GetMapping("/favorites/user/{userId}")
    public ResponseEntity<ApiResponse<List<EventResponseDto>>> getFavoriteEventsByUser(@PathVariable Long userId) {
        List<EventResponseDto> favoriteEvents = eventService.findFavoriteEventsByUser(userId);
        return success(favoriteEvents, "Eventos favoritos del usuario recuperados.");
    }

    
        /**
     * Busca eventos por el email del usuario creador.
     *
     * @param email Email del usuario.
     * @return Lista de eventos creados por ese usuario.
     */
    @GetMapping("/user/{email}")
    public ResponseEntity<ApiResponse<List<EventResponseDto>>> getEventsByUserEmail(@PathVariable String email) {
        List<EventResponseDto> events = eventService.findByUserEmail(email);
        return success(events, "Eventos encontrados para el usuario " + email);
    }

    
}