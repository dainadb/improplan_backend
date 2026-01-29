package io.github.dainadb.improplan.domain.user.service;

import java.util.List;

import io.github.dainadb.improplan.domain.user.dto.UserChangePasswordDto;
import io.github.dainadb.improplan.domain.user.dto.UserProfileRequestDto;
import io.github.dainadb.improplan.domain.user.dto.UserResponseDto;

/**
 * Interfaz para el servicio de gestión de usuarios.
 * Define las operaciones de negocio específicas para la entidad User.
 */
public interface IUserService {

   
    /**
     * Busca y devuelve un usuario por su ID.
     * @param id ID del usuario.
     * @return El DTO del usuario.
     */
    UserResponseDto findById(Long id);

    /**
     * Devuelve una lista de todos los usuarios.
     * @return Lista de DTOs de usuarios.
     */
    List<UserResponseDto> findAll();

    /**
     * Busca y devuelve un usuario por su correo electrónico.
     * @param email Correo electrónico del usuario.
     * @return El DTO del usuario.
     */

    UserResponseDto findByEmail(String email);

    /**
     * Busca y devuelve todos los usuarios que tienen un rol específico.
     * @param roleName Nombre del rol.
     * @return Lista de DTOs de usuarios que tienen el rol especificado.
     */
    List<UserResponseDto> findByRole(String roleName);

    /**
     * Busca y devuelve todos los usuarios filtrados por su estado (habilitado/deshabilitado).
     * @param enabled El estado por el cual filtrar.
     * @return Una lista de DTOs de los usuarios que coinciden con el estado.
     */
    List<UserResponseDto> findByEnabled(boolean enabled);

    /**
     * Actualiza el perfil de un usuario (nombre y apellidos).
     * @param id ID del usuario a actualizar.
     * @param profileDto DTO con los datos del perfil.
     * @return El DTO del usuario con los datos actualizados.
     */
    UserResponseDto updateProfile(Long id, UserProfileRequestDto profileDto);

    /**
     * Intercambia el rol de un usuario entre 'ROLE_USER' y 'ROLE_ADMIN'.   
     * @param id    ID del usuario.    
     */
    void exchangeRole (Long id);

    /**
     * Cambia la contraseña de un usuario.
     * @param id ID del usuario.
     * @param changePasswordDto DTO con la nueva contraseña.
     */
    void changePassword(Long id, UserChangePasswordDto changePasswordDto);

    /**
     * Habilita un usuario en el sistema.
     * @param id ID del usuario a habilitar.
     */
    void enableUser(Long id);

    /**
     * Deshabilita un usuario en el sistema (Soft Delete).
     * Esta es la operación que reemplaza al borrado físico.
     * @param id ID del usuario a deshabilitar.
     */
    void disableUser(Long id);

    
}