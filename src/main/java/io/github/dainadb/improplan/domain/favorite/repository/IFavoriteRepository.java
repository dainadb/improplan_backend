package io.github.dainadb.improplan.domain.favorite.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.dainadb.improplan.domain.favorite.entity.Favorite;


/** 
 * Repositorio para la entidad Favorite. 
 * Permite realizar operaciones CRUD y consultas sobre la tabla de favoritos.
*/
public interface IFavoriteRepository extends JpaRepository<Favorite, Long> {

   
    /**
     * Devuelve los favoritos del usuario por su email.
     * @param email Email del usuario.
     *  @return Lista de favoritos que ha seleccionado ese usuario
     */
    List<Favorite> findByUserEmail(String email);

    /**     
     * Busca un favorito por el ID del usuario y el ID del evento.
     * @param userId ID del usuario.
     * @param eventId ID del evento. 
     * @return un {@link Optional} que contiene el favorito si existe, o vacío en caso contrario
     */
     Optional<Favorite> findByUserIdAndEventId(Long userId, Long eventId);


    /**
     * Cuenta cuántas veces un evento ha sido marcado como favorito.
     * @param eventId El ID del evento.
     * @return El número de favoritos para ese evento.
     */
    long countByEventId(Long eventId);

    /**
        * Elimina todos los favoritos asociados a un evento específico.
        * Utilizado cuando un evento es eliminado para limpiar los favoritos relacionados.
        * 
        * @param eventId El ID del evento cuyos favoritos se van a eliminar.
        */
    void deleteByEventId(Long eventId);

}
