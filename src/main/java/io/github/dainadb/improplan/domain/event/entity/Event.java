package io.github.dainadb.improplan.domain.event.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.validator.constraints.URL;

import io.github.dainadb.improplan.domain.eventdate.entity.EventDate;
import io.github.dainadb.improplan.domain.municipality.entity.Municipality;
import io.github.dainadb.improplan.domain.theme.entity.Theme;
import io.github.dainadb.improplan.domain.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * Entidad que representa un evento.
 */
@Entity
@Table(name = "app_events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_event")
    private Long id;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(nullable = false, length = 255)
    private String summary;

    @Lob // Large Object, para textos largos
    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "place_name", length = 200)
    private String placeName;

    @Column(length = 255)
    private String address;

    @Builder.Default
    @Column(precision = 10, scale = 8)
    private Double latitude = 0.0; //Se establece un valor por defecto porque cuando el usuario (role user) crea el evento, no introduce latitud ni longitud.

    @Builder.Default
    @Column(precision = 11, scale = 8)
    private Double longitude = 0.0; //Se establece un valor por defecto porque cuando el usuario (role user) crea el evento, no introduce latitud ni longitud.

    @Column(length = 255)
    private String image;

    @Column(name = "info_url", length = 255)
    @URL(message = "La URL de información no es válida")
    private String infoUrl;

    @Column(name = "is_free")
    private Boolean isFree;

    @Column(precision = 10, scale = 2)
    private Double price;

    @Builder.Default
    @Column(name = "in_time", nullable = false)
    private Boolean inTime = true; //Indica si el evento está vigente o no.
    // Por defecto es true porque cuando se crea un evento, las fechas son futuras.
    //Tiene un valor por defecto que lo modificará un método programado (scheduler).
    
    /** 
     * Estado del evento, por defecto PENDING.  
     * */ 
    @Builder.Default //Para que no ignore el valor por defecto al usar el builder de Lombok
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusType status = StatusType.PENDING;
    
    //Enumeración interna para los tipos de estado del evento.
    // Al estar dentro de la clase Event, se accede como Event.StatusType.
    public enum StatusType {
        PUBLISHED,
        PENDING,
        DISCARDED
    }


    /**
     * Municipio donde se celebra el evento.
     * Relación unidireccional ManyToOne con Municipality.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_municipality", nullable = false)
    private Municipality municipality;

    /**
     * Usuario que ha creado el evento.
     * Un evento es creado por un único usuario.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user", nullable = false)
    private User user; 

    /**
     * Temática del evento.
     * Un evento pertenece a una única temática.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_theme", nullable = false)
    private Theme theme;
    

     /**
     * Conjunto de fechas en las que se celebra el evento.
     * Define una relación "Muchos a Muchos" con EventDate unidireccional.
     * Se crea una tabla intermedia 'events_dates' para gestionar la relación.
     */
    @Builder.Default //Para que no ignore el valor por defecto al usar el builder de Lombok
    @ManyToMany(fetch = FetchType.LAZY) 
    @JoinTable(
        name = "events_dates",
        joinColumns = @JoinColumn(name = "id_event"),
        inverseJoinColumns = @JoinColumn(name = "id_date")
    )
    private Set<EventDate> dates = new HashSet<>(); //Usamos Set para evitar fechas duplicadas y se inicializa para evitar NullPointerException.
}