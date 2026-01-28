package io.github.dainadb.improplan.domain.favorite.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de salida con la información de un evento marcado como favorito por un usuario.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FavoriteResponseDto {

    private Long id;
    private LocalDateTime favoriteDate;
    private String userEmail;
    private Long eventId;
    private String eventName;
    private String eventImage;
    private Double eventPrice;
    private String eventThemeName;
    private String eventMunicipalityName;
    private Boolean eventInTime;
    private String eventStatus;
}
