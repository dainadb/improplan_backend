package io.github.dainadb.improplan.model.service.user.dto;


import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para las solicitudes de creación de usuarios.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequestDto {
    private String email;
    private String name;
    private String surnames;
    private String password;
    private Set<String> roles;
    

}
