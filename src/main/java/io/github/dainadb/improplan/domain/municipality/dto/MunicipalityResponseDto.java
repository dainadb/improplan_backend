package io.github.dainadb.improplan.domain.municipality.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de salida con la información de un municipio.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MunicipalityResponseDto {

    private Integer id;
    private String name;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String provinceName;
}
