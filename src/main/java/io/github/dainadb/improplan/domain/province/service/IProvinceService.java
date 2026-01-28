package io.github.dainadb.improplan.domain.province.service;

import java.util.List;
import java.util.Optional;

import io.github.dainadb.improplan.domain.generic.service.IGenericCrudDtoService;
import io.github.dainadb.improplan.domain.province.dto.ProvinceRequestDto;
import io.github.dainadb.improplan.domain.province.dto.ProvinceResponseDto;
import io.github.dainadb.improplan.domain.province.entity.Province;

/**
 * Interfaz para el servicio de gestión de Provincias.
 * Extiende la interfaz CRUD genérica y añade métodos de búsqueda específicos.
 */
public interface IProvinceService extends IGenericCrudDtoService<Province, ProvinceRequestDto, ProvinceResponseDto, Integer> {

    /**
     * Busca provincias por el nombre de la comunidad autónoma a la que pertenecen.
     * La búsqueda es por nombre exacto.
     *
     * @param communityName El nombre exacto de la comunidad autónoma.
     * @return Una lista de DTOs de las provincias encontradas.
     */
    List<ProvinceResponseDto> findByAutonomousCommunityName(String communityName);

    /**
     * Busca una provincia por su nombre.
     * @param name Nombre de la provincia.
     * @return Provincia que coincide con el nombre.
     */
    Optional<ProvinceResponseDto> findByName(String name);
}