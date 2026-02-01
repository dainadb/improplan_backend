package io.github.dainadb.improplan.domain.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.dainadb.improplan.common.response.ApiResponse;
import io.github.dainadb.improplan.domain.auth.dto.LoginRequesttDto;
import io.github.dainadb.improplan.domain.auth.dto.LoginResponseDto;
import io.github.dainadb.improplan.domain.auth.dto.RegisterUserDto;
import io.github.dainadb.improplan.domain.auth.service.IAuthService;
import io.github.dainadb.improplan.domain.generic.controller.GenericRestController;
import io.github.dainadb.improplan.domain.user.dto.UserResponseDto;
import jakarta.validation.Valid;
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/auth")
public class AuthRestController extends GenericRestController {

    @Autowired
    private IAuthService authService;

    /**
     * Inicia sesión de un usuario.
     * @param loginDto Datos de inicio de sesión (email y contraseña).
     * @return Respuesta en formto ApiResponse con los detalles del usuario autenticado.
     */

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDto>> login(@RequestBody @Valid LoginRequesttDto loginDto) {
        LoginResponseDto response = authService.authenticateUser(loginDto);

        //Se crea un objeto de autenticación con los 3 parámetros: principal (email), credentials (null porque ya se ha autenticado) y authorities (roles)
        var authtoken = new UsernamePasswordAuthenticationToken( 
                        response.getEmail(),
                        null,
                        response.getRoles().stream() // Se mapean los roles (String) a SimpleGrantedAuthority
                                .map(role -> new SimpleGrantedAuthority(role))
                                .toList());

        SecurityContextHolder.getContext().setAuthentication(authtoken); //Se le asigna la autenticación al contexto de seguridad

        return success(response, "Usuario logueado correctamente");
    }

    /**
     * Cierra la sesión del usuario autenticado.
     * @return Respuesta en formato ApiResponse indicando el éxito del logout.
     */
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout() {
        SecurityContextHolder.clearContext();
        return success(null, "Logout exitoso");
    }

    /**
     * Registra un nuevo usuario en el sistema.
     * @param registerDto Datos del nuevo usuario a registrar.
     * @return Respuesta en formato ApiResponse con los detalles del usuario registrado.
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponseDto>> register(@RequestBody @Valid RegisterUserDto registerDto) {
        UserResponseDto response = authService.registerUser(registerDto);
        return created(response, "Usuario registrado correctamente");
    }
    
    /**
     * Obtiene los detalles del usuario actualmente autenticado.
     * @return Respuesta en formato ApiResponse con los detalles del usuario autenticado.
     */
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponseDto>> getAuthenticatedUser() {
        UserResponseDto user = authService.getCurrentUser(getEmail()); //Con el método getEmail de la clase GenericRestController obtenemos el email del usuario autenticado
        return success(user, "Usuario autenticado");
    }




}
