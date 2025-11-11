package com.jskang.backend.dto;

import com.jskang.backend.domain.UserM;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter // <-- JSON 바디의 값을 DTO 필드에 자동으로 매핑하기 위해 필요합니다.
public class SaveUserRequest {

    // 1. Postman에서 보낸 id를 받기 위한 필드 추가
    private String id;
    private String email;
    private String phoneNumber;

    public UserM toUser() {
        return UserM.builder()
                // 2. DTO의 id를 Entity의 id로 설정
                .id(this.id)
                .email(this.email)
                .phoneNumber(this.phoneNumber)
                .build();
    }
}