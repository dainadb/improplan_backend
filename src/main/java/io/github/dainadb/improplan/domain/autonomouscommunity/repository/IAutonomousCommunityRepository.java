package io.github.dainadb.improplan.domain.autonomouscommunity.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.dainadb.improplan.domain.autonomouscommunity.entity.AutonomousCommunity;

import java.util.Optional;


/** 
 * Repositorio para la entidad AutonomousCommunity. 
 * Permite realizar operaciones CRUD y consultas sobre la tabla de comunidades autónomas.
*/
public interface IAutonomousCommunityRepository extends JpaRepository<AutonomousCommunity, Integer> {

    /**
     * Busca una comunidad autónoma por su nombre.
     * @param name Nombre de la comunidad autónoma.
     * @return Comunidad autónoma que coincide con el nombre.
     */
    
    Optional<AutonomousCommunity> findByNameContainingIgnoreCase(String name);
    
}
