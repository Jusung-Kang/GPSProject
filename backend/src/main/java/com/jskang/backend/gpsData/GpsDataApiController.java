package com.jskang.backend.gpsData;

import com.jskang.backend.domain.GpsData;
import com.jskang.backend.gpsData.dto.GpsDataResponseDto;
import com.jskang.backend.gpsData.dto.SaveGpsDataRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class GpsDataApiController {

    private final GpsDataService gpsDataService;

    @PostMapping("history/{historyId}/gps")
    public ResponseEntity<GpsDataResponseDto> create(@PathVariable Long historyId, @RequestBody SaveGpsDataRequestDto request){
        GpsDataResponseDto gpsDataResponseDto = gpsDataService.create(historyId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(gpsDataResponseDto);
    }

    @PutMapping("history/gps/{gpsId}")
    public ResponseEntity<GpsDataResponseDto> update(@PathVariable Long gpsId, @RequestBody SaveGpsDataRequestDto request){
        GpsDataResponseDto response = gpsDataService.update(gpsId, request);

        return ResponseEntity.ok()
                .body(response);
    }

    @GetMapping("history/{historyId}/gps")
    public ResponseEntity<List<GpsDataResponseDto>> findAllByHistoryId(@PathVariable Long historyId){
        List<GpsDataResponseDto> response = gpsDataService.findAllByHistoryId(historyId);

        return ResponseEntity.ok()
                .body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        // 서비스에서 던진 예외 메시지(예: "해당 유저를 찾을수 없습니다.")와
        // 404 상태코드를 반환합니다.
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
    }

}
