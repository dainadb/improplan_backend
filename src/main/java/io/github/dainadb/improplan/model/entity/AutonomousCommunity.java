package io.github.dainadb.improplan.model.entity;

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
 * Entidad que representa una Comunidad Autónoma.
 */
@Entity
@Table(name = "autonomous_communities")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AutonomousCommunity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Clave primaria de la comunidad autónoma.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_autonomous")
    private Integer id;

    /**
     * Nombre de la comunidad autónoma.
     */
    @Column(nullable = false, length = 150)
    private String name;

    //En vez de establecer una relación bidireccional con Province, se ha preferido estableceruna relación unidireccional desde Province hacia AutonomousCommunity.
    //Así se evita inicializar y gestionar la lista de provincias en la comunidad autónoma, simplificando el modelo de datos.
    //Para acceder a las porvincias de una comunidad autónoma, se accederá a través del repositorio de Province.
    
  
}