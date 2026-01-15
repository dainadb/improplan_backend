package io.github.dainadb.improplan.model.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import io.github.dainadb.improplan.model.entity.Favorite;
import jakarta.transaction.Transactional;


/** 
 * Repositorio para la entidad Favorite. 
 * Permite realizar operaciones CRUD y consultas sobre la tabla de favoritos.
*/
public interface IFavoriteRepository extends JpaRepository<Favorite, Long> {

    /**     
     * Busca un favorito por el ID del usuario y el ID del evento.
     * @param userId ID del usuario.
     * @param eventId ID del evento. 
     * @return un {@link Optional} que contiene el favorito si existe, o vacío en caso contrario
     */
     Optional<Favorite> findByUserIdAndEventId(Long userId, Long eventId);

    /**
     * Elimina un favorito por el ID del usuario y el ID del evento.
     * @param userId ID del usuario.   
     * @param eventId ID del evento.
     */
    
    @Transactional
    @Modifying
    @Query("DELETE FROM Favorite f WHERE f.user.id = :userId AND f.event.id = :eventId")
    void deleteByUserIdAndEventId(@Param("userId") Long userId, @Param("eventId") Long eventId);
    
    /**
     * Cuenta cuántas veces un evento ha sido marcado como favorito.
     * @param eventId El ID del evento.
     * @return El número de favoritos para ese evento.
     */
    long countByEventId(Long eventId);

}
