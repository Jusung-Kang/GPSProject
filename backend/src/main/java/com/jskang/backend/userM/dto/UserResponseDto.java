package com.jskang.backend.userM.dto;

import com.jskang.backend.domain.UserM;
import com.jskang.backend.availableSports.dto.AvailableSportsResponseDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class UserResponseDto {

    private final Long id;
    private final String email;
    private final String phoneNumber;
    private final List<AvailableSportsResponseDto> availableSports;
    private final int exerciseHistoryCount;

    @Builder
    public UserResponseDto(UserM userM) {
        this.id = userM.getId();
        this.email = userM.getEmail();
        this.phoneNumber = userM.getPhoneNumber();

        this.availableSports = userM.getAvailableSports().stream()
                .map(AvailableSportsResponseDto::new)
                .collect(Collectors.toList());
        this.exerciseHistoryCount = userM.getExerciseHistory().size();
    }


}
