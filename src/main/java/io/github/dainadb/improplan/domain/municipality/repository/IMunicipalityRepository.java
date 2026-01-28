package io.github.dainadb.improplan.domain.municipality.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.dainadb.improplan.domain.municipality.entity.Municipality;

/**
 * Repositorio para la entidad Municipality.
 * Permite realizar operaciones CRUD y consultas sobre la tabla de municipios.
 */
public interface IMunicipalityRepository extends JpaRepository<Municipality, Integer> {

    /**
     * Busca municipios por el nombre de su provincia.
     * @param name Nombre de la provincia.
     * @return Lista de municipios que coinciden con el nombre de la provincia.
     */
    //No se indica containgIgnoreCase porque la búsqueda que se hará en el front será mediante un desplegable con los nombres exactos
    List<Municipality> findByProvinceName(String name);


    /**
     * Busca municipios cuyo nombre contiene la cadena dada, ignorando mayúsculas y minúsculas.
     * @param name Cadena a buscar en los nombres de los municipios.
     * @return Lista de municipios que contienen la cadena en su nombre.
     */
    //Se usa para la búsqueda tipo autocomplete en el front.
    List<Municipality> findByNameContainingIgnoreCase(String name);

    /**
     * Busca un municipio por su nombre.
     * @param name Nombre del municipio.
     * @return Municipio que coincide con el nombre.
     */

    Optional<Municipality> findByName(String name);
}

  
