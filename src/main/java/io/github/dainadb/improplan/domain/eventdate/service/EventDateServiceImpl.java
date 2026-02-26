package io.github.dainadb.improplan.domain.eventdate.service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.github.dainadb.improplan.domain.eventdate.dto.EventDateResponseDto;
import io.github.dainadb.improplan.domain.eventdate.entity.EventDate;
import io.github.dainadb.improplan.domain.eventdate.repository.IEventDateRepository;
import jakarta.transaction.Transactional;

/**
 * Implementación del servicio para gestionar las fechas de los eventos (EventDate).
 */
@Service
public class EventDateServiceImpl implements IEventDateService {

    @Autowired
    private IEventDateRepository eventDateRepository;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * {@inheritDoc} 
     */
    @Override
    @Transactional
    public Set<EventDate> findOrCreateDates(Set<LocalDate> dates) {
        if (dates == null || dates.isEmpty()) {
            return new HashSet<>();
        }

        return dates.stream()
                .map(date -> eventDateRepository.findByFullDate(date)
                        .orElseGet(() -> {
                            EventDate newEventDate = new EventDate();
                            newEventDate.setFullDate(date);
                            return eventDateRepository.save(newEventDate);
                        }))
                .collect(Collectors.toSet());
    }

     /**
     * {@inheritDoc}
     */
    @Override
    public List<EventDateResponseDto> getAllDatesByEventId(Long eventId) {
        List<EventDate> dates = eventDateRepository.findAllDatesByEventId(eventId);
        return dates.stream()
                .map(date -> modelMapper.map(date, EventDateResponseDto.class))
                .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<EventDateResponseDto> getUpcomingDatesByEventId(Long eventId) {
        List<EventDate> dates = eventDateRepository.findUpcomingDatesByEventId(eventId, LocalDate.now());
        return dates.stream()
                .map(date -> modelMapper.map(date, EventDateResponseDto.class))
                .toList();
    }
}
