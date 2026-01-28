package io.github.dainadb.improplan.domain.role.service;

import java.util.Optional;

import io.github.dainadb.improplan.domain.generic.service.IGenericCrudDtoService;
import io.github.dainadb.improplan.domain.role.dto.RoleRequestDto;
import io.github.dainadb.improplan.domain.role.dto.RoleResponseDto;
import io.github.dainadb.improplan.domain.role.entity.Role;

/**
 * Interfaz para el servicio de gestión de Roles.
 * Extiende la interfaz CRUD genérica para proporcionar operaciones básicas
 * y añade métodos de búsqueda específicos.
 */
public interface IRoleService extends IGenericCrudDtoService<Role, RoleRequestDto, RoleResponseDto, Integer> {
    
    /**
     * Busca un rol por su nombre (tipo de rol).
     *
     * @param name El nombre del rol a buscar (ej. "ROLE_ADMIN").
     * @return un {@link Optional} que contiene el DTO del rol si se encuentra,
     *         o un Optional vacío si no existe.
     */
    Optional<RoleResponseDto> findByName(String name);
}