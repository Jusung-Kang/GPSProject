package com.jskang.backend.exerciseHistory;

import com.jskang.backend.exerciseHistory.dto.ExerciseHistoryResponseDto;
import com.jskang.backend.exerciseHistory.dto.SaveExerciseHistoryRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class ExerciseHistoryApiController {

    private final ExerciseHistoryService exerciseHistoryService;

    @PostMapping("users/{userId}/history")
    public ResponseEntity<ExerciseHistoryResponseDto> create(@PathVariable Long userId, @RequestBody SaveExerciseHistoryRequestDto requestSport) {

        ExerciseHistoryResponseDto exerciseHistoryResponseDto = exerciseHistoryService.create(userId, requestSport);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(exerciseHistoryResponseDto);

    }

    @PutMapping("history/{historyId}")
    public ResponseEntity<ExerciseHistoryResponseDto> update(@PathVariable Long historyId, @RequestBody SaveExerciseHistoryRequestDto request) {

        ExerciseHistoryResponseDto response = exerciseHistoryService.update(historyId, request);
        return ResponseEntity.ok()
                .body(response);

    }

    @GetMapping("history")
    public ResponseEntity<List<ExerciseHistoryResponseDto>> findAll(){

        List<ExerciseHistoryResponseDto> response = exerciseHistoryService.findAll();

        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping("history/{historyId}")
    public ResponseEntity<ExerciseHistoryResponseDto> findById(@PathVariable Long historyId) {

        ExerciseHistoryResponseDto history = exerciseHistoryService.findById(historyId);

        return ResponseEntity.ok()
                .body(history);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
    }
}
