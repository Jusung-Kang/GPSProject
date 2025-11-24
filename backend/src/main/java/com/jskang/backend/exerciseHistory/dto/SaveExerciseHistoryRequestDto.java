package com.jskang.backend.exerciseHistory.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class SaveExerciseHistoryRequestDto {

    private Integer seq;
    private LocalDateTime dt;
    private Long sportId;
    private Long userId;

}
