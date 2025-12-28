package com.jskang.backend.sportType.dto;

import com.jskang.backend.domain.SportType;
import lombok.Getter;
import lombok.Setter;

@Getter
public class SportTypeResponseDto {

    private final String sportNm;

    public SportTypeResponseDto(SportType sportType) {

        this.sportNm = sportType.getSportNm();
    }

}
