package io.github.dainadb.improplan.domain.municipality.controller;

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
import io.github.dainadb.improplan.domain.generic.controller.GenericRestController;
import io.github.dainadb.improplan.domain.municipality.dto.MunicipalityResponseDto;
import io.github.dainadb.improplan.domain.municipality.service.IMunicipalityService;

/**
 * Controlador REST para la consulta de Municipios.
 * Expone endpoints para obtener listas de municipios, buscar por nombre o
 * filtrarlos por provincia. Como son solo datos de lectura,
 * no se exponen endpoints de escritura (POST, PUT, DELETE).
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/municipalities")
public class MunicipalityRestController extends GenericRestController {

    @Autowired
    private IMunicipalityService municipalityService;

    
    /**
     * Busca un municipio por su ID.
     *
     * @param id El ID del municipio a buscar.
     * @return ResponseEntity con los datos del municipio encontrado.
     */
   
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MunicipalityResponseDto>> getMunicipalityById(@PathVariable Integer id) {
        MunicipalityResponseDto municipality = municipalityService.findById(id);
        return success(municipality, "Municipio encontrado con éxito.");
    }
    
    /**
     * Busca municipios cuyo nombre contiene el texto proporcionado (para autocompletado).
     * La búsqueda no distingue entre mayúsculas y minúsculas.
     *
     * @param name El texto a buscar en el nombre del municipio.
     * @return ResponseEntity con una lista de municipios que coinciden con el criterio.
     */
  
    @GetMapping("/by-name")
    public ResponseEntity<ApiResponse<List<MunicipalityResponseDto>>> searchMunicipalitiesByName(@RequestParam String name) {
        List<MunicipalityResponseDto> municipalities = municipalityService.findByNameContaining(name);
        return success(municipalities, "Búsqueda de municipios por nombre completada.");
    }

    /**
     * Obtiene una lista de municipios que pertenecen a una provincia específica.
     *
     * @param provinceName El nombre exacto de la provincia.
     * @return ResponseEntity con la lista de municipios de esa provincia.
     */
   
    @GetMapping("/by-province")
    public ResponseEntity<ApiResponse<List<MunicipalityResponseDto>>> getMunicipalitiesByProvince(@RequestParam String provinceName) {
        List<MunicipalityResponseDto> municipalities = municipalityService.findByProvinceName(provinceName.trim());
        return success(municipalities, "Municipios de la provincia '" + provinceName + "' obtenidos con éxito.");
    }
}