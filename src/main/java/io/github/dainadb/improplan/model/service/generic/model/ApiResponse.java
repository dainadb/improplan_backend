package io.github.dainadb.improplan.model.service.generic.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase genérica para respuestas API uniformes.
 *
 * @param <T> Representa el tipo de dato devuelto en la respuesta.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {

    private boolean success;

    private String message;

    private T data; // Se usa la T por convención de tipos genéricos, indicamos que no tiene un tipo fijo
    //De esta manera se puede usar ApiResponser para devolver cualquier tipo de dato:ApiResponse<User>, ApiResponse<List<Product>>, ApiResponse<Map<String, String>>



    //Se crean diferentes constructores que indican si la respuesta es exitosa o no.

    /**
     * Constructor para respuestas exitosas con datos y mensaje por defecto.
     * 
     * @param data Datos devueltos
     */
    public ApiResponse(T data) {
        this.success = true;
        this.message = "Operación exitosa";
        this.data = data;
    }

    /**
     * Constructor para respuestas exitosas con datos y mensaje personalizado.
     * 
     * @param data    Datos devueltos
     * @param message Mensaje personalizado
     */
    public ApiResponse(T data, String message) {
        this.success = true;
        this.message = message;
        this.data = data;
    }

    /**
     * Constructor para respuestas con error.
     * 
     * @param message Mensaje del error
     */
    public ApiResponse(String message) {
        this.success = false;
        this.message = message;
        this.data = null;
    }
}