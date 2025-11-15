package com.jskang.backend.availableSports.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SaveAvailableSportsRequestDto {

    private String level;
    private String positionCd;
    private Long sportId;

}
