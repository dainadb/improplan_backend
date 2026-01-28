package io.github.dainadb.improplan.domain.theme.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de respuesta para la entidad Theme.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ThemeResponseDto {
    private Integer id;
    private String name;
    private String description;
}
