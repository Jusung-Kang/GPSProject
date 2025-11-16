package com.jskang.backend.sportType;

import com.jskang.backend.domain.SportType;
import com.jskang.backend.sportType.dto.SaveSportTypeRequestDto;
import com.jskang.backend.sportType.dto.SportTypeResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class SportTypeApiController {

    private final SportTypeService sportTypeService;

    @PostMapping("sportTypes")
    public ResponseEntity<SportTypeResponseDto> createSportType(@RequestBody SaveSportTypeRequestDto requestSportType) {

        SportType sportType = sportTypeService.createSportType(requestSportType);

        SportTypeResponseDto sportTypeResponseDto = new SportTypeResponseDto(sportType);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(sportTypeResponseDto);

    }

    @PutMapping("sportTypes/{sportId}")
    public ResponseEntity<SportTypeResponseDto> updateSportType(@PathVariable Long sportId, @RequestBody SaveSportTypeRequestDto requestSportType) {

        SportType sportType = sportTypeService.updateSportType(sportId, requestSportType);
        SportTypeResponseDto sportTypeResponseDto = new SportTypeResponseDto(sportType);
        return ResponseEntity.ok()
                .body(sportTypeResponseDto);

    }

    @GetMapping("sportTypes")
    public ResponseEntity<List<SportTypeResponseDto>> getSportTypeAll() {

        List<SportTypeResponseDto> responseSportType = sportTypeService.findSportTypeAll()
                .stream()
                .map(SportTypeResponseDto::new)
                .toList();

        return ResponseEntity.ok()
                .body(responseSportType);
    }

    @GetMapping("sportTypes/{sportId}")
    public ResponseEntity<SportTypeResponseDto> getSportType(@PathVariable Long sportId) {
        SportType sportType = sportTypeService.findById(sportId);

        return ResponseEntity.ok()
                .body(new SportTypeResponseDto(sportType));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        // 서비스에서 던진 예외 메시지(예: "해당 운동이 존재하지 않습니다.")와
        // 404 상태코드를 반환합니다.
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
    }
}
