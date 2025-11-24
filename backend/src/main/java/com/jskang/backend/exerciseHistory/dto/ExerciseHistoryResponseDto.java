package com.jskang.backend.exerciseHistory.dto;

import com.jskang.backend.domain.ExerciseHistory;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ExerciseHistoryResponseDto {

    private Long historyId;
    private String sportNm;
    private Integer seq;
    private LocalDateTime dt;

    public ExerciseHistoryResponseDto(ExerciseHistory entity) {
        this.historyId = entity.getHistoryId();
        this.seq = entity.getSeq();
        this.dt = entity.getDt();
        // 연관된 SportType에서 이름을 꺼내옴 (NPE 방지 로직은 상황에 따라 추가)
        this.sportNm = entity.getSportType() != null ? entity.getSportType().getSportNm() : "";
    }
}
