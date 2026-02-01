package io.github.dainadb.improplan.domain.favorite.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.dainadb.improplan.common.response.ApiResponse;
import io.github.dainadb.improplan.domain.favorite.dto.FavoriteRequestDto;
import io.github.dainadb.improplan.domain.favorite.dto.FavoriteResponseDto;
import io.github.dainadb.improplan.domain.favorite.service.IFavoriteService;
import io.github.dainadb.improplan.domain.generic.controller.GenericRestController;


/**
 * Controlador REST para gestionar los eventos favoritos de los usuarios.
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/favorites")
public class FavoriteRestController extends GenericRestController {

    @Autowired
    private IFavoriteService favoriteService;

    /**
     * Añade un evento a la lista de favoritos del usuario autenticado.
     * @param favoriteRequestDto Datos del evento a añadir a favoritos.
     * @return Respuesta con los detalles del evento añadido.
     */
    @PostMapping("/add")
    public ResponseEntity<ApiResponse<FavoriteResponseDto>> addFavorite(@RequestBody FavoriteRequestDto favoriteRequestDto) {
        String userEmail = getEmail(); // Obtiene el email del usuario autenticado
        FavoriteResponseDto newFavorite = favoriteService.addFavorite(favoriteRequestDto, userEmail);
        return created(newFavorite, "Evento añadido a favoritos con éxito.");
    }

    /**
     * Elimina un evento de la lista de favoritos del usuario autenticado.
     * @param eventId ID del evento a eliminar de favoritos.
     * @return Respuesta indicando el éxito de la operación.
     */
    @DeleteMapping("/delete/{eventId}")
    public ResponseEntity<ApiResponse<Void>> removeFavorite(@PathVariable Long eventId) {
        String userEmail = getEmail(); 
        favoriteService.removeFavorite(eventId, userEmail);
        return success(null, "Evento eliminado de favoritos con éxito.");
    }

   /**
     * Obtiene la lista de eventos favoritos del usuario autenticado.
     * @return Respuesta con la lista de eventos favoritos.
     */
    @GetMapping("/my-favorites")
    public ResponseEntity<ApiResponse<List<FavoriteResponseDto>>> getMyFavorites() {
        String userEmail = getEmail();
        List<FavoriteResponseDto> favorites = favoriteService.getFavoritesByUser(userEmail);
        return success(favorites, "Lista de favoritos obtenida con éxito.");
    }

   /**
     * Cuenta cuantas veces se ha marcado un evento específico como favorito.
     * @param eventId ID del evento.
     * @return Respuesta con el recuento de favoritos para el evento.
     */
    @GetMapping("/count/{eventId}")
    public ResponseEntity<ApiResponse<Long>> countFavoritesByEvent(@PathVariable Long eventId) {
        Long count = favoriteService.countFavoritesByEventId(eventId);
        return success(count, "Recuento de favoritos para el evento " + eventId + ".");
    }

}