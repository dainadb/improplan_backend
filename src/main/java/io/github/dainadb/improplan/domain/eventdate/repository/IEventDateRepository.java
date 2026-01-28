package io.github.dainadb.improplan.domain.eventdate.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import io.github.dainadb.improplan.domain.eventdate.entity.EventDate;

/** 
 * Repositorio para la entidad EventDate. 
 * Permite realizar operaciones CRUD y consultas sobre la tabla de fechas de eventos.
*/
public interface IEventDateRepository extends JpaRepository<EventDate, Long> {


 /**
     * Busca una EventDate por su fecha completa.
     * @param fullDate La fecha a buscar.
     * @return Un Optional que contiene la EventDate si se encuentra.
     */
    Optional<EventDate> findByFullDate(LocalDate fullDate);



    //Como la entidad EventDate no tiene relación directa con Event, no se pueden usar métodos derivados para buscar por atributos de Event.
    //Se tienen que usar consulas JPQL.
    

    /**
     * Busca TODAS las fechas asociadas a un evento específico, ordenadas cronológicamente.
     *
     * @param eventId El ID del evento por el cual filtrar.
     * @return Una lista de todas las EventDate asociadas al evento ordenadas cronológicamente.
     */
    @Query("SELECT ed FROM Event e JOIN e.dates ed WHERE e.id = :eventId ORDER BY ed.fullDate ASC")
    List<EventDate> findAllDatesByEventId(@Param("eventId") Long eventId);

    /**
     * Busca las fechas de un evento específico que sean iguales o posteriores a la fecha actual.
     * @param eventId El ID del evento por el cual filtrar.
     * @param currentDate La fecha a partir de la cual buscar (la fecha actual).
     * @return Una lista de EventDate que cumplen con ambos criterios, ordenadas por fecha.
     */
    @Query("SELECT ed FROM Event e JOIN e.dates ed WHERE e.id = :eventId AND ed.fullDate >= :currentDate ORDER BY ed.fullDate ASC")
    List<EventDate> findUpcomingDatesByEventId(@Param("eventId") Long eventId, @Param("currentDate") LocalDate currentDate);


}
