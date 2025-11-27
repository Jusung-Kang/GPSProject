package com.jskang.backend.availableSports.dto;

import com.jskang.backend.domain.AvailableSports;
import com.jskang.backend.domain.ExerciseHistory;
import com.jskang.backend.exerciseHistory.dto.ExerciseHistoryResponseDto;
import lombok.Getter;

import java.util.List;

@Getter
public class AvailableSportsResponseDto {

    private final String sportName;
    private final String positionCd;
    private final String level;

    public AvailableSportsResponseDto(AvailableSports availableSports){
        this.sportName = availableSports.getSportType().getSportNm();
        this.positionCd = availableSports.getPositionCd();
        this.level = availableSports.getLevel();
    }

    // DTO 클래스
    public static List<AvailableSportsResponseDto> from(List<AvailableSports> entities) {
        return entities.stream()
                .map(AvailableSportsResponseDto::new)
                .toList();
    }

}
