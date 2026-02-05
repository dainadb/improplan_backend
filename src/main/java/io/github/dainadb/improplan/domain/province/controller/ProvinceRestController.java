package io.github.dainadb.improplan.domain.province.controller;

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
import io.github.dainadb.improplan.domain.province.dto.ProvinceResponseDto;
import io.github.dainadb.improplan.domain.province.service.IProvinceService;

/**
 * Controlador REST para la consulta de Provincias.
 * Proporciona endpoints para obtener todas las provincias, buscar por ID y buscar por nombre.
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/provinces")
public class ProvinceRestController extends GenericRestController {

    @Autowired
    private IProvinceService provinceService;

  
    @GetMapping("/by-community")
    public ResponseEntity<ApiResponse<List<ProvinceResponseDto>>> getProvincesByCommunity(@RequestParam String communityName) {
        
        List<ProvinceResponseDto> provinces = provinceService.findByAutonomousCommunityName(communityName.trim());
        
        return success(provinces, "Lista de provincias obtenida con éxito.");
    }

   
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProvinceResponseDto>> getProvinceById(@PathVariable Integer id) {
        ProvinceResponseDto province = provinceService.findById(id);
        return success(province, "Provincia encontrada con éxito.");
    }

   
    @GetMapping("/by-name")
    public ResponseEntity<ApiResponse<ProvinceResponseDto>> getProvinceByName( @RequestParam String name) {
        
        ProvinceResponseDto province = provinceService.findByName(name.trim());
        return success(province, "Provincia encontrada con éxito.");
    }
}