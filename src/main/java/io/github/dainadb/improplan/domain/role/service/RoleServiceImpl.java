package io.github.dainadb.improplan.domain.role.service;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import io.github.dainadb.improplan.domain.generic.service.GenericCrudDtoServiceImpl;
import io.github.dainadb.improplan.domain.role.dto.RoleRequestDto;
import io.github.dainadb.improplan.domain.role.dto.RoleResponseDto;
import io.github.dainadb.improplan.domain.role.entity.Role;
import io.github.dainadb.improplan.domain.role.repository.IRoleRepository;
import io.github.dainadb.improplan.exception.BadRequestException;


/**
 * Implementación del servicio para la gestión de Roles.
 * Hereda toda la funcionalidad CRUD del servicio genérico.
 */
@Service
public class RoleServiceImpl 
    extends GenericCrudDtoServiceImpl<Role, RoleRequestDto, RoleResponseDto, Integer> 
    implements IRoleService {

    @Autowired
    private IRoleRepository roleRepository;

    // --- IMPLEMENTACIÓN DE MÉTODOS ABSTRACTOS DE LA CLASE GENÉRICA ---

    @Override
    protected JpaRepository<Role, Integer> getRepository() {
        return roleRepository;
    }

    @Override
    protected Class<Role> getEntityClass() {
        return Role.class;
    }

    @Override
    protected Class<RoleRequestDto> getRequestDtoClass() {
        return RoleRequestDto.class;
    }

    @Override
    protected Class<RoleResponseDto> getResponseDtoClass() {
        return RoleResponseDto.class;
    }
    
    //  IMPLEMENTACIÓN DE MÉTODOS ESPECÍFICOS DE LA INTERFAZ 

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<RoleResponseDto> findByName(String name) {
       
        if (name == null || name.trim().isEmpty()) {
            throw new BadRequestException("El nombre del rol no puede estar vacío.");
        }

        Role.RoleType roleType;

        try {
            //Se intenta convertir el nombre del rol a RoleType (enum)
            roleType = Role.RoleType.valueOf(name.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            // Si la conversión falla porque el rol no existe. Devolvemos un Optional vacío.
            // No lanzamos una excepción porque es un caso de "no encontrado", no un error del sistema.
            return Optional.empty();
        }

        Optional<Role> roleOpt = roleRepository.findByName(roleType);
    

        // Reutiliza el método convertToResponseDto heredado de la clase genérica.
        return roleOpt.map(this::convertToResponseDto);
    }
}