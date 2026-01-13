package io.github.dainadb.improplan.model.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * Entidad que representa un Municipio.
 */
@Entity
@Table(name = "municipalities")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Municipality implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Clave primaria del municipio.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_municipality")
    private Integer id;

    /**
     * Nombre del municipio.
     */
    @Column(nullable = false, length = 150)
    private String name;

    /**
     * Latitud del municipio.
     */
    private Double latitude;

    /**
     * Longitud del municipio.
     */
    private Double longitude;

    /**
     * La provincia a la que pertenece este municipio.
     * Relación unidireccional ManyToOne con Province.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_province", referencedColumnName = "id_province")
    private Province province;

    
}