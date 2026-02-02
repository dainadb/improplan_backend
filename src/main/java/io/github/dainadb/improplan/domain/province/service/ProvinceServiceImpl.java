package io.github.dainadb.improplan.domain.province.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import io.github.dainadb.improplan.domain.generic.service.GenericCrudDtoServiceImpl;
import io.github.dainadb.improplan.domain.province.dto.ProvinceRequestDto;
import io.github.dainadb.improplan.domain.province.dto.ProvinceResponseDto;
import io.github.dainadb.improplan.domain.province.entity.Province;
import io.github.dainadb.improplan.domain.province.repository.IProvinceRepository;

/**
 * Implementación del servicio para la gestión de Provincias.
 * Hereda la funcionalidad CRUD básica del servicio genérico.
 */
@Service
public class ProvinceServiceImpl 
    extends GenericCrudDtoServiceImpl<Province, ProvinceRequestDto, ProvinceResponseDto, Integer> 
    implements IProvinceService {

    @Autowired
    private IProvinceRepository provinceRepository;

    // IMPLEMENTACIÓN DE MÉTODOS ABSTRACTOS DE LA CLASE GENÉRICA 

    @Override
    protected JpaRepository<Province, Integer> getRepository() {
        return provinceRepository;
    }

    @Override
    protected Class<Province> getEntityClass() {
        return Province.class;
    }

    @Override
    protected Class<ProvinceRequestDto> getRequestDtoClass() {
        return ProvinceRequestDto.class;
    }

    @Override
    protected Class<ProvinceResponseDto> getResponseDtoClass() {
        return ProvinceResponseDto.class;
    }

    //  IMPLEMENTACIÓN DE MÉTODOS ESPECÍFICOS DE LA INTERFAZ

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ProvinceResponseDto> findByAutonomousCommunityName(String communityName) {

        List<Province> provinces = provinceRepository.findByAutonomousCommunityNameIgnoreCase(communityName.trim());
        return provinces.stream()
                .map(this::convertToResponseDto) // Reutiliza el método heredado
                .toList();
    }

    @Override
    public ProvinceResponseDto findByName(String name) {
        Province province = provinceRepository.findByNameIgnoreCase(name)
                .orElseThrow(() -> new RuntimeException("Provincia no encontrada con el nombre: " + name));
        return convertToResponseDto(province);
    }
}