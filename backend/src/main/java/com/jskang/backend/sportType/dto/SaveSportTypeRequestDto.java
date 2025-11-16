package com.jskang.backend.sportType.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SaveSportTypeRequestDto {

    private Long sportId;
    private String sportNm;

}
