package io.github.dainadb.improplan.domain.theme.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import io.github.dainadb.improplan.domain.generic.service.GenericCrudDtoServiceImpl;
import io.github.dainadb.improplan.domain.theme.dto.ThemeRequestDto;
import io.github.dainadb.improplan.domain.theme.dto.ThemeResponseDto;
import io.github.dainadb.improplan.domain.theme.entity.Theme;
import io.github.dainadb.improplan.domain.theme.repository.IThemeRepository;
import io.github.dainadb.improplan.exception.NotFoundException;

/**
 * Implementación del servicio para la gestión de Temáticas (Themes).
 * Hereda toda la funcionalidad CRUD del servicio genérico.
 */
@Service
public class ThemeServiceImpl 
    extends GenericCrudDtoServiceImpl<Theme, ThemeRequestDto, ThemeResponseDto, Integer> 
    implements IThemeService {

    @Autowired
    private IThemeRepository themeRepository;

    //  IMPLEMENTACIÓN DE MÉTODOS ABSTRACTOS DE LA CLASE GENÉRICA 

    @Override
    protected JpaRepository<Theme, Integer> getRepository() {
        return themeRepository;
    }

    @Override
    protected Class<Theme> getEntityClass() {
        return Theme.class;
    }

    @Override
    protected Class<ThemeRequestDto> getRequestDtoClass() {
        return ThemeRequestDto.class;
    }

    @Override
    protected Class<ThemeResponseDto> getResponseDtoClass() {
        return ThemeResponseDto.class;
    }

    // IMPLEMENTACIÓN DE MÉTODOS ESPECÍFICOS DE LA INTERFAZ 

    /**
     * {@inheritDoc}
     */
    @Override
    public ThemeResponseDto findByName(String name) {
        Theme theme = themeRepository.findByNameIgnoreCase(name).orElseThrow(() -> new NotFoundException("No se encontró ninguna temática con el nombre: " + name));
        
        // Reutiliza el método heredado convertToResponseDto para el mapeo
        return convertToResponseDto(theme);
    }

}