package io.github.dainadb.improplan.model.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import io.github.dainadb.improplan.model.entity.Role.RoleType;
import io.github.dainadb.improplan.model.entity.User;

/** 
 * Repositorio para la entidad User. 
 * Permite realizar operaciones CRUD y consultas sobre la tabla de usuarios.
*/
public interface IUserRepository extends JpaRepository<User, Long> {


    /**
     * Busca un usuario por su correo electrónico.
     * @param email Correo electrónico del usuario.
     * @return un {@link Optional} que contiene el usuario si existe, o vacío en caso contrario
     */
    Optional<User> findByEmail(String email);

    /**
     * Busca un usuario por su correo electrónico, asegurándose de que esté habilitado.
     * @param email Correo electrónico del usuario.
     * @return un {@link Optional} que contiene el usuario habilitado si existe, o vacío en caso contrario
     */
    Optional<User> findByEmailAndEnabledTrue(String email);

    /**
     * Busca usuarios que tienen un rol específico.
     * @param name Nombre del rol (RoleType).
     * @return Lista de usuarios que poseen el rol especificado.
     */
    List<User> findByRolesName (RoleType name);


    /**
     * Busca usuarios según si están habilitados o no.
     * @param enabled Indica si el usuario está habilitado (true) o deshabilitado (false).
     * @return Lista de usuarios que coinciden con el criterio de habilitación.
     */
    List<User> findByEnabled(Boolean enabled);
    
    //MÉTODOS PAGINADOS
    
    //Valorar si es necesario añadir más métodos paginados.
    /**
     * Recupera una página de usuarios.
     * @param pageable Objeto Pageable que define la paginación y ordenación.
     * @return Una página de usuarios.
     */
    Page<User> findAll(Pageable pageable);

    


}
