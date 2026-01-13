package io.github.dainadb.improplan.model.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
/**
 * Entidad que representa un rol en el sistema.
 * Define los permisos y el nivel de acceso de un usuario.
 */
@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Identificador único del rol.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_role")
    private Integer id;

    /**
     * Nombre del rol, basado en el enum RoleType.
     * Se almacena como texto en la base de datos gracias a EnumType.STRING.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true, length = 50)
    private RoleType name;

    /**
     * Descripción textual de las responsabilidades del rol.
     */
    @Column(length = 255)
    private String description;

  /**
     * Enumeración interna para los tipos de rol disponibles.
     * Al estar dentro de la clase Role, se accede como Role.RoleType.
     */
    public enum RoleType {
        ROLE_ADMIN,
        ROLE_USER
    }

}