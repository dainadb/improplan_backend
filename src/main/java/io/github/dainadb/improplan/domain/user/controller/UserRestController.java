package io.github.dainadb.improplan.domain.user.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.dainadb.improplan.common.response.ApiResponse;
import io.github.dainadb.improplan.domain.generic.controller.GenericRestController;
import io.github.dainadb.improplan.domain.user.dto.UserChangePasswordDto;
import io.github.dainadb.improplan.domain.user.dto.UserProfileRequestDto;
import io.github.dainadb.improplan.domain.user.dto.UserResponseDto;
import io.github.dainadb.improplan.domain.user.service.IUserService;

/**
 * Controlador REST para la gestión de usuarios.
 * Proporciona endpoints para operaciones CRUD y otras acciones específicas sobre los usuarios.
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/users")
public class UserRestController extends GenericRestController {

    @Autowired
    private IUserService userService;


    /**
     * Obtiene una lista de todos los usuarios del sistema.
     * @return ResponseEntity con la lista de usuarios.
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponseDto>>> getAllUsers() {
        List<UserResponseDto> users = userService.findAll();
        return success(users, "Lista de usuarios obtenida con éxito.");
    }

    /**
     * Busca un usuario por su ID.
     * @param id ID del usuario a buscar.
     * @return ResponseEntity con el usuario encontrado.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponseDto>> getUserById(@PathVariable Long id) {
        UserResponseDto user = userService.findById(id);
        return success(user, "Usuario encontrado con éxito.");
    }

    /**
     * Busca un usuario por su dirección de email.
     * @param email Email del usuario a buscar.
     * @return ResponseEntity con el usuario encontrado.
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<ApiResponse<UserResponseDto>> getUserByEmail(@PathVariable String email) {
        UserResponseDto user = userService.findByEmail(email);
        return success(user, "Usuario encontrado con éxito por email.");
    }

    /**
     * Filtra y devuelve usuarios que pertenecen a un rol específico.
     * @param roleName Nombre del rol (ej. "ROLE_ADMIN", "ROLE_USER").
     * @return ResponseEntity con la lista de usuarios que coinciden.
     */
    @GetMapping("/filter/role/{roleName}")
    public ResponseEntity<ApiResponse<List<UserResponseDto>>> getUsersByRole(@PathVariable String roleName) {
        List<UserResponseDto> users = userService.findByRole(roleName);
        return success(users, "Usuarios filtrados por rol: " + roleName);
    }
    
    /**
     * Filtra y devuelve usuarios según su estado de habilitación.
     * @param enabled `true` para buscar usuarios habilitados, `false` para deshabilitados.
     * @return ResponseEntity con la lista de usuarios que coinciden.
     */
    @GetMapping("/filter/enabled/{enabled}")
    public ResponseEntity<ApiResponse<List<UserResponseDto>>> getUsersByEnabledStatus(@PathVariable boolean enabled) {
        List<UserResponseDto> users = userService.findByEnabled(enabled);
        return success(users, "Usuarios filtrados por estado 'enabled': " + enabled);
    }

    /**
     * Habilita una cuenta de usuario.
     * @param id ID del usuario a habilitar.
     * @return ResponseEntity sin contenido y estado 200 (OK).
     */
    @PatchMapping("/{id}/enable")
    public ResponseEntity<ApiResponse<Void>> enableUser(@PathVariable Long id) {
        userService.enableUser(id);
        return success(null, "Usuario habilitado con éxito.");
    }

    /**
     * Deshabilita una cuenta de usuario.
     * @param id ID del usuario a deshabilitar.
     * @return ResponseEntity sin contenido y estado 200 (OK).
     */
    @PatchMapping("/{id}/disable")
    public ResponseEntity<ApiResponse<Void>> disableUser(@PathVariable Long id) {
        userService.disableUser(id);
        return success(null, "Usuario deshabilitado con éxito.");
    }
    
     /**
     * Intercambia el rol de un usuario (de ADMIN a USER o viceversa).
     * @param id ID del usuario cuyo rol se va a intercambiar.
     * @return ResponseEntity sin contenido y estado 200 (OK).
     */
    @PatchMapping("/{id}/exchange-role")
    public ResponseEntity<ApiResponse<Void>> exchangeRole(@PathVariable Long id) {
        userService.exchangeRole(id);
        return success(null, "Rol del usuario intercambiado con éxito.");
    }

    

    /**
     * Actualiza el perfil de un usuario (nombre y apellidos).
     * @param id ID del usuario a actualizar.
     * @param profileDto DTO con los nuevos datos del perfil.
     * @return ResponseEntity con el perfil del usuario actualizado.
     */
    @PutMapping("/{id}/profile")
    public ResponseEntity<ApiResponse<UserResponseDto>> updateProfile(@PathVariable Long id, @RequestBody UserProfileRequestDto profileDto) {
        UserResponseDto updatedUser = userService.updateProfile(id, profileDto);
        return success(updatedUser, "Perfil actualizado correctamente.");
    }

    /**
     * Cambia la contraseña de un usuario.
     * @param id ID del usuario cuya contraseña se cambiará.
     * @param passwordDto DTO con la nueva contraseña y su confirmación.
     * @return ResponseEntity sin contenido y estado 200 (OK).
     */
    @PutMapping("/{id}/change-password")
    public ResponseEntity<ApiResponse<Void>> changePassword(@PathVariable Long id, @RequestBody UserChangePasswordDto passwordDto) {
        userService.changePassword(id, passwordDto);
        return success(null, "Contraseña actualizada con éxito.");
    }
}