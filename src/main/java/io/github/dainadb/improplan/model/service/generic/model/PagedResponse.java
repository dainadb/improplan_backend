package io.github.dainadb.improplan.model.service.generic.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Clase genérica utilizada para representar una respuesta paginada en la API.
 *
 * @param <T> Tipo de dato contenido en la página
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PagedResponse<T> {

    private String message; //Mensaje descriptivo de la operación
    private List<T> data; //Lista de elementos de la página actual
    private long totalItems; //Número total de elementos disponibles
    private int totalPages; //Número total de páginas disponibles
    private int page; //Número de la página actual (comienza en 0)
    private int pageSize;//Número de elementos por página
}