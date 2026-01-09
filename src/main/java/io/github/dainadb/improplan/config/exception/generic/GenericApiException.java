package io.github.dainadb.improplan.config.exception.generic;

import org.springframework.http.HttpStatus;

import lombok.Getter;

/**
 * Clase base para todas las excepciones personalizadas de la API.
 * Permite asociar un mensaje y un código de estado HTTP a cada error de negocio.
 */
@Getter
public class GenericApiException extends RuntimeException {

    //Es tipo HttpStatus para aprovechar los códigos de estado HTTP predefinidos en Spring.
    private final HttpStatus status; //Al ser final una vez asignado en el constructor no puede cambiarse.

    /**
     * Construye una nueva instancia de GenericApiException.
     *
     * @param message El mensaje descriptivo del error.
     * @param status  El código de estado HTTP asociado al error.
     */
    public GenericApiException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

}
