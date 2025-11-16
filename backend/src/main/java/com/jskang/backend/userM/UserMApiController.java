package com.jskang.backend.userM;

import com.jskang.backend.domain.UserM;
import com.jskang.backend.userM.dto.SaveUserMRequestDto;
import com.jskang.backend.userM.dto.UserResponseDto;
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

    @PostMapping("users")
    public ResponseEntity<UserResponseDto> createUser(@RequestBody SaveUserMRequestDto requestUser) {

          UserM userM = userMService.createUserM(requestUser);

          UserResponseDto userResponseDto = new UserResponseDto(userM);

          return ResponseEntity.status(HttpStatus.CREATED)
                  .body(userResponseDto);

    }

    @PutMapping("users{id}")
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

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(e.getMessage()); // 예외 메시지(예: "User not found")를 body에 담아 반환
    }

}
