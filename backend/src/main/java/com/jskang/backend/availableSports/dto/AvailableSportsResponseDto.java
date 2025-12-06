package com.jskang.backend.availableSports.dto;

import com.jskang.backend.domain.AvailableSports;
import lombok.Getter;

import java.util.List;

@Getter
public class AvailableSportsResponseDto {

    private final String sportName;
    private final Long positionId;
    private final String level;

    public AvailableSportsResponseDto(AvailableSports availableSports){
        this.sportName = availableSports.getSportType().getSportNm();
        this.positionId = availableSports.getPositionM().getPositionId();
        this.level = availableSports.getLevel();
    }

    // DTO 클래스
    public static List<AvailableSportsResponseDto> from(List<AvailableSports> entities) {
        return entities.stream()
                .map(AvailableSportsResponseDto::new)
                .toList();
    }

}
