package io.github.dainadb.improplan.exception;

import org.springframework.http.HttpStatus;

import io.github.dainadb.improplan.exception.generic.GenericApiException;

/**
 * Excepción personalizada para representar un error HTTP 409 (Conflict).
 * <p>
 * Se debe lanzar cuando una solicitud no puede ser procesada debido a un
 * conflicto con el estado actual del recurso, como una violación de
 * una restricción de unicidad.
 */
public class ConflictException extends GenericApiException {

    /**
     * Construye una nueva ConflictException con un mensaje de error específico.
     *
     * @param message El mensaje que describe la naturaleza del conflicto.
     */
    public ConflictException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}