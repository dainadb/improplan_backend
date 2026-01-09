package io.github.dainadb.improplan.config.exception;

import org.springframework.http.HttpStatus;
import io.github.dainadb.improplan.config.exception.generic.GenericApiException;

/**
 * Excepción personalizada para representar un error HTTP 403 (Forbidden).
 * <p>
 * Se debe lanzar cuando el servidor entiende la solicitud pero se niega a autorizarla
 * porque el cliente autenticado no tiene los permisos necesarios para realizar la acción.
 */
public class ForbiddenException extends GenericApiException {

    /**
     * Construye una nueva ForbiddenException con un mensaje de error específico.
     *
     * @param message El mensaje que describe por qué el acceso fue denegado.
     */
    public ForbiddenException(String message) {
        super(message, HttpStatus.FORBIDDEN);
    }
}