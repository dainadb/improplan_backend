package io.github.dainadb.improplan.domain.event.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

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
import io.github.dainadb.improplan.domain.municipality.entity.Municipality;
import io.github.dainadb.improplan.domain.municipality.repository.IMunicipalityRepository;
import io.github.dainadb.improplan.domain.theme.entity.Theme;
import io.github.dainadb.improplan.domain.theme.repository.IThemeRepository;
import io.github.dainadb.improplan.domain.user.entity.User;
import io.github.dainadb.improplan.domain.user.repository.IUserRepository;
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
        Theme theme = themeRepository.findByName(dto.getThemeName())
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
        Theme theme = themeRepository.findByName(dto.getThemeName())
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
    public List<Event> findByContainingName(String name) {
        return eventRepository.findByNameContainingIgnoreCase(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Event> findByMunicipalityName(String name) {
        return eventRepository.findByMunicipalityNameIgnoreCase(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Event> findByProvinceName(String name) {
        return eventRepository.findByProvinceNameIgnoreCase(name);
    }

    /**
     *{@inheritDoc}
     */
    @Override
    public List<Event> findByAutonomousCommunityName(String name) {
        return eventRepository.findByAutonomousCommunityNameIgnoreCase(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Event> findByThemeName(String name) {
        return eventRepository.findByThemeNameIgnoreCase(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Event> findByIsFree(Boolean isFree) {
        return eventRepository.findByIsFree(isFree);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Event> findByMaxPrice(Double maxPrice) {
        return eventRepository.findByPriceLessThanEqual(maxPrice);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Event> findByDate(LocalDate fullDate) {
        return eventRepository.findByDatesFullDate(fullDate);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Event> findByStatus(StatusType status) {
        return eventRepository.findByStatus(status);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Event> findByInTime(Boolean inTime) {
        return eventRepository.findByInTime(inTime);
    }

    /**
     *{@inheritDoc}
     */
    @Override
    public List<Event> findByInTimeAndStatus(Boolean inTime, StatusType status) {
        return eventRepository.findByInTimeAndStatus(inTime, status);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Event> findByUserEmail(String email) {
        return eventRepository.findByUserEmail(email);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Event> findFavoriteEventsByUser(Long userId) {
        return eventRepository.findFavoriteEventsByUserId(userId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Event> searchPublishedEvents(String communityName, String provinceName, LocalDate eventDate,
            String themeName, String municipalityName, Double maxPrice) {
        return eventRepository.searchPublishedEvents(communityName.toLowerCase(),  provinceName.toLowerCase(),eventDate,
                                                    themeName != null ? themeName.toLowerCase() : null, //Indicamos que si no es null, se pase en minúsculas y si es null, se pase como null
                                                    municipalityName != null ? municipalityName.toLowerCase() : null,
                                                    maxPrice);
    }

   
  

     // MÉTODO PRIVADO DE VALIDACIÓN 
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

   

   

}
