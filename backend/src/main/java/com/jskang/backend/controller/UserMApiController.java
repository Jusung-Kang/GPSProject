package com.jskang.backend.controller;

import com.jskang.backend.domain.UserM;
import com.jskang.backend.dto.SaveUserRequest;
import com.jskang.backend.dto.UserResponse;
import com.jskang.backend.service.UserMService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class UserMApiController {

    private final UserMService userMService;

    @PostMapping("api/users")
    public ResponseEntity<UserM> saveUserM(@RequestBody SaveUserRequest saveUserRequest) {
        UserM saveUser = userMService.save(saveUserRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(saveUser);
    }

    @GetMapping("api/users")
    public ResponseEntity<List<UserResponse>> getAllUserM() {
        List<UserResponse> getUsersAll = userMService.findAll()
                .stream()
                .map(UserResponse::new)
                .toList();

        return ResponseEntity.ok()
                .body(getUsersAll);
    }


}
