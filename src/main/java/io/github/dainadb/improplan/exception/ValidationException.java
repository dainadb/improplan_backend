package io.github.dainadb.improplan.exception;

import org.springframework.http.HttpStatus;

import io.github.dainadb.improplan.exception.generic.GenericApiException;

public class ValidationException extends GenericApiException{

    /**
     * Construye una nueva ValidationException cuando una validación falla.
     *
     * @param message El mensaje que describe la razón de la validación fallida.
     */
    public ValidationException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
