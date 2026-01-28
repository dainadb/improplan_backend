package io.github.dainadb.improplan.domain.auth.service;

import io.github.dainadb.improplan.domain.auth.dto.LoginRequesttDto;
import io.github.dainadb.improplan.domain.auth.dto.LoginResponseDto;
import io.github.dainadb.improplan.domain.user.dto.UserRequestDto;
import io.github.dainadb.improplan.domain.user.dto.UserResponseDto;


public interface IAuthService {
/**
 *  Autentica a un usuario utilizando las credenciales (email y contraseña) proporcionadas en el DTO.
 * @param dto contiene las credenciales del usuario.
 * @return respuesta con los datos del usuario autenticado y roles
 */
  LoginResponseDto authenticateUser (LoginRequesttDto dto);

  /**
   * Registra un nuevo usuario en el sistema utilizando los datos proporcionados en el DTO.
   * @param dto contiene los datos del nuevo usuario.
   * @return respuesta con los datos del usuario registrado.
   */
  UserResponseDto registerUser(UserRequestDto dto);

  /**
   * Obtiene los detalles del usuario actual (que ha iniciado sesión) basado en su email.
   * @param email contiene el correo electrónico del usuario.
   * @return respuesta con los datos del usuario actual.
   */
  UserResponseDto getCurrentUser(String email);
}
