package com.jskang.backend.userM.dto;

import com.jskang.backend.domain.UserM;
import com.jskang.backend.availableSports.dto.AvailableSportsResponseDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class UserMResponseDto {

    private final Long userId;
    private final String email;
    private final String phoneNumber;
    private final List<AvailableSportsResponseDto> availableSports;
    private final int exerciseHistoryCount;

    @Builder
    public UserMResponseDto(UserM userM) {
        this.userId = userM.getUserId();
        this.email = userM.getEmail();
        this.phoneNumber = userM.getPhoneNumber();

        this.availableSports = userM.getAvailableSports().stream()
                .map(AvailableSportsResponseDto::new)
                .collect(Collectors.toList());
        this.exerciseHistoryCount = userM.getExerciseHistory().size();
    }

    public static List<UserMResponseDto> from(List<UserM> entities) {
        return entities.stream()
                .map(UserMResponseDto::new)
                .toList();
    }

}
