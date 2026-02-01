package io.github.dainadb.improplan.domain.role.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.dainadb.improplan.common.response.ApiResponse;
import io.github.dainadb.improplan.domain.generic.controller.GenericRestController;
import io.github.dainadb.improplan.domain.role.dto.RoleResponseDto;
import io.github.dainadb.improplan.domain.role.service.IRoleService;


import java.util.List;

/**
 * Controlador REST para gestionar las operaciones relacionadas con los roles.
 * Expone endpoints para consultar los roles del sistema.
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/roles")
public class RoleRestController extends GenericRestController {

    @Autowired
    private IRoleService roleService;

    /**
     * Devuelve una lista con todos los roles disponibles en el sistema.
     * @return ResponseEntity con una lista de RoleResponseDto y estado 200 (OK).
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<RoleResponseDto>>> getAllRoles() {
        List<RoleResponseDto> roles = roleService.findAll();
        return success(roles, "Lista de roles obtenida con éxito.");
    }

    /**
     * Busca y devuelve un rol específico por su ID.
     *
     * @param id El ID del rol a buscar.
     * @return ResponseEntity con el RoleResponseDto encontrado y estado 200 (OK).
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RoleResponseDto>> getRoleById(@PathVariable Integer id) {
        RoleResponseDto role = roleService.findById(id);
        return success(role, "Rol encontrado con éxito.");
    }
}