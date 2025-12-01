package com.jskang.backend.gpsData.dto;

import com.jskang.backend.domain.GpsData;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
public class GpsDataResponseDto {

    private BigDecimal longitude;
    private BigDecimal latitude;
    private BigDecimal distance;
    private BigDecimal max_distance;
    private BigDecimal speed;
    private BigDecimal maxSpeed;
    private Integer heartRate;
    private int gpsSeq;

    public GpsDataResponseDto(GpsData gpsData) {
        this.longitude = gpsData.getLongitude();
        this.latitude = gpsData.getLatitude();
        this.distance = gpsData.getDistance();
        this.max_distance = gpsData.getMaxDistance();
        this.speed = gpsData.getSpeed();
        this.maxSpeed = gpsData.getMaxSpeed();
        this.heartRate = gpsData.getHeartRate();
        this.gpsSeq = gpsData.getGpsSeq();
    }

    public static List<GpsDataResponseDto> from(List<GpsData> entities) {
        return entities.stream()
                .map(GpsDataResponseDto::new)
                .toList();
    }


}
