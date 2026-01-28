package io.github.dainadb.improplan.domain.user.service;

import java.util.List;

import io.github.dainadb.improplan.domain.generic.service.IGenericCrudDtoService;
import io.github.dainadb.improplan.domain.user.dto.UserChangePasswordDto;
import io.github.dainadb.improplan.domain.user.dto.UserProfileRequestDto;
import io.github.dainadb.improplan.domain.user.dto.UserRequestDto;
import io.github.dainadb.improplan.domain.user.dto.UserResponseDto;
import io.github.dainadb.improplan.domain.user.entity.User;

/**
 * Interfaz para el servicio de gestión de usuarios.
 * Extiende la funcionalidad CRUD genérica con operaciones específicas para usuarios.
 */
public interface IUserService extends IGenericCrudDtoService<User, UserRequestDto, UserResponseDto, Long> {

    /**
     * Actualiza el perfil de un usuario existente (nombre y apellidos).
     *
     * @param id                    ID del usuario a actualizar.
     * @param userProfileRequestDto DTO con los datos del perfil.
     * @return El DTO del usuario con los datos actualizados.
     */
    UserResponseDto updateProfile(Long id, UserProfileRequestDto userProfileRequestDto);

    /**
     * Cambia la contraseña de un usuario.
     *
     * @param id               ID del usuario.
     * @param changePasswordDto DTO con la nueva contraseña.
     */
    void changePassword(Long id, UserChangePasswordDto changePasswordDto);

    /**
     * Habilita un usuario en el sistema.
     *
     * @param id ID del usuario a habilitar.
     */
    void enableUser(Long id);

    /**
     * Deshabilita un usuario en el sistema.
     *
     * @param id ID del usuario a deshabilitar.
     */
    void disableUser(Long id);

    /**
     * Busca y devuelve todos los usuarios filtrados por su estado (habilitado/deshabilitado).
     *
     * @param enabled El estado por el cual filtrar (true para habilitados, false para deshabilitados).
     * @return Una lista de DTOs de los usuarios que coinciden con el estado.
     */
    List<UserResponseDto> findByEnabled(boolean enabled);
}