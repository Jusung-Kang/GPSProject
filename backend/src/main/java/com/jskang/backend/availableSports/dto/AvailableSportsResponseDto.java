package com.jskang.backend.availableSports.dto;

import com.jskang.backend.domain.AvailableSports;
import lombok.Getter;

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



}
