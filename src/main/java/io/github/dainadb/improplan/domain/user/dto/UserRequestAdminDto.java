package io.github.dainadb.improplan.domain.user.dto;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para las solicitudes de actualización de usuarios por parte de administradores.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequestAdminDto {
    private boolean enabled;
    private Set<String> roles;
}
