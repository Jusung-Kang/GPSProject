package com.jskang.backend.controller;

import com.jskang.backend.domain.AvailableSports;
import com.jskang.backend.domain.UserM;
import com.jskang.backend.dto.AvailableSportsResponseDto;
import com.jskang.backend.dto.SaveAvailableSportsRequestDto;
import com.jskang.backend.dto.SaveUserMRequestDto;
import com.jskang.backend.dto.UserResponseDto;
import com.jskang.backend.service.UserMService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class UserMApiController {

    private final UserMService userMService;

    @PostMapping("user")
    public ResponseEntity<UserResponseDto> createUser(@RequestBody SaveUserMRequestDto requestUser) {

          UserM userM = userMService.createUserM(requestUser);

          UserResponseDto userResponseDto = new UserResponseDto(userM);

          return ResponseEntity.status(HttpStatus.CREATED)
                  .body(userResponseDto);

    }

    @PutMapping("user{id}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable Long id, @RequestBody SaveUserMRequestDto requestUser) {
        UserM userM = userMService.updateUserM(id, requestUser);
        return ResponseEntity.ok(new UserResponseDto(userM));
    }

    @GetMapping("users")
    public ResponseEntity<List<UserResponseDto>> getAllUserM() {
        List<UserResponseDto> getUsersAll = userMService.findAll()
                .stream()
                .map(UserResponseDto::new)
                .toList();

        return ResponseEntity.ok()
                .body(getUsersAll);
    }

    @GetMapping("users/{id}")
    public ResponseEntity<UserResponseDto> getUserMById(@PathVariable Long id) {
        UserM userM = userMService.findById(id);

        return ResponseEntity.ok()
                .body(new UserResponseDto(userM));
    }

    @PostMapping("user/{sportId}/sports")
    public ResponseEntity<AvailableSportsResponseDto> createSport(@PathVariable Long sportId, @RequestBody SaveAvailableSportsRequestDto requestSport) {

        AvailableSports sport = userMService.createAvailableSports(sportId, requestSport);

        AvailableSportsResponseDto availableSportsResponseDto = new AvailableSportsResponseDto(sport);

        return ResponseEntity.status(HttpStatus.CREATED)
                .build();

    }

    @PutMapping("user/{userI}/sports/{sportId}")
    public ResponseEntity<AvailableSportsResponseDto> updateSport(@PathVariable Long userId, @PathVariable Long sportId, @RequestBody SaveAvailableSportsRequestDto requestSport) {
        AvailableSports sport = userMService.updateAvailableSports(userId, sportId, requestSport);
        AvailableSportsResponseDto availableSportsResponseDto = new AvailableSportsResponseDto(sport);
        return ResponseEntity.ok()
                .build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(e.getMessage()); // 예외 메시지(예: "User not found")를 body에 담아 반환
    }

}
