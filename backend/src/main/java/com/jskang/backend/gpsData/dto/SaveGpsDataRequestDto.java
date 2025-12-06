package com.jskang.backend.gpsData.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class SaveGpsDataRequestDto {

    private BigDecimal longitude;
    private BigDecimal latitude;
    private BigDecimal altitude;
    private BigDecimal distance;
    private BigDecimal speed;
    private Integer heartRate;
    private Integer gpsSeq;

}
