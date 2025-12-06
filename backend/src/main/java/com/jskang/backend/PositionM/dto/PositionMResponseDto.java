package com.jskang.backend.PositionM.dto;

import com.jskang.backend.domain.PositionM;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PositionMResponseDto {

    private String positionNm;

    public PositionMResponseDto(PositionM entity) {
        this.positionNm = entity.getPositionNm();
    }

    public static List<PositionMResponseDto> from(List<PositionM> entities) {
        return entities.stream()
                .map((PositionMResponseDto::new))
                .toList();
    }

}
