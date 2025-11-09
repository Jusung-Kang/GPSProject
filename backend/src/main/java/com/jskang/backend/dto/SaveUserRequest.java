package com.jskang.backend.dto;

import com.jskang.backend.domain.UserM;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class SaveUserRequest {

    private String userId;
    private String email;
    private String phoneNumber;

    public UserM toUser(){
        return UserM.builder()
                .id(userId)
                .email(email)
                .phoneNumber(phoneNumber)
                .build();
    }


}
