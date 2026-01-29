package io.github.dainadb.improplan.domain.user.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.dainadb.improplan.domain.role.entity.Role;
import io.github.dainadb.improplan.domain.role.repository.IRoleRepository;
import io.github.dainadb.improplan.domain.user.dto.*;
import io.github.dainadb.improplan.domain.user.entity.User;
import io.github.dainadb.improplan.domain.user.repository.IUserRepository;
import io.github.dainadb.improplan.exception.BadRequestException;
import io.github.dainadb.improplan.exception.NotFoundException;

/**
 * Implementación del servicio de usuario.
 */
//Tiene una lógica de negocio compleja que no encaja bien con el CRUD genérico, por eso no extiende de este.
@Service
public class UserServiceImpl implements IUserService {

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
    public UserResponseDto findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado con el id: " + id));
        return convertToResponseDto(user);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UserResponseDto> findAll() {
        return userRepository.findAll().stream()
                .map(this::convertToResponseDto)
                .toList();
    }

    /**
     * {@inheritDoc}
     */
     @Override
    public UserResponseDto findByEmail(String email) {
         User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado con el email: " + email));
         return convertToResponseDto(user);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UserResponseDto> findByRole(String roleName) {
        try {
            // Se convierte el nombre del rol (que es tipo String) a su tipo Enum correspondiente.
            Role.RoleType roleType = Role.RoleType.valueOf(roleName.toUpperCase());
            
            return userRepository.findByRolesName(roleType).stream()
                .map(this::convertToResponseDto)
                .toList();

        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Nombre de rol inválido: " + roleName);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UserResponseDto> findByEnabled(boolean enabled) {
        return userRepository.findByEnabled(enabled).stream()
                .map(this::convertToResponseDto)
                .toList();
    }
    
    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
    //Se ha implementado este método porque de momento solo hay dos roles y es más sencillo intercambiarlos así. 
    // Y los usuarios solo contendrán uno (aunque la entidad User permita más).
    @Override
    public void exchangeRole(Long userId) {
         User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado con ID: " + userId));

        boolean isCurrentlyAdmin = user.getRoles().stream()
                .anyMatch(r -> r.getName() == Role.RoleType.ROLE_ADMIN);

        // Se determina el rol opuesto
        Role.RoleType newRoleType;

            if (isCurrentlyAdmin) {
                // Si el usuario ya es ADMIN, el nuevo rol será USER
                newRoleType = Role.RoleType.ROLE_USER;
            } else {
                // Si no es ADMIN (es decir, es USER), el nuevo rol será ADMIN
                newRoleType = Role.RoleType.ROLE_ADMIN;
            }

        
        Role newRole = roleRepository.findByName(newRoleType)
                .orElseThrow(() -> new NotFoundException("Rol no encontrado: " + newRoleType.name()));

        //Se crea un nuevo conjunto de roles y se le asigna únicamente el nuevo rol
        Set<Role> roles = new HashSet<>();
        roles.add(newRole);
        user.setRoles(roles);

        userRepository.save(user);
    }
    

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void enableUser(Long id) {
        changeUserStatus(id, true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void disableUser(Long id) {
        changeUserStatus(id, false);
    }


 //  Métodos privados auxiliares 
    
    /**
     * Cambia el estado de habilitación de un usuario.
     * @param id ID del usuario.
     * @param status Nuevo estado de habilitación.
     */
    private void changeUserStatus(Long id, boolean status) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado con el id: " + id));
        user.setEnabled(status);
        userRepository.save(user);
    }

    /**
     * Convierte una entidad User a un UserResponseDto.
     * @param user La entidad User.
     * @return El UserResponseDto correspondiente.
     */
    private UserResponseDto convertToResponseDto(User user) {
        return modelMapper.map(user, UserResponseDto.class);
    }

    

}   