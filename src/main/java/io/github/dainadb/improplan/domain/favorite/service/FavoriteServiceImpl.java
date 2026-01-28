package io.github.dainadb.improplan.domain.favorite.service;

import java.time.LocalDateTime;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.github.dainadb.improplan.domain.event.entity.Event;
import io.github.dainadb.improplan.domain.event.repository.IEventRepository;
import io.github.dainadb.improplan.domain.favorite.dto.FavoriteRequestDto;
import io.github.dainadb.improplan.domain.favorite.dto.FavoriteResponseDto;
import io.github.dainadb.improplan.domain.favorite.entity.Favorite;
import io.github.dainadb.improplan.domain.favorite.repository.IFavoriteRepository;
import io.github.dainadb.improplan.domain.user.entity.User;
import io.github.dainadb.improplan.domain.user.repository.IUserRepository;
import io.github.dainadb.improplan.exception.ConflictException;
import io.github.dainadb.improplan.exception.NotFoundException;
import jakarta.transaction.Transactional;

/**
 * Implementación del servicio para gestionar los eventos favoritos de los usuarios.
 */
@Service
public class FavoriteServiceImpl implements IFavoriteService {

    @Autowired
    private IFavoriteRepository favoriteRepository;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IEventRepository eventRepository;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public FavoriteResponseDto addFavorite(FavoriteRequestDto favoriteRequestDto, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado con email: " + userEmail));

        Event event = eventRepository.findById(favoriteRequestDto.getEventId())
                .orElseThrow(() -> new NotFoundException("Evento no encontrado con ID: " + favoriteRequestDto.getEventId()));
        
        // Comprobar si ya existe una entrada de favorito para evitar conflictos en la BBDD
        favoriteRepository.findByUserIdAndEventId(user.getId(), event.getId()).ifPresent(f -> {
            throw new ConflictException("Este evento ya está en tus favoritos.");
        });

        //No se usa ModelMapper porque todas las propiedades de Favorite requieren lógica de negocio específica
        Favorite favorite = new Favorite();
        favorite.setUser(user);
        favorite.setEvent(event);
        favorite.setFavoriteDate(LocalDateTime.now());
        
        Favorite savedFavorite = favoriteRepository.save(favorite);

        // Aquí sí usamos ModelMapper para mapear y crear el DTO de respuesta
        return modelMapper.map(savedFavorite, FavoriteResponseDto.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void removeFavorite(Long eventId, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado con email: " + userEmail));
        
        // Validar si existe el favorito antes de intentar borrarlo
        Favorite favorite = favoriteRepository.findByUserIdAndEventId(user.getId(), eventId)
                .orElseThrow(() -> new NotFoundException("El evento con ID " + eventId + " no está en tu lista de favoritos."));
        
        favoriteRepository.deleteById(favorite.getId());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<FavoriteResponseDto> getFavoritesByUser(String userEmail) {
        if (!userRepository.existsByEmail(userEmail)) {
            throw new NotFoundException("Usuario no encontrado con email: " + userEmail);
        }

        List<Favorite> favorites = favoriteRepository.findByUserEmail(userEmail);

        return favorites.stream()
                .map(favorite -> modelMapper.map(favorite, FavoriteResponseDto.class))
                .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long countFavoritesByEventId(Long eventId) {

        return favoriteRepository.countByEventId(eventId);
    }
}