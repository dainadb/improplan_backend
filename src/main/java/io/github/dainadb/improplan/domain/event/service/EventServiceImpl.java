package io.github.dainadb.improplan.domain.event.service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import io.github.dainadb.improplan.common.utils.Validator;
import io.github.dainadb.improplan.domain.event.dto.EventRequestDto;
import io.github.dainadb.improplan.domain.event.dto.EventResponseDto;
import io.github.dainadb.improplan.domain.event.entity.Event;
import io.github.dainadb.improplan.domain.event.entity.Event.StatusType;
import io.github.dainadb.improplan.domain.event.repository.IEventRepository;
import io.github.dainadb.improplan.domain.eventdate.entity.EventDate;
import io.github.dainadb.improplan.domain.eventdate.service.IEventDateService;
import io.github.dainadb.improplan.domain.favorite.repository.IFavoriteRepository;
import io.github.dainadb.improplan.domain.municipality.entity.Municipality;
import io.github.dainadb.improplan.domain.municipality.repository.IMunicipalityRepository;
import io.github.dainadb.improplan.domain.theme.entity.Theme;
import io.github.dainadb.improplan.domain.theme.repository.IThemeRepository;
import io.github.dainadb.improplan.domain.user.entity.User;
import io.github.dainadb.improplan.domain.user.repository.IUserRepository;
import io.github.dainadb.improplan.exception.BadRequestException;
import io.github.dainadb.improplan.exception.NotFoundException;
import io.github.dainadb.improplan.exception.ValidationException;
import jakarta.transaction.Transactional;



//No hereda de ningún CRUD genérico porque tiene lógica de negocio específica.
@Service
public class EventServiceImpl  implements IEventService {

    @Autowired
    private IEventRepository eventRepository;

    @Autowired
    private IMunicipalityRepository municipalityRepository;

    @Autowired
    private IThemeRepository themeRepository;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IEventDateService eventDateService;

    @Autowired
    private IFavoriteRepository favoriteRepository;

    @Autowired
    private ModelMapper modelMapper;


    // MÉTODOS CRUD PRINCIPALES 

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public EventResponseDto createEvent(EventRequestDto dto, String userEmail) {
        
        validateEventRequest(dto);

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new NotFoundException("Usuario creador del evento no encontrado con email: " + userEmail));
        Municipality municipality = municipalityRepository.findByName(dto.getMunicipalityName())
                .orElseThrow(() -> new NotFoundException("Municipio no encontrado: " + dto.getMunicipalityName()));
        Theme theme = themeRepository.findByNameIgnoreCase(dto.getThemeName())
                .orElseThrow(() -> new NotFoundException("Temática no encontrada: " + dto.getThemeName()));
        
        //Se buscan o crean las fechas que trae el RequestDto
        Set<EventDate> eventDates = eventDateService.findOrCreateDates(dto.getEventDates());

        Event event = modelMapper.map(dto, Event.class);
        //Aplicación manual de la relaciones complejas
        event.setUser(user);
        event.setMunicipality(municipality);
        event.setTheme(theme);
        event.setDates(eventDates);
        event.setStatus(StatusType.PENDING); // Estado inicial por defecto
        event.setInTime(true); // Siempre 'en tiempo' al crearse

        Event savedEvent = eventRepository.save(event);

        return modelMapper.map(savedEvent, EventResponseDto.class);
    }
    


    /**
     * {@inheritDoc}    
     */
    @Override
    @Transactional
    public EventResponseDto updateEvent(Long id, EventRequestDto dto) {
        
        validateEventRequest(dto);

        Event existingEvent = eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Evento no encontrado con ID: " + id));


        Municipality municipality = municipalityRepository.findByName(dto.getMunicipalityName())
                .orElseThrow(() -> new NotFoundException("Municipio no encontrado: " + dto.getMunicipalityName()));
        Theme theme = themeRepository.findByNameIgnoreCase(dto.getThemeName())
                .orElseThrow(() -> new NotFoundException("Temática no encontrada: " + dto.getThemeName()));

        Set<EventDate> eventDates = eventDateService.findOrCreateDates(dto.getEventDates());

        //Actualización atributo inTime según las nuevas fechas
        // Un evento está vigente si al menos una de sus fechas es hoy o en el futuro.
        boolean isNowInTime = dto.getEventDates().stream()
                .anyMatch(date -> date.isAfter(LocalDate.now()));
        
        existingEvent.setInTime(isNowInTime);

        // Usa ModelMapper para mapear los campos simples del DTO sobre la entidad existente.
        // Gracias a la configuración con skip(), ModelMapper no tocará las relaciones (user, theme, etc.).
        modelMapper.map(dto, existingEvent);

        // Asigna manualmente las relaciones complejas que acabamos de obtener.
        // El campo 'user' no se actualiza, ya que el creador del evento no cambia
        existingEvent.setMunicipality(municipality);
        existingEvent.setTheme(theme);
        existingEvent.setDates(eventDates);

        Event updatedEvent = eventRepository.save(existingEvent);

        return modelMapper.map(updatedEvent, EventResponseDto.class);
    }



    /**
     * {@inheritDoc}
     */
    @Override
    public EventResponseDto findById(Long id) {
        return eventRepository.findById(id)
                .map(event -> modelMapper.map(event, EventResponseDto.class))
                .orElseThrow(() -> new NotFoundException("Evento no encontrado con ID: " + id));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<EventResponseDto> findAll() {
        return eventRepository.findAll().stream()
               .map(event -> modelMapper.map(event, EventResponseDto.class))
               .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void publishEvent(Long id) {
        Event event = eventRepository.findById(id)  
                .orElseThrow(() -> new NotFoundException("No se puede publicar el evento. No encontrado con ID: " + id));
       event.setStatus(StatusType.PUBLISHED);
       eventRepository.save(event);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void deleteEvent(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("No se puede descartar el evento. No encontrado con ID: " + id));
        event.setStatus(StatusType.DISCARDED);

        //Al cambiar a descartado, se eliminan todos los favoritos asociados a ese evento
        favoriteRepository.deleteByEventId(id);

        eventRepository.save(event);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void hardDeleteEvent(Long id) {
        
        Event event = eventRepository.findById(id).orElseThrow(() -> new NotFoundException("No se ha encontrado el evento con id: " + id));

        if (event.getStatus() != StatusType.DISCARDED) {
            throw new ValidationException("Solo se pueden eliminar eventos que estén en estado DESCARTADO.");
        }

        eventRepository.deleteById(id);
    }



    // MÉTODOS DE BÚSQUEDA ESPECÍFICA 
    // (Implementación de todos los métodos de búsqueda de la interfaz)


    /**
     * {@inheritDoc}
     */
    @Override
    public List<EventResponseDto> findByContainingName(String name) {
        return eventRepository.findByNameContainingIgnoreCase(name).stream()
                .map(this::convertToResponseDto)
                .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<EventResponseDto> findByMunicipalityName(String name) {
        return eventRepository.findByMunicipalityNameIgnoreCase(name).stream()
                .map(this::convertToResponseDto)
                .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<EventResponseDto> findByProvinceName(String name) {
        return eventRepository.findByProvinceNameIgnoreCase(name).stream()
                .map(this::convertToResponseDto)
                .toList();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<EventResponseDto> findByThemeName(String name) {
        return eventRepository.findByThemeNameIgnoreCase(name).stream()
                .map(this::convertToResponseDto)
                .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<EventResponseDto> findByIsFree(Boolean isFree) {
        return eventRepository.findByIsFree(isFree).stream()
                .map(this::convertToResponseDto)
                .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<EventResponseDto> findByMaxPrice(Double maxPrice) {
        return eventRepository.findByPriceLessThanEqual(maxPrice).stream()
                .map(this::convertToResponseDto)
                .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<EventResponseDto> findByDate(LocalDate fullDate) {
        return eventRepository.findByDatesFullDate(fullDate).stream()
                .map(this::convertToResponseDto)
                .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<EventResponseDto> findByStatus(String status) {
        try{ //COnversión de String a enum
            StatusType statusType = StatusType.valueOf(status.toUpperCase());
            return eventRepository.findByStatus(statusType).stream()
                    .map(this::convertToResponseDto)
                    .toList();
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Estado de evento inválido: " + status);
        }
        
       
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<EventResponseDto> findByInTime(Boolean inTime) {
        return eventRepository.findByInTime(inTime).stream()
                .map(this::convertToResponseDto)
                .toList();
    }

    /**
     *{@inheritDoc}
     */
    @Override
    public List<EventResponseDto> findByInTimeAndStatus(Boolean inTime, String status) {
       try{
            StatusType statusType = StatusType.valueOf(status.toUpperCase());
            return eventRepository.findByInTimeAndStatus(inTime, statusType).stream()
                .map(this::convertToResponseDto)
                .toList();
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Estado de evento inválido: " + status);
       }
        
    }

    /**
     * {@inheritDoc}
     */
    @Override
     public List<EventResponseDto> findOutTimeAndNotDiscarded(Boolean inTime, Collection<String> statuses) {
        try{
            //Convertimos los String a StatusType
            Set<StatusType> statusTypes = statuses.stream()
                    .map(status -> StatusType.valueOf(status.toUpperCase()))
                    .collect(Collectors.toSet());
            
            return eventRepository.findByInTimeAndStatusIn(false, statusTypes).stream()
                    .map(this::convertToResponseDto)
                    .toList();
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Uno o más estados de evento inválidos en la lista proporcionada.");
        }

    }

    /**
     * {@inheritDoc}
     */

    @Override
     public long countEventsByStatus(String status) {
        try {
            StatusType statusType = StatusType.valueOf(status.toUpperCase());
            return eventRepository.countByStatus(statusType);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Estado de evento inválido: " + status);
        }
     }

    /**
     * {@inheritDoc}
     */
     @Override
     public long countEventsByInTime(Boolean inTime) {
        return eventRepository.countByInTime(inTime);
     }

     
    /**
     * {@inheritDoc}
     */
    @Override
    public List<EventResponseDto> findByUserEmail(String email) {
        return eventRepository.findByUserEmail(email).stream()
                .map(this::convertToResponseDto)
                .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<EventResponseDto> findFavoriteEventsByUser(Long userId) {
        return eventRepository.findFavoriteEventsByUserId(userId).stream()
                .map(this::convertToResponseDto)
                .toList();
    }

    /**
     * {@inheritDoc}
     */
    //Solo se mostrarán los eventos que estén publicados y vigentes y que cumplan los filtros
    @Override
    public List<EventResponseDto> searchPublishedEvents( String provinceName, LocalDate eventDate,
            String themeName, String municipalityName, Double maxPrice) {
            List<Event> events = eventRepository.searchPublishedEvents(provinceName.toLowerCase(),eventDate,
                                                    themeName != null ? themeName.toLowerCase() : null, //Indicamos que si no es null, se pase en minúsculas y si es null, se pase como null
                                                    municipalityName != null ? municipalityName.toLowerCase() : null,
                                                    maxPrice);
        return events.stream()
                     .map(this::convertToResponseDto)
                     .toList() ;
    }

   
 

     // MÉTODO PRIVADOS DE VALIDACIÓN Y UTILIDAD
     /**
      * Valida los datos del DTO de solicitud de evento.
      * @param dto DTO de solicitud de evento
      * @throws ValidationException si algún dato no es válido.
      */
    private void validateEventRequest(EventRequestDto dto) {
        if (Boolean.TRUE.equals(dto.getIsFree()) && dto.getPrice() != null && dto.getPrice() > 0) {
            throw new ValidationException("Un evento gratuito no puede tener precio.");
        }
        if (dto.getInfoUrl() != null && !dto.getInfoUrl().isEmpty() && !Validator.isValidUrl(dto.getInfoUrl())) {
            throw new ValidationException("La URL de información no es válida.");
        }
        if (dto.getEventDates() == null || dto.getEventDates().isEmpty()) {
            throw new ValidationException("El evento debe tener al menos una fecha.");
        }
        boolean hasPastDates = dto.getEventDates().stream().anyMatch(date -> !date.isAfter(LocalDate.now()));
        if (hasPastDates) {
            throw new ValidationException("No se pueden crear eventos con fechas pasadas o la de hoy.");
        }
    }

    private EventResponseDto convertToResponseDto(Event event) {
        return modelMapper.map(event, EventResponseDto.class);
    }

}
