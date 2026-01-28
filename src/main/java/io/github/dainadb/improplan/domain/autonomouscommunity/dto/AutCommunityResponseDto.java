package io.github.dainadb.improplan.domain.autonomouscommunity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para la respuesta de una Comunidad Autónoma.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AutCommunityResponseDto {

    private Integer id;
    private String name;
}
