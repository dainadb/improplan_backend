package io.github.dainadb.improplan.domain.municipality.service;

import java.util.List;

import io.github.dainadb.improplan.domain.generic.service.IGenericCrudDtoService;
import io.github.dainadb.improplan.domain.municipality.dto.MunicipalityRequestDto;
import io.github.dainadb.improplan.domain.municipality.dto.MunicipalityResponseDto;
import io.github.dainadb.improplan.domain.municipality.entity.Municipality;

/**
 * Interfaz para el servicio de gestión de Municipios.
 * Extiende la interfaz CRUD genérica y añade métodos de búsqueda específicos.
 */
public interface IMunicipalityService extends IGenericCrudDtoService<Municipality, MunicipalityRequestDto, MunicipalityResponseDto, Integer> {

    /**
     * Busca municipios por el nombre de la provincia a la que pertenecen.
     * La búsqueda es por nombre exacto.
     *
     * @param provinceName El nombre exacto de la provincia.
     * @return Una lista de DTOs de los municipios encontrados.
     */
    List<MunicipalityResponseDto> findByProvinceName(String provinceName);

    /**
     * Busca municipios cuyo nombre contenga una cadena de texto, ignorando mayúsculas y minúsculas.
     * Para funcionalidades de autocompletado.
     *
     * @param name El texto a buscar dentro de los nombres de los municipios.
     * @return Una lista de DTOs de los municipios encontrados.
     */
    List<MunicipalityResponseDto> findByNameContaining(String name);
}