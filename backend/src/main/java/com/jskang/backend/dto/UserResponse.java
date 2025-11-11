package com.jskang.backend.dto;

import com.jskang.backend.domain.AvailableSports;
import com.jskang.backend.domain.UserM;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class UserResponse {

    private final String id;
    private final String email;
    private final String phoneNumber;

    private List<AvailableSportResponse> availableSports;

    private int exerciseHistoryCount;

    @Builder
    public UserResponse(UserM userM) {
        this.id = userM.getId();
        this.email = userM.getEmail();
        this.phoneNumber = userM.getPhoneNumber();

        this.availableSports = userM.getAvailableSports().stream()
                .map(AvailableSportResponse::new)
                .collect(Collectors.toList());
        this.exerciseHistoryCount = userM.getExerciseHistory().size();
    }


}
