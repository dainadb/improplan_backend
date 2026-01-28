package io.github.dainadb.improplan.domain.auth.service;



import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import io.github.dainadb.improplan.common.utils.Validator;
import io.github.dainadb.improplan.domain.auth.dto.LoginRequesttDto;
import io.github.dainadb.improplan.domain.auth.dto.LoginResponseDto;
import io.github.dainadb.improplan.domain.role.entity.Role;
import io.github.dainadb.improplan.domain.role.repository.IRoleRepository;
import io.github.dainadb.improplan.domain.user.dto.UserRequestDto;
import io.github.dainadb.improplan.domain.user.dto.UserResponseDto;
import io.github.dainadb.improplan.domain.user.entity.User;
import io.github.dainadb.improplan.domain.user.repository.IUserRepository;
import io.github.dainadb.improplan.exception.BadRequestException;
import io.github.dainadb.improplan.exception.ConflictException;
import io.github.dainadb.improplan.exception.NotFoundException;
import io.github.dainadb.improplan.exception.UnauthorizedException;
import jakarta.transaction.Transactional;

/**
 * Implementación del servicio de autenticación y registro de usuarios.
 * Este servicio se centra exclusivamente en la lógica de autenticación, registro
 * y gestión del usuario de la sesión actual.
 */
@Service
public class AuthServiceImpl implements IAuthService {

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IRoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper modelMapper;

   /**
     * {@inheritDoc}
     */
    @Override
    public LoginResponseDto authenticateUser(LoginRequesttDto loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new UnauthorizedException("Credenciales inválidas"));

        validateUserForLogin(user, loginRequest.getPassword());

        return modelMapper.map(user, LoginResponseDto.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public UserResponseDto registerUser(UserRequestDto userRequestDto) {
        
        validateRegistration(userRequestDto);

        User newUser = modelMapper.map(userRequestDto, User.class);
        //Aplicación manual de campos específicos que no se mapean directamente
        newUser.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));
        newUser.setRegistrationDate(LocalDateTime.now());
        newUser.setEnabled(true); // Por defecto, habilitado

        Set<Role> roles = resolveUserRoles(userRequestDto);
        newUser.setRoles(roles);

        User savedUser = userRepository.save(newUser);

        return modelMapper.map(savedUser, UserResponseDto.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserResponseDto getCurrentUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado: " + email));

        return modelMapper.map(user, UserResponseDto.class);
    }

    // MÉTODOS PRIVADOS AUXILIARES 

    /**
     * Valida los datos de entrada para un nuevo registro de usuario.
     * @param dto DTO de registro
     */
    private void validateRegistration(UserRequestDto dto) {
        if (!Validator.isValidEmail(dto.getEmail())) {
            throw new BadRequestException("El email no tiene un formato válido: " + dto.getEmail());
        }
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new ConflictException("El email ya está en uso: " + dto.getEmail());
        }
    }

    /**
     * Valida si un usuario existente puede iniciar sesión.
     * @param user Usuario a validar
     * @param rawPassword Contraseña en texto plano proporcionada en el login
     */
    private void validateUserForLogin(User user, String rawPassword) {
        if (!user.getEnabled()) {
            throw new BadRequestException("El usuario está deshabilitado y no puede iniciar sesión.");
        }
        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new UnauthorizedException("Credenciales inválidas");
        }
    }

    /**
     * Resuelve y obtiene las entidades Role a partir de los nombres de roles del DTO.
     * Asigna el rol 'ROLE_USER' por defecto si no se especifica ninguno.
     * @param dto DTO de registro de usuario
     * @return Un conjunto de entidades Role
     */
    private Set<Role> resolveUserRoles(UserRequestDto dto) {
        if (dto.getRoles() == null || dto.getRoles().isEmpty()) {
            Role defaultRole = roleRepository.findByName(Role.RoleType.ROLE_USER)
                    .orElseThrow(() -> new NotFoundException("Rol por defecto 'ROLE_USER' no encontrado en la base de datos."));
            return Collections.singleton(defaultRole);
        }

        return dto.getRoles().stream()
                .map(roleName -> {
                    try {
                        Role.RoleType roleType = Role.RoleType.valueOf(roleName.toUpperCase());
                        return roleRepository.findByName(roleType)
                                .orElseThrow(() -> new NotFoundException("Rol no encontrado: " + roleName));
                    } catch (IllegalArgumentException e) {
                        throw new BadRequestException("Nombre de rol inválido: " + roleName);
                    }
                })
                .collect(Collectors.toSet());
    }
}


