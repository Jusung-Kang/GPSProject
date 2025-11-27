package com.jskang.backend.availableSports;

import com.jskang.backend.availableSports.dto.AvailableSportsResponseDto;
import com.jskang.backend.availableSports.dto.SaveAvailableSportsRequestDto;
import com.jskang.backend.domain.AvailableSports;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class AvailableSportsApiController {

    private final AvailableSportsService availableSportsService;


    @PostMapping("users/{userId}/sports")
    public ResponseEntity<AvailableSportsResponseDto> create(@PathVariable Long userId, @RequestBody SaveAvailableSportsRequestDto requestSport) {

        AvailableSportsResponseDto availableSportsResponseDto = availableSportsService.create(userId, requestSport);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(availableSportsResponseDto);

    }

    @PutMapping("users/{userId}/sports/{sportId}")
    public ResponseEntity<AvailableSportsResponseDto> update(@PathVariable Long userId, @PathVariable Long sportId, @RequestBody SaveAvailableSportsRequestDto requestSport) {

        AvailableSports sport = availableSportsService.update(userId, sportId, requestSport);
        AvailableSportsResponseDto availableSportsResponseDto = new AvailableSportsResponseDto(sport);
        return ResponseEntity.ok()
                .body(availableSportsResponseDto);

    }

    @GetMapping("sports")
    public ResponseEntity<List<AvailableSportsResponseDto>> findAll(){

        List<AvailableSportsResponseDto> responseSports = availableSportsService.findAll();

        return ResponseEntity.status(HttpStatus.OK)
                .body(responseSports);
    }

    @GetMapping("users/{userId}/sports/{sportId}")
    public ResponseEntity<AvailableSportsResponseDto> findById(@PathVariable Long userId, @PathVariable Long sportId) {

        AvailableSports sport = availableSportsService.findById(userId, sportId);

        return ResponseEntity.ok()
                .body(new AvailableSportsResponseDto(sport));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        // 서비스에서 던진 예외 메시지(예: "해당 유저를 찾을수 없습니다.")와
        // 404 상태코드를 반환합니다.
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
    }

}
