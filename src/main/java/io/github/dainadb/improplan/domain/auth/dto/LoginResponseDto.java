package io.github.dainadb.improplan.domain.auth.dto;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para la respuesta de inicio de sesión.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponseDto {
    private String email;
    private String name;
    private Set<String> roles;

}
