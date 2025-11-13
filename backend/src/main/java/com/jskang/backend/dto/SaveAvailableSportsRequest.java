package com.jskang.backend.dto;

import com.jskang.backend.domain.AvailableSports;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SaveAvailableSportsRequest {

    private String level;
    private String position_cd;
    private String sportId;
    private String sport_id;
    private String id;

    public AvailableSports toSports(){
        return AvailableSports.builder()
                .level(this.level)
                .positionCd(this.position_cd)
                .build();
    }
}
