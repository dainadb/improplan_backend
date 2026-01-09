package io.github.dainadb.improplan.config.exception;

import org.springframework.http.HttpStatus;
import io.github.dainadb.improplan.config.exception.generic.GenericApiException;

/**
 * Excepción personalizada para representar un error HTTP 404 (Not Found).
 * <p>
 * Se debe lanzar cuando el servidor no puede encontrar el recurso solicitado
 * por el cliente.
 */
public class NotFoundException extends GenericApiException {

    /**
     * Construye una nueva NotFoundException con un mensaje de error específico.
     *
     * @param message El mensaje que indica qué recurso no pudo ser encontrado.
     */
    public NotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}