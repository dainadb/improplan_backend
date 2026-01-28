package io.github.dainadb.improplan.exception;

import org.springframework.http.HttpStatus;

import io.github.dainadb.improplan.exception.generic.GenericApiException;

/**
 * Excepción para representar un error HTTP 400 (Bad Request).
 * <p>
 * Se lanza cuando la solicitud del cliente es inválida, malformada o le faltan
 * parámetros requeridos que impiden que el servidor la procese.
 */
public class BadRequestException extends GenericApiException{

    /**
     * Construye una nueva BadRequestException con un mensaje de error específico.
     *
     * @param message El mensaje que describe por qué la solicitud fue incorrecta.
     */
    public BadRequestException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
