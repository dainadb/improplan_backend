package io.github.dainadb.improplan.model.service.generic;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz genérica para servicios que utilizan DTOs de entrada y salida.
 *
 * @param <TEntity>      Entidad principal gestionada
 * @param <TRequestDto>  DTO de entrada (creación/actualización)
 * @param <TResponseDto> DTO de salida (respuesta al cliente)
 * @param <ID>           Tipo del identificador primario de la entidad
 */
public interface IGenericCrudDtoService<TEntity, TRequestDto, TResponseDto, ID>  {

    /**
     * Obtiene todos los elementos. 
     * @return Lista de DTOs de respuesta
     */
    List<TResponseDto> findAll();

    /**
     * Busca un elemento por su ID.
     * @param id Identificador del elemento
     * @return DTO de respuesta correspondiente al ID dado
     */
    TResponseDto findById(ID id);

    /**
     * Lee una entidad por su ID.
     * @param id Identificador de la entidad
     * @return Entidad correspondiente al ID dado sin convertir a DTO
     */
    Optional<TEntity> read(ID id);

    /**
     * Crea un nuevo elemento.
     * @param dto DTO de entrada con los datos del nuevo elemento
     * @return DTO de respuesta del elemento creado
     */
    TResponseDto create(TRequestDto dto);
    /**
     * Actualiza un elemento existente.
     * @param id  Identificador del elemento a actualizar
     * @param dto DTO de entrada con los datos actualizados
     * @return DTO de respuesta del elemento actualizado
     */
    TResponseDto update(ID id, TRequestDto dto);

    /**
     * Elimina un elemento por su ID.
     * @param id Identificador del elemento a eliminar
     */
    void delete(ID id);


}
