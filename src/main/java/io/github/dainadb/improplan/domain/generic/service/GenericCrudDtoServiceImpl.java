package io.github.dainadb.improplan.domain.generic.service;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import io.github.dainadb.improplan.exception.NotFoundException;

public abstract class GenericCrudDtoServiceImpl<TEntity, TRequestDto, TResponseDto, ID> 
                implements IGenericCrudDtoService<TEntity, TRequestDto, TResponseDto, ID> {


    @Autowired
    protected ModelMapper modelMapper;

    //MÉTODOS ABSTRACTOS que las clases hijas van a tener que implementar cuando hereden de esta clase genérica.

    /**
     * Devuelve el repositorio correspondiente a la entidad.
     *
     * @return Repositorio JPA
     */
    protected abstract JpaRepository<TEntity, ID> getRepository();

    /**
     * Devuelve la clase de la entidad.
     *
     * @return Clase de la entidad
     */
    protected abstract Class<TEntity> getEntityClass();

    /**
     * Devuelve la clase del DTO de entrada.
     *
     * @return Clase del DTO request
     */
    protected abstract Class<TRequestDto> getRequestDtoClass();

    /**
     * Devuelve la clase del DTO de salida.
     *
     * @return Clase del DTO response
     */
    protected abstract Class<TResponseDto> getResponseDtoClass();


  //MÉTODOS ADICIONALES PARA CONVERSIÓN Y VALIDACIÓN ( que las clases hijas podrán sobrescribir si fuese necesario)


  /**
     * Convierte una entidad a un ResponseDto
     * @param entity Entidad  
     * @return Dto convertido
     */
    protected TResponseDto convertToResponseDto (TEntity entity){
        return modelMapper.map(entity, getResponseDtoClass());
    }

    /**
     * Convierte un DTO de entrada a una entidad.
     * @param dto DTO de entrada
     * @return Entidad convertida
     */
    protected TEntity convertToEntity(TRequestDto dto) {
        return modelMapper.map(dto, getEntityClass()); 
    }

    /**
     * Para actualización parcial de una entidad existente con los datos de un DTO de entrada.
     * @param entity  Entidad existente a actualizar
     * @param dto DTO de entrada
     */
     protected void updateEntityFromDto(TEntity entity, TRequestDto dto) {
        modelMapper.map(dto, entity); //Modifica una entidad que ya existe con los datos del dto.
    }

    
    /**
     * Valida los datos antes de crear una nueva entidad.
     * @param dto   DTO de entrada
     */
    protected void validateCreate(TRequestDto dto) {
        // Vacío por defecto - las subclases pueden sobrescribir
    }
    /**
     * Valida los datos antes de actualizar una entidad existente.
     * @param id  Identificador de la entidad a actualizar
     * @param dto DTO de entrada
     */
    protected void validateUpdate(ID id, TRequestDto dto) {
        // Vacío por defecto - las subclases pueden sobrescribir
    }



    // IMPLEMENTACIÓN de los métodos de la interfaz genérica


    /**
     *  {@inheritDoc}
     */
    @Override
    public List<TResponseDto> findAll() {
        return getRepository().findAll().stream()
                .map(this::convertToResponseDto) // Toda entidad que se obtenga se convierte a DTO
                .toList();
    }
    /**
     * {@inheritDoc}
     */

    @Override
    public TResponseDto findById(ID id) {
        TEntity entity = getRepository().findById(id)
                .orElseThrow(() -> new NotFoundException("Entidad no encontrada con el id: " + id));
        return convertToResponseDto(entity);
    }

    /**
     * {@inheritDoc}
     */
    //Devuelve un optional para que el usuario decida qué hacer si no existe la entidad.
    @Override
    public Optional<TEntity> read(ID id) {
        return getRepository().findById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TResponseDto create(TRequestDto dto) {
        //Validación previa personalizada
        validateCreate(dto);
        // Conversión de DTO a entidad y guardado
        TEntity entity = convertToEntity(dto); 
        entity = getRepository().save(entity);
        return convertToResponseDto(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TResponseDto update(ID id, TRequestDto dto) {
        // Validaciones personalizadas 
        validateUpdate(id, dto);

        // Se obtiene la entidad 
        TEntity existingEntity = getRepository().findById(id)
                .orElseThrow(() -> new NotFoundException("Entidad no encontrada con el id: " + id));
        
        //Se actualiza con los datos del DTO
        updateEntityFromDto(existingEntity, dto); 
        
        TEntity updatedEntity = getRepository().save(existingEntity); //Se guardan los cambios enal BBDD
        
        return convertToResponseDto(updatedEntity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(ID id) {
        if (!getRepository().existsById(id)) {
                    throw new NotFoundException("Entidad no encontrada con ID: " + id);
                }

        getRepository().deleteById(id);
    }

  
}
