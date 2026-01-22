package io.github.dainadb.improplan.model.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidad que representa un evento marcado como favorito por un usuario.
 * Implementa una restricción única para que un usuario solo pueda marcar
 * como favorito un evento una vez.
 */
@Entity
@Table(name = "favorites", uniqueConstraints = {
@UniqueConstraint(columnNames = {"user_id", "event_id"}) 
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Favorite implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Clave primaria única para la entrada de favorito.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_favorite")
    private Long id;

    /**
     * Fecha y hora en que el evento fue marcado como favorito.
     */
    @Column(name = "favorite_date", updatable = false) // updatable = false para que no se modifique después de crearse.
    private LocalDateTime favoriteDate;

    /**
     * Relación unidireccional ManyToOne con el usuario que marcó el evento como favorito.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * Relación unidireccional ManyToOne con el evento que fue marcado como favorito.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;
}
