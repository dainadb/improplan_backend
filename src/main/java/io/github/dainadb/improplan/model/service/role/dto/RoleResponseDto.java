package io.github.dainadb.improplan.model.service.role.dto;

import io.github.dainadb.improplan.model.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de respuesta para la entidad Role.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoleResponseDto {

    private Integer id;
    private Role.RoleType name;
    private String description;
}
