package io.github.dainadb.improplan.common.utils;

import java.util.regex.Pattern;

/**
 * Clase utilitaria para validaciones comunes en el sistema.
 * 
 */
public final class Validator {
    private Validator() {
        // Constructor privado para evitar instanciar la clase
    }

    /**
     * Valida si el email proporcionado tiene un formato correcto.
     * @param email Email a validar
     * @return  true si el email es válido, false en caso contrario
     */
    public static boolean isValidEmail(String email) {
        if (email == null) return false;

        // Eliminar espacios en blanco al inicio y al final
        String trimmedEmail = email.trim();
        // No permitir que termine con un punto
        if (trimmedEmail.endsWith(".")) return false;

        // Expresión regular para validar el formato del email
        String emailRegex =
            "^([0-9a-zA-Z]([-.\\w]*[0-9a-zA-Z])*@([0-9a-zA-Z][-\\w]*[0-9a-zA-Z]\\.)+[a-zA-Z]{2,9})$";

        return Pattern.matches(emailRegex, trimmedEmail); //emailRegex es la expresión regular con la que se valida el email a través de Pattern.matches
    }

    public static boolean isValidUrl(String url) {
        if (url == null) return false;

        String trimmedUrl = url.trim();
        if( trimmedUrl.endsWith(".")) return false;
        
        // Expresión regular para validar el formato de la URL
        String urlRegex = "^(https?|ftp)://[^\\s/$.?#].[^\\s]*$";
        return Pattern.matches(urlRegex, trimmedUrl);
    }

}
