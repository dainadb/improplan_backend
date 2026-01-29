package io.github.dainadb.improplan.domain.autonomouscommunity.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import io.github.dainadb.improplan.domain.autonomouscommunity.dto.AutCommunityRequestDto;
import io.github.dainadb.improplan.domain.autonomouscommunity.dto.AutCommunityResponseDto;
import io.github.dainadb.improplan.domain.autonomouscommunity.entity.AutonomousCommunity;
import io.github.dainadb.improplan.domain.autonomouscommunity.repository.IAutonomousCommunityRepository;
import io.github.dainadb.improplan.domain.generic.service.GenericCrudDtoServiceImpl;

public class AutonomousCommunityServiceImpl extends GenericCrudDtoServiceImpl<AutonomousCommunity, AutCommunityRequestDto, AutCommunityResponseDto, Integer> implements IAutonomousCommunityService {

    @Autowired
    private IAutonomousCommunityRepository autCommunityRepository;
    

    //OVERRIDE MÉTODOS GENÉRICOS DE GENERICCRUDDTOSERVICEIMPL

    @Override
    protected Class<AutonomousCommunity> getEntityClass() {
        return AutonomousCommunity.class;
    }

    @Override
    protected JpaRepository<AutonomousCommunity, Integer> getRepository() {
        return autCommunityRepository;
    }

    @Override
    protected Class<AutCommunityRequestDto> getRequestDtoClass() {
        return AutCommunityRequestDto.class;
    }

    @Override
    protected Class<AutCommunityResponseDto> getResponseDtoClass() {
        return AutCommunityResponseDto.class;
    }


      //OVERRIDE MÉTODOS DE LA INTERFAZ IAUTONOMOUSCOMMUNITYSERVICE

      /**
       * {@inheritDoc}
       */
    @Override
    public AutCommunityResponseDto findByName(String name) {
        AutonomousCommunity autCommunityOpt = autCommunityRepository.findByNameIgnoreCase(name)
                .orElseThrow(() -> new RuntimeException("Comunidad autónoma no encontrada con el nombre: " + name));

        return convertToResponseDto(autCommunityOpt); // Reutiliza el conversor heredado
    }


    //Nota: no se implementan los métodos validateCreate o convertToEntity (por ejemplo) porque que estas entidades se crearán en la BBDD de forma previa y no se crearán nuevas desde la aplicación.
    //Posible implementación futura
}