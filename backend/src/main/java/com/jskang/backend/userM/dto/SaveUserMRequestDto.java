package com.jskang.backend.userM.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SaveUserMRequestDto {

    private String nm;
    private String email;
    private String password;
    private String phoneNumber;

}