package io.github.dainadb.improplan.domain.user.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import io.github.dainadb.improplan.domain.role.entity.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidad que representa a un usuario del sistema.
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements Serializable, UserDetails {

   private static final long serialVersionUID = 1L;

    /**
     * Identificador único del usuario (PK).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user")
    private Long id;

    /**
     * Email del usuario, debe ser único. Se usará para el login.
     */
    @Column(nullable = false, unique = true, length = 150)
    private String email;

    /**
     * Nombre del usuario.
     */
    @Column(nullable = false, length = 50)
    private String name;

    /**
     * Apellidos del usuario.
     */
    @Column(nullable = false, length = 100)
    private String surnames;

    /**
     * Contraseña del usuario, almacenada de forma segura (cifrada).
     */
    @Column(nullable = false, length = 255)
    private String password;

    /**
     * Fecha en la que el usuario se registró.
     */
    @Column(name = "registration_date", updatable = false) // updatable = false para que no se modifique después de crearse.
    private LocalDateTime registrationDate;

    /**
     * Indica si el usuario está habilitado o no para acceder al sistema.
     * Por defecto, un usuario nuevo está habilitado.
     */
    @Builder.Default //Para que no ignore el valor por defecto al usar el builder de Lombok
    @Column(nullable = false)
    private Boolean enabled = true ;


    /**
     * Roles asignados al usuario para gestionar permisos.
     * Relación muchos a muchos con la entidad Role (unidireccional).
     */
    @Builder.Default //Para que no ignore el valor por defecto al usar el builder de Lombok
    @ManyToMany(fetch = FetchType.EAGER) //EAGER para cargar los roles junto con el usuario y así tener los permisos disponibles al autenticar.
    @JoinTable(
        name= "users_roles",
        joinColumns = @JoinColumn(name = "id_user"),
        inverseJoinColumns = @JoinColumn(name = "id_role")
    )
    //Se usa Set para evitar roles duplicados.
    private Set<Role> roles = new HashSet<>(); //Se inicializa la colección para evitar NullPointerException (que nunca sea null) al intentar añadir roles.

    
    /**  
     * Métodos de la interfaz UserDetails
    **/
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name())) //Con role.getName() accedemos al atributo name que es de tipo RoleType (enum) y luego con el método name() de la clase enum obtenemos su representación en String.
                .toList(); //Se tranforma el objeto role a SimpleGrantedAuthority que es lo que Spring Security usa para gestionar los permisos.
    }

    @Override
    public String getUsername() {
        // Usamos el email como nombre de usuario para la autenticación
        return this.email;
    }

   
}