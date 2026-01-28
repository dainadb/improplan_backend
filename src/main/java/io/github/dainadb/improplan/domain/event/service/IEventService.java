package io.github.dainadb.improplan.domain.event.service;

import java.time.LocalDate;
import java.util.List;

import io.github.dainadb.improplan.domain.event.dto.EventRequestDto;
import io.github.dainadb.improplan.domain.event.dto.EventResponseDto;
import io.github.dainadb.improplan.domain.event.entity.Event;
import io.github.dainadb.improplan.domain.event.entity.Event.StatusType;
import io.github.dainadb.improplan.exception.NotFoundException;


/**
 * Interfaz para el servicio de gestión de Eventos.
 * Define las operaciones de negocio para crear, buscar, leer y eliminar eventos.
 */

//Tiene una lógica de negocio compleja que no encaja bien con el CRUD genérico, por eso no extiende de este.
public interface IEventService {

 /**
     * Crea un nuevo evento asociándolo al usuario autenticado.
     *
     * @param eventDto DTO con los datos del evento a crear.
     * @param userEmail Email del usuario que está creando el evento.
     * @return DTO con la respuesta del evento recién creado.
     */
    EventResponseDto createEvent(EventRequestDto eventDto, String userEmail);

    /**
     * Busca un evento por su ID y lo devuelve como un DTO de respuesta.
     *
     * @param id El ID del evento a buscar.
     * @return DTO de respuesta del evento.
     * @throws NotFoundException si no se encuentra el evento.
     */
    EventResponseDto findById(Long id);

    /**
     * Devuelve una lista de todos los eventos como DTOs de respuesta.
     *
     * @return Lista de DTOs de eventos.
     */
    List<EventResponseDto> findAll();

    /**
     * Realiza un borrado lógico de un evento, cambiando su estado a 'DISCARDED'.
     * El evento no se elimina de la base de datos.
     *
     * @param id El ID del evento a descartar.
     * @throws NotFoundException si no se encuentra el evento.
     */
    void deleteEvent(Long id);

    /**
     * Realiza un borrado físico y permanente de un evento de la base de datos.
     * Esta acción es irreversible y solo puede hacerse si el estado previo del evento es 'DISCARDED'.
     *
     * @param id El ID del evento a eliminar permanentemente.
     * @throws NotFoundException si el evento no existe antes de intentar borrarlo.
     */
    void hardDeleteEvent(Long id);

    /**
     * Publica un evento, cambiando su estado a 'PUBLISHED'.
     * @param id    ID del evento a publicar
     */
    void publishEvent(Long id);

    /**
     * Actualiza un evento existente.   
     * @param id  ID del evento a actualizar
     * @param eventDto DTO con los datos actualizados del evento
     * @return  DTO con la información del evento actualizado
     */
    EventResponseDto updateEvent(Long id, EventRequestDto eventDto);


    //MÉTODOS DE BÚSQUEDA ESPECÍFICA

    /**
     * Busca eventos cuyo nombre contenga la cadena dada, ignorando mayúsculas y minúsculas.
     * @param name Cadena a buscar en los nombres de los eventos.
     */
    List<Event> findByContainingName(String name);

    /**
     * Busca eventos en municipio específico.
     * @param name Nombre del municipio.
     */
    List<Event> findByMunicipalityName(String name);

    /**
     * Busca eventos en provincia específica.
     * @param name Nombre de la provincia.
     */
    List<Event> findByProvinceName(String name);

    /**
    * Busca eventos en comunidad autónoma específica.
    * @param name Nombre de la comunidad autónoma.
    */
    List<Event> findByAutonomousCommunityName (String name);

    /**
     * Busca eventos de una temática específica.
     * @param name Nombre de la temática.
     */
    List<Event> findByThemeName(String name); 

    /**
     * Busca eventos según si son gratuitos o no.
     * @param isFree true para eventos gratuitos, false para de pago.
     */
    List<Event> findByIsFree(Boolean isFree);

    /**
     * Busca eventos por su precio máximo.
     * @param maxPrice Precio máximo.
     */
    List<Event> findByMaxPrice(Double maxPrice);

    /**
     * Busca eventos por una fecha específica.
     * @param fullDate Fecha completa.
     */
    List<Event> findByDate(LocalDate fullDate);
    
    /**
     * Busca eventos por su estado.
     * @param status Estado del evento.
     */
    List<Event> findByStatus(StatusType status);

    /**
     * Busca eventos según si están a tiempo o no.
     * @param inTime true para eventos a tiempo, false para eventos pasados.
     */
    List<Event> findByInTime(Boolean inTime);

    /**
     * Busca eventos según si están a tiempo y su estado.
     * @param inTime true para eventos a tiempo, false para eventos pasados.
     * @param status Estado del evento.
     */
    List<Event> findByInTimeAndStatus(Boolean inTime, StatusType status);

    /**
     * Busca eventos asociados a un usuario específico por su email.
     * @param email Email del usuario.
     */
    List<Event> findByUserEmail (String email);

    /**
    * Obtiene los eventos favoritos de un usuario específico.
    * @param userId ID del usuario.
    */
    List<Event> findFavoriteEventsByUser(Long userId);

    /**
     * Busca eventos publicados que coincidan con los criterios de búsqueda proporcionados. 
     * @param communityName Nombre de la comunidad autónoma (obligatorio)
     * @param provinceName Nombre de la provincia. (obligatorio)
     * @param eventDate Fecha del evento. (obligatorio)
     * @param themeName Nombre de la temática. (opcional)
     * @param municipalityName Nombre del municipio. (opcional)
     * @param maxPrice Precio máximo. (opcional)
     * @return
     */
     List<Event> searchPublishedEvents( String communityName,
                                        String provinceName,
                                        LocalDate eventDate,
                                        String themeName,
                                        String municipalityName,
                                        Double maxPrice
                                        );

   

}
