package io.github.dainadb.improplan.domain.favorite.service;

import java.util.List;

import io.github.dainadb.improplan.domain.favorite.dto.FavoriteRequestDto;
import io.github.dainadb.improplan.domain.favorite.dto.FavoriteResponseDto;

/**
 * Interfaz para el servicio de gestión de favoritos.
 * Define las operaciones que un usuario puede realizar con sus eventos favoritos.
 */
public interface IFavoriteService {

    /**
     * Añade un evento a la lista de favoritos de un usuario.
     *
     * @param favoriteRequestDto DTO que contiene el ID del evento a añadir.
     * @param userEmail El email del usuario autenticado que realiza la operación.
     * @return un DTO con la información del favorito recién creado.
     */
    FavoriteResponseDto addFavorite(FavoriteRequestDto favoriteRequestDto, String userEmail);

    /**
     * Elimina un evento de la lista de favoritos de un usuario.
     *
     * @param eventId El ID del evento a eliminar de los favoritos.
     * @param userEmail El email del usuario autenticado que realiza la operación.
     */
    void removeFavorite(Long eventId, String userEmail);

    /**
     * Obtiene la lista completa de eventos favoritos para un usuario.
     *
     * @param userEmail El email del usuario autenticado.
     * @return Una lista de DTOs, cada uno representando un evento favorito.
     */
    List<FavoriteResponseDto> getFavoritesByUser(String userEmail);

    /**
     * Cuenta cuántas veces un evento ha sido marcado como favorito.
     *
     * @param eventId El ID del evento.
     * @return El número de favoritos para ese evento.
     */
    Long countFavoritesByEventId(Long eventId);
}