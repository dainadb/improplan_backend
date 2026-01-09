package io.github.dainadb.improplan.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración global de ModelMapper para personalizar el mapeo entre
 * entidades y DTOs.
 */
@Configuration
public class ModelMapperConfig {

 @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
       return modelMapper;
    }
}
