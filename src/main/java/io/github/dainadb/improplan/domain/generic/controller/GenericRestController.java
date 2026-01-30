package io.github.dainadb.improplan.domain.generic.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import io.github.dainadb.improplan.common.response.ApiResponse;

/**
 * Controlador genérico base para otros controladores REST.
 * Proporciona métodos comunes para manejar respuestas API y obtener información del usuario autenticado.
 */
public abstract class GenericRestController {
/**
     * Obtiene el email del usuario autenticado actual.
     *
     * @return el email de usuario, o "Anónimo" si no hay autenticación activa.
     */
    protected String getEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null ? auth.getName() : "Anónimo"; //Operador ternario usado para simplificar el if-else
            //Si auth no es null se devuelve auth.getName(), si es null se devuelve "Anónimo"
        //auth.getName() devuelve el valor del método getUsername() de la entidad User, que es el email.
    }

    /**
     * Verifica si el usuario autenticado tiene el rol de administrador.
     *
     * @return true si el usuario tiene el rol ROLE_ADMIN, false en caso contrario.
     */
    protected boolean isAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication(); //El objeto auth 
        return auth != null && auth.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
    }

    /**
     * Devuelve una respuesta HTTP 200 (OK) con un cuerpo estándar de tipo
     * ApiResponse.
     *
     * @param <T>     Tipo de dato devuelto.
     * @param data    Datos a incluir en la respuesta.
     * @param message Mensaje descriptivo.
     * @return ResponseEntity con código 200 y cuerpo formateado.
     */
    protected <T> ResponseEntity<ApiResponse<T>> success(T data, String message) {
        return ResponseEntity.ok(new ApiResponse<>(data, message));
    }

    /**
     * Devuelve una respuesta HTTP 201 (Created) con un cuerpo estándar de tipo
     * ApiResponse.
     *
     * @param <T>     Tipo de dato devuelto.
     * @param data    Datos a incluir en la respuesta.
     * @param message Mensaje descriptivo.
     * @return ResponseEntity con código 201 y cuerpo formateado.
     */
    protected <T> ResponseEntity<ApiResponse<T>> created(T data, String message) {
        return ResponseEntity.status(201).body(new ApiResponse<>(data, message));
    }

    /**
     * Devuelve una respuesta HTTP 400 (Bad Request) con un mensaje de error.
     *
     * @param <T>     Tipo de dato genérico (vacío en este caso).
     * @param message Mensaje de error.
     * @return ResponseEntity con código 400 y cuerpo formateado.
     */
    protected <T> ResponseEntity<ApiResponse<T>> failure(String message) {
        return ResponseEntity.badRequest().body(new ApiResponse<>(message));
    }

     /**
     * Devuelve una respuesta HTTP 404 (Not Found) con un mensaje de error.
     *
     * @param message Mensaje de error.
     * @return ResponseEntity con código 404 y cuerpo formateado.
     */
    protected <T> ResponseEntity<ApiResponse<T>> notFound(String message) {
        return ResponseEntity.status(404).body(new ApiResponse<>(message));
    }
}
