package com.jskang.backend.dto;

import com.jskang.backend.domain.AvailableSports;
import lombok.Getter;

@Getter
public class AvailableSportsResponse {

    private final String sportName;
    private final String position;
    private final String level;

    public AvailableSportsResponse(AvailableSports availableSports){
        this.sportName = availableSports.getSportType().getSportNm();
        this.position = availableSports.getPositionCd();
        this.level = availableSports.getLevel();
    }



}
