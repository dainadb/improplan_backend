package io.github.dainadb.improplan.domain.theme.entity;

import java.io.Serializable;

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
 * Entidad que representa una Temática para los eventos.
 */
@Entity
@Table(name = "themes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Theme implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Clave primaria de la temática.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_theme")
    private Integer id;

    /**
     * Nombre de la temática.
     */
    @Column(nullable = false, unique = true, length = 50)
    private String name;

    /**
     * Descripción de la temática.
     */
    @Column(length = 255)
    private String description;

}