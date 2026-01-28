package io.github.dainadb.improplan.domain.event.service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import io.github.dainadb.improplan.domain.event.entity.Event;
import io.github.dainadb.improplan.domain.event.repository.IEventRepository;
import io.github.dainadb.improplan.domain.eventdate.entity.EventDate;
import jakarta.transaction.Transactional;

/**
 * Servicio para gestionar tareas programadas relacionadas con los eventos.
 */
@Service
public class EventSchedulerService {


    @Autowired
    private IEventRepository eventRepository;

    /**
     * Tarea programada que se ejecuta cada 24 horas para actualizar el estado 'inTime' de los eventos.
     * Un evento se considera caducado si su última fecha de celebración es anterior al día actual.
     * La expresión cron "0 0 1 * * ?" significa: "a la 1:00:00 AM cada día del mes cualquier día de la semana".
     */
    @Scheduled(cron = "0 0 1 * * ?") // Ejecuta la tarea todos los días a la 1 AM
    @Transactional 
    public void updateExpiredEventsStatus() {
       
        //Obtenemos solo los eventos que están marcados como 'en tiempo' para optimizar.
        List<Event> activeEvents = eventRepository.findByInTime(true);
        
       
        LocalDate today = LocalDate.now();
        

        for (Event event : activeEvents) { //De cada evento activo
            
            //Se obtiene la última fecha (la más futura) del conjunto de fechas del evento
            Optional<LocalDate> lastDateOpt = event.getDates().stream()
                .map(EventDate::getFullDate) 
                .max(Comparator.naturalOrder()); //con .max se obtiene el valor máximo. Con Comparator.naturalOrder() se indica el criterio de comparación (orden natural de LocalDate).

            
            if (lastDateOpt.isPresent()) {
                // Si hay fechas, se comprueba si la última fecha es anterior a hoy
                if (lastDateOpt.get().isBefore(today)) {
                    event.setInTime(false);
                    eventRepository.save(event);
                }
            } else {
                // Si no hay fechas, marcamos el evento como no vigente
                event.setInTime(false);
                eventRepository.save(event);
            }

        }
    }   
}