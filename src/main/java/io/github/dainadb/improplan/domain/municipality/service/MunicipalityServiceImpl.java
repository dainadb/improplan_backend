package io.github.dainadb.improplan.domain.municipality.service;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import io.github.dainadb.improplan.domain.generic.service.GenericCrudDtoServiceImpl;
import io.github.dainadb.improplan.domain.municipality.dto.MunicipalityRequestDto;
import io.github.dainadb.improplan.domain.municipality.dto.MunicipalityResponseDto;
import io.github.dainadb.improplan.domain.municipality.entity.Municipality;
import io.github.dainadb.improplan.domain.municipality.repository.IMunicipalityRepository;

/**
 * Implementación del servicio para la gestión de Municipios.
 * Hereda la funcionalidad CRUD básica del servicio genérico.
 */
@Service
public class MunicipalityServiceImpl extends GenericCrudDtoServiceImpl<Municipality, MunicipalityRequestDto, MunicipalityResponseDto, Integer> 
                                    implements IMunicipalityService {

    @Autowired
    private IMunicipalityRepository municipalityRepository;

    // --- IMPLEMENTACIÓN DE MÉTODOS ABSTRACTOS DE LA CLASE GENÉRICA ---

    @Override
    protected JpaRepository<Municipality, Integer> getRepository() {
        return municipalityRepository;
    }

    @Override
    protected Class<Municipality> getEntityClass() {
        return Municipality.class;
    }

    @Override
    protected Class<MunicipalityRequestDto> getRequestDtoClass() {
        return MunicipalityRequestDto.class;
    }

    @Override
    protected Class<MunicipalityResponseDto> getResponseDtoClass() {
        return MunicipalityResponseDto.class;
    }

    //  IMPLEMENTACIÓN DE MÉTODOS ESPECÍFICOS DE LA INTERFAZ 

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MunicipalityResponseDto> findByProvinceName(String provinceName) {
        List<Municipality> municipalities = municipalityRepository.findByProvinceName(provinceName);
        return municipalities.stream()
                .map(this::convertToResponseDto)
                .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MunicipalityResponseDto> findByNameContaining(String name) {
        List<Municipality> municipalities = municipalityRepository.findByNameContainingIgnoreCase(name);
        return municipalities.stream()
                .map(this::convertToResponseDto)
                .toList();
    }

    //Nota: no se implementan los métodos validateCreate o convertToEntity (por ejemplo) porque que estas entidades se crearán en la BBDD de forma previa y no se crearán nuevas desde la aplicación.
    //Posible implementación futura
}