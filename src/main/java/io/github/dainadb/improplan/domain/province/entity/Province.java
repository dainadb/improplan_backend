package io.github.dainadb.improplan.domain.province.entity;

import java.io.Serializable;

import io.github.dainadb.improplan.domain.autonomouscommunity.entity.AutonomousCommunity;
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
 * Entidad que representa una Provincia.
 */
@Entity
@Table(name = "provinces")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Province implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Clave primaria de la provincia.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_province")
    private Integer id;

    /**
     * Nombre de la provincia.
     */
    @Column(nullable = false, length = 150)
    private String name;

    /**
     * Latitud de la provincia.
     */
    private Double latitude;

    /**
     * Longitud de la provincia.
     */
    private Double longitude;

    /**
     * La comunidad autónoma a la que pertenece esta provincia.
     * Relación unidireccional ManyToOne con AutonomousCommunity.
     */
    @ManyToOne(fetch = FetchType.LAZY) //Se usa el fetch Lazy para evitar cargar la comunidad autónoma al cargar la provincia, solo se cargará cuando se acceda al atributo. 
    @JoinColumn(name = "id_autonomous_community", referencedColumnName = "id_autonomous")
    private AutonomousCommunity autonomousCommunity;

    //No se establece una relación bidireccional con Municipality para simplificar el modelo de datos.(lo mismo que se ha comentado en AutonomousCommunity)
    
}
