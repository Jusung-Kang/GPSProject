package com.jskang.backend.controller;

import com.jskang.backend.domain.UserM;
import com.jskang.backend.dto.SaveUserRequest;
import com.jskang.backend.dto.UserResponse;
import com.jskang.backend.service.UserMService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class UserMApiController {

    private final UserMService userMService;

    @PostMapping("api/users")
    public ResponseEntity<UserM> saveUserM(@RequestBody SaveUserRequest saveUserRequest) {
        UserM saved = userMService.save(saveUserRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(saved);
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

    @GetMapping("api/users/{id}")
    public ResponseEntity<UserResponse> getUserMById(@PathVariable String id) {
        UserM userM = userMService.findById(id);

        if (userM == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .body(new UserResponse(userM));
    }


}
