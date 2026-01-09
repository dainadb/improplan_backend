package io.github.dainadb.improplan.config.exception;

import org.springframework.http.HttpStatus;
import io.github.dainadb.improplan.config.exception.generic.GenericApiException;

/**
 * Excepción personalizada para representar un error HTTP 401 (Unauthorized).
 * <p>
 * Se lanza cuando el cliente no ha proporcionado credenciales válidas y no se le puede autenticar.
 */
public class UnauthorizedException extends GenericApiException {

    /**
     * Construye una nueva UnauthorizedException con un mensaje de error específico.
     *
     * @param message El mensaje que indica por qué la autenticación falló.
     */
    public UnauthorizedException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }
}