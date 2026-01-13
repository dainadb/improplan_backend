package io.github.dainadb.improplan.model.entity;

import java.io.Serializable;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * Entidad que representa una fecha específica en la que puede ocurrir un evento.
 */
@Entity
@Table(name = "event_dates")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventDate implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Identificador único de la fecha.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_date")
    private Long id;

    /**
     * La fecha completa del evento.
     * Se utiliza LocalDate para representar solo la fecha (año, mes, día).
     */
    @Column(name = "full_date", nullable = false)
    private LocalDate fullDate;

}