package io.github.dainadb.improplan.domain.theme.service;


import io.github.dainadb.improplan.domain.generic.service.IGenericCrudDtoService;
import io.github.dainadb.improplan.domain.theme.dto.ThemeRequestDto;
import io.github.dainadb.improplan.domain.theme.dto.ThemeResponseDto;
import io.github.dainadb.improplan.domain.theme.entity.Theme;

/**
 * Interfaz para el servicio de gestión de Temáticas.
 * Extiende la interfaz CRUD genérica y añade métodos de búsqueda específicos.
 */
public interface IThemeService extends IGenericCrudDtoService<Theme, ThemeRequestDto, ThemeResponseDto, Integer> {

    /**
     * Busca una temática por su nombre.
     *
     * @param name El nombre exacto de la temática a buscar.
     * @return un DTO de la temática encontrada.
     * 
     */
    ThemeResponseDto findByName(String name);
}
