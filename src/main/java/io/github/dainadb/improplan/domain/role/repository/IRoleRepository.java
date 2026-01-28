package io.github.dainadb.improplan.domain.role.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.dainadb.improplan.domain.role.entity.Role;
import io.github.dainadb.improplan.domain.role.entity.Role.RoleType;

/** 
 * Repositorio para la entidad Role. 
 * Permite realizar operaciones CRUD y consultas sobre la tabla de roles.
*/
public interface IRoleRepository extends JpaRepository<Role, Integer> {

    /**
     * Busca un rol por su nombre que es tipo Roletype(por ejemplo: ROLE_ADMIN, ROLE_USER).
     *
     * @param name Tipo de rol
     * @return Optional con el rol si existe
     */
    Optional<Role> findByName(RoleType name);
}
