package io.github.dainadb.improplan.exception.generic;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import io.github.dainadb.improplan.common.response.ApiResponse;

/**
 * Clase centralizada que captura y maneja las excepciones en toda la aplicación .
 * <p>
 * Su propósito es interceptar las excepciones lanzadas y transformarlas en una
 * respuesta {@link ApiResponse} consistente, asegurando  un formato de error estandarizado.
 */

/*Con el uso de esta clase se evita la duplicación de código en los controladores (con el try-catch) y se garantiza una gestión uniforme de errores en toda la aplicación.*/
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Maneja las excepciones personalizadas de la aplicación que heredan de {@link GenericApiException}.
     * <p>
     * Este método permite el manejo de  todos los errores de negocio.
     * @param ex La instancia de la excepción de negocio lanzada.
     * @return  Un {@link ResponseEntity} con el código de estado y el mensaje de error definidos en la excepción.
     */
    @ExceptionHandler(GenericApiException.class) //Con la clase GenericApiException evitamos tener que crear un método por cada excepción personalizada.
    public ResponseEntity<ApiResponse<Object>> handleGenericApiExceptions(GenericApiException ex) {
        //Con ResponseEntity<ApiResponse<Object>> indicamos que el cuerpo de la respuesta será un ApiResponse que su campo data puede ser de cualquier tipo (Object).
        /*La firma ApiResponse<Object> se usa para que el método sea lo suficientemente genérico y compatible como para manejar todos los tipos de errores (con o sin data) que heredan de GenericApiException.*/
        return ResponseEntity  
                .status(ex.getStatus())
                .body(new ApiResponse<>(ex.getMessage()));
    }

    /**
     * Captura los errores de validación provenientes de anotaciones como @Valid
     * <p>
     * Construye una respuesta detallada que incluye un mapa de los campos que no superaron
     * la validación y sus respectivos mensajes de error.
     *
     * @param ex La excepción que contiene los errores de validación de campos.
     * @return Una {@link ResponseEntity} con estado 400 (Bad Request) y un mapa de errores en el campo 'data' de {@link ApiResponse}.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        Map<String, String> errors = ex.getBindingResult() 
                .getFieldErrors() 
                .stream()
                .collect(Collectors.toMap(
                        error -> error.getField(),
                        error -> error.getDefaultMessage(),
                        (existingValue, newValue) -> existingValue // En caso de campos duplicados, mantener el primero.
                ));

        ApiResponse<Map<String, String>> response = new ApiResponse<>();
        response.setSuccess(false);
        response.setMessage("Error de validación de datos");
        response.setData(errors);

        return ResponseEntity.badRequest().body(response);
    }

    /**
     * Maneja la excepción específica de Spring Security para usuarios no encontrados.
     * <p>
     * Devuelve una respuesta clara con estado 404 (Not Found) cuando el {@code UserDetailsService}
     * no logra encontrar un usuario durante el proceso de autenticación.
     *
     * @param ex La excepción lanzada por Spring Security.
     * @return Una {@link ResponseEntity} con estado 404.
     */
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleUsernameNotFound(UsernameNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>(ex.getMessage()));
    }

    /**
     * Captura cualquier excepción no controlada.
     * <p>
     * Evita que la aplicación exponga trazas de error al cliente, devolviendo un
     * error genérico con estado 500 (Internal Server Error).
     *
     * @param ex La excepción genérica o inesperada que fue capturada.
     * @return Una {@link ResponseEntity} genérica con estado 500.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleUnexpectedExceptions(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>("Ha ocurrido un error inesperado en el servidor."));
    }
}