package io.github.dainadb.improplan.domain.auth.service;



import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Set;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import io.github.dainadb.improplan.common.utils.Validator;
import io.github.dainadb.improplan.domain.auth.dto.LoginRequesttDto;
import io.github.dainadb.improplan.domain.auth.dto.LoginResponseDto;
import io.github.dainadb.improplan.domain.auth.dto.RegisterUserDto;
import io.github.dainadb.improplan.domain.role.entity.Role;
import io.github.dainadb.improplan.domain.role.repository.IRoleRepository;
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
        if(!Validator.isValidEmail(loginRequest.getEmail())){
            throw new BadRequestException("El email no tiene un formato válido: " + loginRequest.getEmail());
        }

        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new UnauthorizedException("El email proporcionado no está registrado: "));

        validateUserForLogin(user, loginRequest.getPassword());

        return modelMapper.map(user, LoginResponseDto.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public UserResponseDto registerUser(RegisterUserDto registerDto) {
        
        validateRegistration(registerDto);

        User newUser = modelMapper.map(registerDto, User.class);
        //Aplicación manual de campos específicos que no se mapean directamente
        newUser.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        newUser.setRegistrationDate(LocalDateTime.now());
        newUser.setEnabled(true); // Por defecto, habilitado

        //Cuando se registre un usuario, se le asignará el rol "ROLE_USER" por defecto.
        //Se usa RegisterUserDto porque no se quiere que el cliente pueda asignar roles al registrarse.

        //Se busca el rol "ROLE_USER" 
        Set<Role> roles = Collections.singleton( //con singleton se indica que es un conjunto con un solo elemento
                roleRepository.findByName(Role.RoleType.ROLE_USER)
                        .orElseThrow(() -> new NotFoundException("Rol por defecto 'ROLE_USER' no encontrado en la base de datos."))
        );
       
        
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
    private void validateRegistration(RegisterUserDto dto) {
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

   
}


