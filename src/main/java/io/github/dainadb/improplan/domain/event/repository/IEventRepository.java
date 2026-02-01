package io.github.dainadb.improplan.domain.event.repository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import io.github.dainadb.improplan.domain.event.entity.Event;
import io.github.dainadb.improplan.domain.event.entity.Event.StatusType;
/** 
 * Repositorio para la entidad Event. 
 * Permite realizar operaciones CRUD y consultas sobre la tabla de eventos.
*/
public interface IEventRepository extends JpaRepository<Event, Long> {


    /**
     * Busca eventos cuyo nombre contiene la cadena dada, ignorando mayúsculas y minúsculas.
     * @param name Cadena a buscar en los nombres de los eventos.
     * @return Lista de eventos que contienen la cadena en su nombre.
     */
    List<Event> findByNameContainingIgnoreCase(String name);

     /**
     * Busca eventos por su estado.
     * @param status Estado del evento.
     * @return Lista de eventos que coinciden con el estado dado.
     */
    List<Event> findByStatus(StatusType status);

    /**
     * Busca eventos según si están vigentes o no.
     * @param inTime Indica si el evento está vigente (true) o no (false).
     * @return Lista de eventos que coinciden con el criterio dado.
     */
    List<Event> findByInTime(Boolean inTime);

    /**
     * Busca eventos según si están vigentes o no y su estado.
     * @param inTime Indica si el evento está vigente (true) o no (false).
     * @param statusType Estado del evento.
     * @return Lista de eventos que coinciden con los criterios dados.
     */
    List<Event> findByInTimeAndStatus(Boolean inTime, StatusType statusType);
   
    /**
     * Busca eventos por su estado de tiempo (inTime) y por una lista de estados posibles.
     * @param inTime false para eventos fuera de tiempo, true para eventos en tiempo.
     * @param statuses Colección de estados por los que filtrar (ej. PUBLISHED, PENDING).
     * @return Lista de eventos que cumplen las condiciones.
     */
    List<Event> findByInTimeAndStatusIn(boolean inTime, Collection<StatusType> statuses);

    /**
     * Cuenta cuántos eventos hay en un estado específico.
     * @param status Estado del evento.
     * @return Número de eventos en el estado dado.
     */
    long countByStatus(StatusType status);

    /**
     * Cuenta cuántos eventos hay según su estado de tiempo (inTime).
     * @param inTime true para eventos vigentes, false para eventos pasados.
     * @return Número de eventos que coinciden con el criterio dado.
     */
    long countByInTime(Boolean inTime);
    /**
     * Busca eventos según si son gratuitos o no.
     * @param isFree Indica si el evento es gratuito (true) o de pago (false).
     * @return Lista de eventos que coinciden con el criterio de gratuidad.
     */
    List<Event> findByIsFree(Boolean isFree);

   
   /**
    * Busca eventos cuyo precio sea igual o inferior al dado
    * @param maxPrice Precio máximo
    * @return Lista de eventos que tienen un precio igual o inferior al especificado.
    */
    List<Event> findByPriceLessThanEqual (Double maxPrice);

    /**
     * Busca eventos por el nombre del municipio.
     * @param name Nombre del municipio.
     * @return Lista de eventos que pertenecen al municipio con el nombre dado.
     */
    List<Event> findByMunicipalityNameIgnoreCase(String name);

    /**
     * Busca eventos por el nombre de la provincia.
     * @param name Nombre de la provincia.
     * @return Lista de eventos que pertenecen a la provincia con el nombre dado.
     */
    List<Event> findByProvinceNameIgnoreCase(String name);


    /**
     * Busca eventos por el nombre de su temática.
     * @param name Nombre de la temática.
     * @return Lista de eventos que pertenecen a la temática con el nombre dado.
     */
    List<Event> findByThemeNameIgnoreCase(String name); 
   
    /**
     * Busca eventos que ocurren en una fecha específica.
     *
     * @param fullDate Fecha a buscar.
     * @return Lista de eventos que tienen lugar en esa fecha.
     */
    // El nexo de unión entre Event y EventDate es la colección 'dates' en la entidad Event.
      List<Event> findByDatesFullDate(LocalDate fullDate);

    /**
     * Busca eventos asociados al correo electrónico de un usuario.
     * @param email Correo electrónico del usuario.
     * @return Lista de eventos asociados al usuario (el usuario que creó el evento).
     */
    List<Event> findByUserEmail (String email);


    /**
     * Busca la lista de eventos publicados que un usuario ha marcado como favoritos.
     * @param userId El ID del usuario.
     * @return Una lista de los eventos favoritos de ese usuario.
     */
    // Se usa una consulta JPQL para unir la entidad Favorite con Event y filtrar por el ID del usuario.
    @Query("SELECT f.event FROM Favorite f WHERE f.user.id = :userId AND f.event.status = 'PUBLISHED'")
    List<Event> findFavoriteEventsByUserId(@Param("userId") Long userId);

  /**
   * Búsqueda avanzada de eventos publicados y vigentes con filtros dinámicos. 
   * @param provinceName  Nombre de la provincia. Filtro obligatorio.
   * @param eventDate Fecha del evento. Filtro obligatorio.
   * @param themeName   Nombre de la temática. Filtro opcional (puede ser null).
   * @param municipalityName  Nombre del municipio. Filtro opcional (puede ser null).
   * @param maxPrice  Precio máximo. Filtro opcional (puede ser null).
   * @return
   */
    @Query("""
            SELECT e FROM Event e
              JOIN e.municipality m
              JOIN m.province p
              JOIN e.dates d
            WHERE e.status = 'PUBLISHED'
              AND e.inTime = true
              AND p.name = :provinceName
              AND d.fullDate = :eventDate
              AND (:themeName IS NULL OR e.theme.name = :themeName)
              AND (:municipalityName IS NULL OR m.name = :municipalityName)
              AND (:maxPrice IS NULL OR e.price <= :maxPrice)
            """) 
        List<Event> searchPublishedEvents(
                @Param("provinceName") String provinceName,
                @Param("eventDate") LocalDate eventDate,
                @Param("themeName") String themeName,
                @Param("municipalityName") String municipalityName,
                @Param("maxPrice") Double maxPrice
        );




//Valorar si se podrá implementar en un futuro 
   
    //OPCIONES PAGINADAS DE LAS BÚSQUEDAS ANTERIORES:
     /**
     * Busca la lista paginada de eventos que un usuario ha marcado como favoritos.
     *
     * @param userId El ID del usuario.
     * @param pageable Un objeto que contiene la información de paginación (número de página, tamaño y ordenación).
     * @return Una 'página' (Page) de eventos favoritos de ese usuario.
     */
    @Query("SELECT f.event FROM Favorite f WHERE f.user.id = :userId")
    Page<Event> findFavoriteEventsByUserId(@Param("userId") Long userId, Pageable pageable);



    //Para poder paginar los resultados de las búsquedas se devolverá un Page<Event> en lugar de List<Event>(una página de eventos).
    //No queremos toda la lista de eventos al completo, solo un subconjunto de esta lista (una cierta cantidad)
    //Además de los elementos de la página, el objeto Page también contiene información sobre el total de elementos, total de páginas, número de página actual, etc.
   
    //Por parámetros se pasa un objeto Pageable que especifica el número de página, tamaño de página y criterios de ordenación.
    //Page<Event> findAll (Pageable pageable); //No sería necesario definir este método, ya que JpaRepository ya lo proporciona.

    //En esta opción además de pasar por parámetro el objeto Pageable, se pasa el estado para filtrar los eventos por su estado.
    Page<Event> findByStatus (Pageable pageable, StatusType status);

    //En esta opción además de pasar por parámetro el objeto Pageable, se pasa el estado para filtrar los eventos por su estado y si están vigentes
    Page<Event> findByInTimeAndStatus(Pageable pageable, Boolean inTime, StatusType status);



    //FILTROS DINÁMICOS CON PAGINACIÓN:
    /**
     * Búsqueda avanzada de eventos publicados con paginación y filtros dinámicos.
     * @param communityName Nombre de la comunidad autónoma. Filtro obligatorio.
     * @param provinceName Nombre de la provincia. Filtro obligatorio.
     * @param eventDate Fecha del evento. Filtro obligatorio.
     * @param themeName Nombre de la temática. Filtro opcional (puede ser null).
     * @param municipalityName Nombre del municipio. Filtro opcional (puede ser null).
     * @param maxPrice Precio máximo. Filtro opcional (puede ser null).
     * @param pageable Objeto Pageable para la paginación de resultados.
     * @return
     */
    //Esta consulta impide que se carguen todos los eventos en memoria para luego filtrarlos.
    // En su lugar, la base de datos realiza el filtrado y solo devuelve los resultados que coinciden con los criterios especificados.
    // Esto mejora significativamente el rendimiento, especialmente cuando se trabaja con grandes conjuntos de datos.

    //En esta consulta se pueden ver filtros fijos, ejemplo: e.status = 'PUBLISHED' (por estado publicado)o 
    // opcionales, ejemplo: (:themeName IS NULL OR e.theme.name = :themeName) --> aquí se indica que si el parámetro :themeName es null, automáticamente la condición será true y por lo tanto no se filtrará por temática (eso es así porque el usuario ha decidido no filtrar por temática)
    //En cambio, por como se quiere diseñar el front, tanto: comunidad, provincia como fecha, serán filtros fijos, ya que el usuario siempre tendrá que indicarlos antes de que se le muestren los eventos publicados.
    @Query("""
        SELECT e FROM Event e
          JOIN e.municipality m
          JOIN m.province p
          JOIN p.community c
          JOIN e.dates d
        WHERE e.status = 'PUBLISHED' 
          AND c.name = :communityName 
          AND p.name = :provinceName
          AND d.fullDate = :eventDate
          AND (:themeName IS NULL OR e.theme.name = :themeName)
          AND (:municipalityName IS NULL OR m.name = :municipalityName)
          AND (:maxPrice IS NULL OR e.price <= :maxPrice)
        """) 
    Page<Event> searchPublishedEvents(
            @Param("communityName") String communityName,
            @Param("provinceName") String provinceName,
            @Param("eventDate") LocalDate eventDate,
            @Param("themeName") String themeName,
            @Param("municipalityName") String municipalityName,
            @Param("maxPrice") Double maxPrice,
            Pageable pageable
    );

}
