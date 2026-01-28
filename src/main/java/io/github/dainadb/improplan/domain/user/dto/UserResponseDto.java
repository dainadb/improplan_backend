package io.github.dainadb.improplan.domain.user.dto;

import java.time.LocalDateTime;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para las respuestas de usuario.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponseDto {

    private Long id;
    private String email;
    private String name;
    private String surnames;
    private Set<String> roles;
    private LocalDateTime registrationDate;
    private boolean enabled;
}
