package com.jskang.backend.userM;

import com.jskang.backend.userM.dto.SaveUserMRequestDto;
import com.jskang.backend.userM.dto.UserMResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "사용자(User) 관리", description = "사용자 등록, 수정, 조회 관련 API입니다.") // [1. 컨트롤러 전체 이름표]
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class UserMApiController {

    private final UserMService userMService;

    @Operation(summary = "사용자 등록", description = "새로운 사용자를 생성합니다.") // [2. 각 기능 설명]
    @PostMapping("users")
    public ResponseEntity<UserMResponseDto> create(@RequestBody SaveUserMRequestDto requestUser) {
        UserMResponseDto response = userMService.create(requestUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "사용자 정보 수정", description = "ID를 통해 기존 사용자의 정보를 수정합니다.")
    @PutMapping("users/{id}")
    public ResponseEntity<UserMResponseDto> update(@PathVariable Long id, @RequestBody SaveUserMRequestDto requestUser) {
        UserMResponseDto userM = userMService.update(id, requestUser);
        return ResponseEntity.ok(userM);
    }

    @Operation(summary = "전체 사용자 조회", description = "등록된 모든 사용자의 목록을 조회합니다.")
    @GetMapping("users")
    public ResponseEntity<List<UserMResponseDto>> findAll() {
        List<UserMResponseDto> findAll =  userMService.findAll();
        return ResponseEntity.ok().body(findAll);
    }

    @Operation(summary = "단일 사용자 조회", description = "ID(PK)를 이용해 특정 사용자를 조회합니다.")
    @GetMapping("users/{id}")
    public ResponseEntity<UserMResponseDto> findById(@PathVariable Long id) {
        UserMResponseDto response = userMService.findById(id);
        return ResponseEntity.ok().body(response);
    }

    // 예외 처리는 Swagger에 자동으로 명시되지는 않지만, 기능상 문제 없습니다.
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
    }
}