package com.jskang.backend.availableSports.dto;

import com.jskang.backend.domain.AvailableSports;
import lombok.Getter;

@Getter
public class AvailableSportsResponseDto {

    private final String sportName;
    private final String position;
    private final String level;

    public AvailableSportsResponseDto(AvailableSports availableSports){
        this.sportName = availableSports.getSportType().getSportNm();
        this.position = availableSports.getPositionCd();
        this.level = availableSports.getLevel();
    }



}
