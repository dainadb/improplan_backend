package io.github.dainadb.improplan.model.service.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para las solicitudes de actualización del perfil de usuario.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserProfileRequestDto {
    private String name;
    private String surnames;
    
}
