package io.github.dainadb.improplan.domain.province.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.dainadb.improplan.domain.province.entity.Province;

/** 
 * Repositorio para la entidad Province. 
 * Permite realizar operaciones CRUD y consultas sobre la tabla de provincias.
*/
public interface IProvinceRepository extends JpaRepository<Province, Integer> {

    /**
     * Busca provincias por el nombre de su comunidad autónoma.
     * @param name Nombre de la comunidad autónoma.
     * @return Lista de provincias que pertenecen a la comunidad autónoma con el nombre dado.
     */
    // la búsqueda que se hará en el front será mediante un desplegable con los nombres exactos
    List<Province> findByAutonomousCommunityNameIgnoreCase(String name);

    /**
     * Busca una provincia por su nombre.
     * @param name Nombre de la provincia.
     * @return Provincia que coincide con el nombre.
     */
    Optional<Province> findByNameIgnoreCase(String name);


}
