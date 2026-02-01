package io.github.dainadb.improplan.domain.autonomouscommunity.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.github.dainadb.improplan.common.response.ApiResponse;
import io.github.dainadb.improplan.domain.autonomouscommunity.dto.AutCommunityResponseDto;
import io.github.dainadb.improplan.domain.autonomouscommunity.service.IAutonomousCommunityService;
import io.github.dainadb.improplan.domain.generic.controller.GenericRestController;

/**
 * Controlador REST para gestionar las Comunidades Autónomas.
 * Expone endpoints para consultar la lista de comunidades, obtener una por ID o por nombre.
 * Dado que estos datos son de solo lectura (se cargan inicialmente en la BBDD),
 * no se implementan métodos de creación, actualización o borrado.
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/communities")
public class AutCommunityRestController extends GenericRestController {

    @Autowired
    private IAutonomousCommunityService autCommunityService;

    /**
     * Obtiene una lista de todas las Comunidades Autónomas.
     *
     * @return ResponseEntity con la lista de comunidades y un mensaje de éxito.
     */
   
    @GetMapping
    public ResponseEntity<ApiResponse<List<AutCommunityResponseDto>>> getAllCommunity() {
        List<AutCommunityResponseDto> communities = autCommunityService.findAll();
        return success(communities, "Lista de comunidades autónomas obtenida con éxito.");
    }
    
    /**
     * Busca una Comunidad Autónoma por su ID.
     *
     * @param id El ID de la comunidad a buscar.
     * @return ResponseEntity con los datos de la comunidad encontrada.
     */
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AutCommunityResponseDto>> getCommunityById(@PathVariable Integer id) {
        AutCommunityResponseDto community = autCommunityService.findById(id);
        return success(community, "Comunidad autónoma encontrada con éxito.");
    }

    /**
     * Busca una Comunidad Autónoma por su nombre.
     * La búsqueda no distingue entre mayúsculas y minúsculas.
     *
     * @param name El nombre de la comunidad a buscar.
     * @return ResponseEntity con los datos de la comunidad encontrada.
     */
   
    @GetMapping("/by-name")
    public ResponseEntity<ApiResponse<AutCommunityResponseDto>> getCommunityByName(@RequestParam String name) {
        AutCommunityResponseDto community = autCommunityService.findByName(name);
        return success(community, "Comunidad autónoma encontrada con éxito.");
    }
}