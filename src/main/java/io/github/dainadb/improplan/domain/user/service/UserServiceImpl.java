package io.github.dainadb.improplan.domain.user.service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import io.github.dainadb.improplan.common.utils.Validator;
import io.github.dainadb.improplan.domain.generic.service.GenericCrudDtoServiceImpl;
import io.github.dainadb.improplan.domain.role.entity.Role;
import io.github.dainadb.improplan.domain.role.repository.IRoleRepository;
import io.github.dainadb.improplan.domain.user.dto.UserChangePasswordDto;
import io.github.dainadb.improplan.domain.user.dto.UserProfileRequestDto;
import io.github.dainadb.improplan.domain.user.dto.UserRequestDto;
import io.github.dainadb.improplan.domain.user.dto.UserResponseDto;
import io.github.dainadb.improplan.domain.user.entity.User;
import io.github.dainadb.improplan.domain.user.repository.IUserRepository;
import io.github.dainadb.improplan.exception.BadRequestException;
import io.github.dainadb.improplan.exception.ConflictException;
import io.github.dainadb.improplan.exception.NotFoundException;
import jakarta.transaction.Transactional;

/**
 * Implementación del servicio para la gestión de usuarios.
 * Hereda la lógica CRUD genérica y añade funcionalidades específicas para la entidad User.
 */
@Service
public class UserServiceImpl extends GenericCrudDtoServiceImpl<User, UserRequestDto, UserResponseDto, Long> 
                             implements IUserService {

    @Autowired
    private IUserRepository userRepository;
    
    @Autowired
    private IRoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    // --- Implementación de métodos abstractos de la clase genérica ---

    @Override
    protected JpaRepository<User, Long> getRepository() {
        return userRepository;
    }

    @Override
    protected Class<User> getEntityClass() {
        return User.class;
    }

    @Override
    protected Class<UserRequestDto> getRequestDtoClass() {
        return UserRequestDto.class;
    }

    @Override
    protected Class<UserResponseDto> getResponseDtoClass() {
        return UserResponseDto.class;
    }

    // --- Sobrescritura de métodos CRUD para añadir lógica específica ---

    

    @Override
    @Transactional
    public UserResponseDto update(Long id, UserRequestDto dto) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado con el id: " + id));

        // Validación específica antes de actualizar
        validateUpdate(id, dto);

        // Mapeo de campos básicos del DTO a la entidad
        modelMapper.map(dto, existingUser);

        // Lógica específica para campos que no se mapean directamente
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        existingUser.setRoles(resolveUserRoles(dto.getRoles()));

        User updatedUser = userRepository.save(existingUser);
        return convertToResponseDto(updatedUser);
    }
    
   

    @Override
    protected void validateUpdate(Long id, UserRequestDto dto) {
        if (!Validator.isValidEmail(dto.getEmail())) {
            throw new BadRequestException("El email no tiene un formato válido: " + dto.getEmail());
        }
        // Verifica si el nuevo email ya está en uso por OTRO usuario
        userRepository.findByEmail(dto.getEmail()).ifPresent(user -> {
            if (!user.getId().equals(id)) {
                throw new ConflictException("El email ya está en uso por otro usuario: " + dto.getEmail());
            }
        });
    }

    // --- Implementación de métodos específicos de IUserService ---

    @Override
    @Transactional
    public UserResponseDto updateProfile(Long id, UserProfileRequestDto profileDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado con el id: " + id));
        
        user.setName(profileDto.getName());
        user.setSurnames(profileDto.getSurnames());

        User updatedUser = userRepository.save(user);
        return convertToResponseDto(updatedUser);
    }

    @Override
    @Transactional
    public void changePassword(Long id, UserChangePasswordDto changePasswordDto) {
        if (!changePasswordDto.getNewPassword().equals(changePasswordDto.getCheckedPassword())) {
            throw new BadRequestException("Las contraseñas no coinciden.");
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado con el id: " + id));
        
        user.setPassword(passwordEncoder.encode(changePasswordDto.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void enableUser(Long id) {
        toggleUserStatus(id, true);
    }

    @Override
    @Transactional
    public void disableUser(Long id) {
        toggleUserStatus(id, false);
    }

    @Override
    public List<UserResponseDto> findByEnabled(boolean enabled) {
        return userRepository.findByEnabled(enabled).stream()
                .map(this::convertToResponseDto)
                .toList();
    }

    // --- Métodos privados auxiliares ---
    
    /**
     * Resuelve los roles a partir de una lista de nombres de rol.
     * Asigna 'ROLE_USER' por defecto si la lista es nula o vacía.
     * 
     * @param roleNames Nombres de los roles.
     * @return Conjunto de entidades Role.
     */
    private Set<Role> resolveUserRoles(Set<String> roleNames) {
        if (roleNames == null || roleNames.isEmpty()) {
            Role defaultRole = roleRepository.findByName(Role.RoleType.ROLE_USER)
                    .orElseThrow(() -> new NotFoundException("Rol por defecto 'ROLE_USER' no encontrado."));
            return Collections.singleton(defaultRole);
        }

        return roleNames.stream()
                .map(roleName -> {
                    try {
                        Role.RoleType roleType = Role.RoleType.valueOf(roleName.toUpperCase());
                        return roleRepository.findByName(roleType)
                                .orElseThrow(() -> new NotFoundException("Rol no encontrado: " + roleName));
                    } catch (IllegalArgumentException e) {
                        throw new BadRequestException("Nombre de rol inválido: " + roleName);
                    }
                })
                .collect(Collectors.toCollection(HashSet::new));
    }
    
    /**
     * Cambia el estado 'enabled' de un usuario.
     * 
     * @param id ID del usuario.
     * @param status Nuevo estado (true para habilitar, false para deshabilitar).
     */
    private void toggleUserStatus(Long id, boolean status) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado con el id: " + id));
        user.setEnabled(status);
        userRepository.save(user);
    }
}