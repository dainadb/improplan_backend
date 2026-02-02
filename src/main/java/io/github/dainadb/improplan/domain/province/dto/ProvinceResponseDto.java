package io.github.dainadb.improplan.domain.province.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProvinceResponseDto {
    private Integer id;
    private String name;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String autonomousCommunityName;
}
