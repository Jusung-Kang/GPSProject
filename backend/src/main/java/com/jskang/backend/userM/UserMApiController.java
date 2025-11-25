package com.jskang.backend.userM;

import com.jskang.backend.domain.UserM;
import com.jskang.backend.userM.dto.SaveUserMRequestDto;
import com.jskang.backend.userM.dto.UserMResponseDto;
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
    public ResponseEntity<UserMResponseDto> create(@RequestBody SaveUserMRequestDto requestUser) {

          UserMResponseDto response = userMService.create(requestUser);

          return ResponseEntity.status(HttpStatus.CREATED)
                  .body(response);

    }

    @PutMapping("users/{id}")
    public ResponseEntity<UserMResponseDto> update(@PathVariable Long id, @RequestBody SaveUserMRequestDto requestUser) {

        UserMResponseDto userM = userMService.update(id, requestUser);

        return ResponseEntity.ok(userM);

    }

    @GetMapping("users")
    public ResponseEntity<List<UserMResponseDto>> findAll() {
        List<UserMResponseDto> findAll =  userMService.findAll();

        return ResponseEntity.ok()
                .body(findAll);
    }

    @GetMapping("users/{id}")
    public ResponseEntity<UserMResponseDto> findById(@PathVariable Long id) {


        UserMResponseDto response = userMService.findById(id);

        return ResponseEntity.ok()
                .body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(e.getMessage()); // 예외 메시지(예: "User not found")를 body에 담아 반환
    }

}
