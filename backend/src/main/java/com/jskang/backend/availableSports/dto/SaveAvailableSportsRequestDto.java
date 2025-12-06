package com.jskang.backend.availableSports.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SaveAvailableSportsRequestDto {

    private String level;
    private Long positionId;
    private String positionNm;
    private Long sportId;

}
