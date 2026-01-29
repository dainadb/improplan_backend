package io.github.dainadb.improplan.domain.autonomouscommunity.service;


import io.github.dainadb.improplan.domain.autonomouscommunity.dto.AutCommunityRequestDto;
import io.github.dainadb.improplan.domain.autonomouscommunity.dto.AutCommunityResponseDto;
import io.github.dainadb.improplan.domain.autonomouscommunity.entity.AutonomousCommunity;
import io.github.dainadb.improplan.domain.generic.service.IGenericCrudDtoService;


public interface IAutonomousCommunityService extends IGenericCrudDtoService<AutonomousCommunity, AutCommunityRequestDto, AutCommunityResponseDto, Integer> {

    /**
     * Busca una comunidad autónoma por su nombre.
     * @param name El nombre de la comunidad autónoma a buscar.
     * @return DTO de la comunidad autónoma encontrada.
     * @throws RuntimeException si la comunidad autónoma no existe.
     */
    AutCommunityResponseDto findByName(String name);
}
