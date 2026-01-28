package io.github.dainadb.improplan.domain.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para la solicitud de inicio de sesión.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginRequesttDto {

    private String email;
    private String password;
}
