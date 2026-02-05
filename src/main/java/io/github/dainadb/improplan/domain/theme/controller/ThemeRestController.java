package io.github.dainadb.improplan.domain.theme.controller;
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
import io.github.dainadb.improplan.domain.theme.dto.ThemeResponseDto;
import io.github.dainadb.improplan.domain.theme.service.IThemeService;


/**
 * Controlador REST para la gestión de Temáticas (Themes).
 * Expone los endpoints para realizar operaciones CRUD sobre las temáticas.
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/themes")
public class ThemeRestController extends GenericRestController {

    @Autowired
    private IThemeService themeService;

    /**
     * Obtiene una lista de todas las temáticas.
     * @return ResponseEntity con la lista de temáticas y un mensaje de éxito.
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<ThemeResponseDto>>> getAllThemes() {
        List<ThemeResponseDto> themes = themeService.findAll();
        return success(themes, "Temáticas obtenidas correctamente.");
    }

    /**
     * Obtiene una temática por su ID.
     * @param id El ID de la temática a buscar.
     * @return ResponseEntity con la temática encontrada.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ThemeResponseDto>> getThemeById(@PathVariable Integer id) {
        ThemeResponseDto theme = themeService.findById(id);
        return success(theme, "Temática encontrada con éxito.");
    }

    /**
     * Obtiene una temática por su nombre.
     * @param name El nombre de la temática a buscar.
     * @return ResponseEntity con la temática encontrada.
     */
    @GetMapping("/by-name")
    public ResponseEntity<ApiResponse<ThemeResponseDto>> getThemeByName(@RequestParam String name) {
        ThemeResponseDto theme = themeService.findByName(name.trim());
        return success(theme, "Temática encontrada con éxito.");
    }


}