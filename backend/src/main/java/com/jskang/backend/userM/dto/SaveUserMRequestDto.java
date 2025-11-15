package com.jskang.backend.userM.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SaveUserMRequestDto {

    private String email;
    private String phoneNumber;

}