package com.jskang.backend.availableSports;

import com.jskang.backend.availableSports.dto.AvailableSportsResponseDto;
import com.jskang.backend.availableSports.dto.SaveAvailableSportsRequestDto;
import com.jskang.backend.domain.AvailableSports;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "사용자 운동 종목 관리", description = "사용자가 가능한 운동 종목을 등록, 수정, 조회하는 API입니다.") // [1] 그룹 이름표
@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class AvailableSportsApiController {

    private final AvailableSportsService availableSportsService;

    @Operation(summary = "운동 종목 등록", description = "특정 사용자에게 새로운 운동 종목을 등록합니다.") // [2] 기능 설명
    @PostMapping("users/{userId}/sports")
    public ResponseEntity<AvailableSportsResponseDto> create(
            @Parameter(description = "사용자 ID (PK)", required = true, example = "1") // [3] 파라미터 설명
            @PathVariable Long userId,
            @RequestBody SaveAvailableSportsRequestDto requestSport) {

        AvailableSportsResponseDto availableSportsResponseDto = availableSportsService.create(userId, requestSport);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(availableSportsResponseDto);
    }

    @Operation(summary = "운동 종목 수정", description = "사용자의 특정 운동 종목 정보를 수정합니다.")
    @PutMapping("users/{userId}/sports/{sportId}")
    public ResponseEntity<AvailableSportsResponseDto> update(
            @Parameter(description = "사용자 ID (PK)", example = "1") @PathVariable Long userId,
            @Parameter(description = "수정할 운동 종목 ID (PK)", example = "3") @PathVariable Long sportId,
            @RequestBody SaveAvailableSportsRequestDto requestSport) {

        AvailableSports sport = availableSportsService.update(userId, sportId, requestSport);
        AvailableSportsResponseDto availableSportsResponseDto = new AvailableSportsResponseDto(sport);
        return ResponseEntity.ok()
                .body(availableSportsResponseDto);
    }

    @Operation(summary = "전체 운동 종목 조회", description = "등록된 모든 운동 종목 리스트를 조회합니다. (관리자용 혹은 전체 목록)")
    @GetMapping("sports")
    public ResponseEntity<List<AvailableSportsResponseDto>> findAll(){

        List<AvailableSportsResponseDto> responseSports = availableSportsService.findAll();

        return ResponseEntity.status(HttpStatus.OK)
                .body(responseSports);
    }

    @Operation(summary = "단일 운동 종목 조회", description = "특정 사용자의 특정 운동 종목 상세 정보를 조회합니다.")
    @GetMapping("users/{userId}/sports/{sportId}")
    public ResponseEntity<AvailableSportsResponseDto> findById(
            @Parameter(description = "사용자 ID", example = "1") @PathVariable Long userId,
            @Parameter(description = "조회할 운동 종목 ID", example = "3") @PathVariable Long sportId) {

        AvailableSports sport = availableSportsService.findById(userId, sportId);

        return ResponseEntity.ok()
                .body(new AvailableSportsResponseDto(sport));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
    }
}