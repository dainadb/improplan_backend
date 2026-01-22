package io.github.dainadb.improplan.model.service.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para las solicitudes de cambio de contraseña de usuario.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserChangePasswordDto {
    private String newPassword;
    private String checkedPassword;
}
