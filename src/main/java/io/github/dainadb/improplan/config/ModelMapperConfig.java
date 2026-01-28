package io.github.dainadb.improplan.config;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.dainadb.improplan.domain.event.dto.EventRequestDto;
import io.github.dainadb.improplan.domain.event.dto.EventResponseDto;
import io.github.dainadb.improplan.domain.event.entity.Event;
import io.github.dainadb.improplan.domain.eventdate.entity.EventDate;
import io.github.dainadb.improplan.domain.favorite.dto.FavoriteResponseDto;
import io.github.dainadb.improplan.domain.favorite.entity.Favorite;
import io.github.dainadb.improplan.domain.municipality.dto.MunicipalityResponseDto;
import io.github.dainadb.improplan.domain.municipality.entity.Municipality;
import io.github.dainadb.improplan.domain.province.dto.ProvinceResponseDto;
import io.github.dainadb.improplan.domain.province.entity.Province;
import io.github.dainadb.improplan.domain.role.entity.Role;
import io.github.dainadb.improplan.domain.user.dto.UserRequestDto;
import io.github.dainadb.improplan.domain.user.dto.UserResponseDto;
import io.github.dainadb.improplan.domain.user.entity.User;

/**
 * Configuración global de ModelMapper para personalizar el mapeo entre
 * entidades y DTOs.
 */
@Configuration
public class ModelMapperConfig {

 @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
       
       //MAPEOS PERSONALIZADOS:

        /**
         * Mapeo personalizado de Province a ProvinceResponseDto
         */
        modelMapper.typeMap(Province.class, ProvinceResponseDto.class)
               .addMappings(new PropertyMap<Province, ProvinceResponseDto>() {
                   @Override
                   protected void configure() {
                       // Mapear el nombre de la comunidad autónoma desde la entidad relacionada
                       map().setAutonomousCommunityName(source.getAutonomousCommunity().getName());
                   }
               });
    
    
        /**
         * Mapeo personalizado de Municipality a MunicipalityResponseDto
         */
        modelMapper.typeMap(Municipality.class, MunicipalityResponseDto.class)
               .addMappings(new PropertyMap<Municipality,MunicipalityResponseDto>() {
                    @Override
                    protected void configure(){
                        map().setProvinceName(source.getProvince().getName());
                    }

               });

        /**
         * Mapeo personalizado de Favorite a FavoriteResponseDto
         */
        modelMapper.typeMap(Favorite.class, FavoriteResponseDto.class)
               .addMappings(new PropertyMap<Favorite,FavoriteResponseDto>() {
                    @Override
                    protected void configure(){
                        map().setEventId(source.getEvent().getId());
                        map().setEventName(source.getEvent().getName());
                        map().setEventImage(source.getEvent().getImage());
                        map().setEventPrice(source.getEvent().getPrice());
                        map().setEventThemeName(source.getEvent().getTheme().getName());
                        map().setEventMunicipalityName(source.getEvent().getMunicipality().getName());
                        map().setEventInTime(source.getEvent().getInTime());
                        map().setEventStatus(source.getEvent().getStatus().name());
                        map().setUserEmail(source.getUser().getEmail());

                    }
               });
        

        /**
        * Conversor para mapear Set<Role> a Set<String> (nombre del rol)
        */

       //El converter es el método que convierte la colección de un tipo a otro. Es necesario porque el tipo de dato es incompatible: ModelMapper no sabe cómo convertir automáticamente una colección de objetos tipo Role a una colección de Strings (nombres de roles).
        Converter<Set<Role>, Set<String>> roleConverter = ctx -> ctx.getSource().stream() // getSource() devuelve la lista/set de tipo Role. Entonces se hace un stream de esa lista.
                .map(role -> role.getName().name()) //De cada objeto Role se obtiene el nombre del rol (que es un enum), y se convierte a String con name().
                .collect(Collectors.toSet()); // Finalmente se recoge todo en un Set<String>.

        /**
         * Mapeo personalizado de User a UserResponseDto
         */

        // Tras eso indicamos a ModelMapper que use este conversor al mapear User a UserResponseDto:
        modelMapper.typeMap(User.class, UserResponseDto.class)
                .addMappings(new PropertyMap<User, UserResponseDto>() {
                    @Override
                    protected void configure() {
                        using(roleConverter).map(source.getRoles(), destination.getRoles()); //Con using indicamos que use el conversor definido antes para mapear los roles.
                        // con .map indicamos de dónde a dónde mapear: de source.getRoles() (que representa el conjunto de roles de la entidad User) a destination.getRoles() (que representa el conjunto de nombres de roles en UserResponseDto).
                    }
                });
       
       
        /**
        * Conversor para mapear Set<EventDate> a Set<LocalDate> (fecha del evento)
        */

        Converter<Set<EventDate>, Set<LocalDate>> eventDateConverter = ctx -> ctx.getSource().stream() 
                .map(EventDate::getFullDate) //Method reference: de cada objeto EventDate se obtiene la fecha completa (LocalDate) usando el método getFullDate().
                .collect(Collectors.toSet());

        /**
         * Mapeo personalizado de Event a EventResponseDto
         */
        modelMapper.typeMap(Event.class, EventResponseDto.class)
               .addMappings(new PropertyMap<Event,EventResponseDto>() {
                    @Override
                    protected void configure(){
                    
                    map(source.getMunicipality().getName(), destination.getMunicipalityName());
                    map(source.getTheme().getName(), destination.getThemeName());

                    // Dates → eventDates (usando converter)
                    using(eventDateConverter)
                        .map(source.getDates(), destination.getEventDates());
                    }
        });


        //MAPEOS INVERSOS PERSONALIZADOS (para create y update):
        /**
         * Mapeo personalizado de EventRequestDto a Event
         * Se ignoran los campos que requieren lógica de negocio específica en el servicio,
         * como la asignación de entidades relacionadas.
         */
        modelMapper.typeMap(EventRequestDto.class, Event.class)
                .addMappings(new PropertyMap<EventRequestDto, Event>() {
                    @Override
                    protected void configure() {
                        // Skip indica al mapper que debe ignorar estos campos al mapear, ya que se gestionan por separado en el servicio (por ejemplo, asignando la entidad Municipality correcta según el ID).
                        skip(destination.getMunicipality());
                        skip(destination.getTheme());
                        skip(destination.getUser());
                        skip(destination.getDates());
                    }
        });
        

         /**
         * Mapeo personalizado de UserRequestDto a User.
         * Se ignoran los campos que requieren lógica de negocio específica en el servicio,
         * como la codificación de la contraseña y la asignación de roles.
         */
        modelMapper.typeMap(UserRequestDto.class, User.class)
                .addMappings(new PropertyMap<UserRequestDto, User>() {
                    @Override
                    protected void configure() {
                        skip(destination.getRoles());
                        skip(destination.getPassword());
                    }
        });
       
    
        //No se incluyen mapeos personalizados para Province, Municipality,etc inversos porque no se crean/actualizan desde DTOs en la aplicación.
       
       
        return modelMapper;
    }
}
