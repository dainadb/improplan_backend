package io.github.dainadb.improplan.model.service.favorite.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para agregar un evento a los favoritos del
 * usuario autenticado.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FavoriteRequestDto {

    private Long eventId;

}
