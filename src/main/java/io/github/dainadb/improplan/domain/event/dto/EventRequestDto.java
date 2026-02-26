package io.github.dainadb.improplan.domain.event.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import io.github.dainadb.improplan.domain.event.entity.Event;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO que representa los datos requeridos para crear o actualizar un evento por un usuario.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventRequestDto {
    private String name;
    private String summary;
    private String description;
    private String placeName;
    private String address;
    private BigDecimal latitude; //Estos datos tienen un valor por defecto de 0.0 porque el usuario (role-user) no introduce latitud ni longitud cuando crea la solicitud del evento. Estos datos los introducirá el administrador manualmente cuando publique el evento.
    private BigDecimal longitude; //Estos datos tienen un valor por defecto de 0.0 porque el usuario (role-user) no introduce latitud ni longitud cuando crea la solicitud del evento. Estos datos los introducirá el administrador manualmente cuando publique el evento.
    private String image;
    private String infoUrl;
    private Boolean isFree;
    private BigDecimal price;
    //private Boolean inTime; //Es un campo que por defecto será true ya que cuando se cree un evento las fechas serán futuras y lo modificará un método programado (scheduler).
    private Event.StatusType status; // Es un campo que por defecto será PENDING cuando se cree un evento. El administrador podrá cambiarlo a PUBLISHED o DISCARDED.
    private String municipalityName;
    private String themeName;
    private Set<LocalDate> eventDates;
}
