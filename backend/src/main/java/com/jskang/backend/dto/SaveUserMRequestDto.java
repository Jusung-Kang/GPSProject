package com.jskang.backend.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SaveUserMRequestDto {

    // 1. Postman에서 보낸 id를 받기 위한 필드 추가
    private Long id;
    private String email;
    private String phoneNumber;
    private List<SaveAvailableSportsRequestDto> saveAvailableSportsRequestDto;

}