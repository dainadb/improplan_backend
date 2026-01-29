package io.github.dainadb.improplan.domain.theme.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.dainadb.improplan.domain.theme.entity.Theme;

/** 
 * Repositorio para la entidad Theme. 
 * Permite realizar operaciones CRUD y consultas sobre la tabla de temas.
*/
public interface IThemeRepository extends JpaRepository<Theme, Integer> {

    /**
     * Busca un tema por su nombre.
     * @param name Nombre del tema.
     * @return un {@link Optional} que contiene la categoría si existe, o vacío en caso contrario
     */
    Optional<Theme> findByNameIgnoreCase(String name);
}
