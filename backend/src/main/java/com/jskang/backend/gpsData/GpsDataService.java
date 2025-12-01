package com.jskang.backend.gpsData;

import com.jskang.backend.domain.ExerciseHistory;
import com.jskang.backend.domain.GpsData;
import com.jskang.backend.exerciseHistory.ExerciseHistoryRepository;
import com.jskang.backend.gpsData.dto.GpsDataResponseDto;
import com.jskang.backend.gpsData.dto.SaveGpsDataRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class GpsDataService {

    private final GpsDataRepository gpsDataRepository;
    private final ExerciseHistoryRepository exerciseHistoryRepository;

    @Transactional
    public GpsDataResponseDto create(Long historyId, SaveGpsDataRequestDto request) {

        ExerciseHistory history = exerciseHistoryRepository.findById(historyId)
                .orElseThrow(() -> new IllegalArgumentException("정보를 찾지 못하였습니다"));

        GpsData gps = GpsData.builder()
                .longitude(request.getLongitude())
                .latitude(request.getLatitude())
                .altitude(request.getAltitude())
                .speed(request.getSpeed())
                .distance(request.getDistance())
                .heartRate(request.getHeartRate())
                .gpsSeq(request.getGpsSeq())
                .exerciseHistory(history)
                .build();

        GpsData saved = gpsDataRepository.save(gps);



        GpsDataResponseDto response = new GpsDataResponseDto(saved);

        return response;
    }

    @Transactional
    public GpsDataResponseDto update(Long gpsId, SaveGpsDataRequestDto request) {

        GpsData gps = gpsDataRepository.findById(gpsId)
                .orElseThrow(() -> new IllegalArgumentException("해당 기록은 없습니다."));

        gps.setMaxRecord(request.getMaxDistance(),  request.getMaxSpeed());

        GpsDataResponseDto response = new GpsDataResponseDto(gps);

        return response;
    }

    public List<GpsDataResponseDto> findAllByHistoryId(Long historyId){

        List<GpsData> list = gpsDataRepository.findAllByExerciseHistory_HistoryId(historyId);
        List<GpsDataResponseDto> response = GpsDataResponseDto.from(list);
        return response;

    }

// 일단 필요없다고 생각.
//    public GpsDataResponseDto findById(Long gpsId) {
//        GpsData gps = gpsDataRepository.findById(gpsId)
//                .orElseThrow(() -> new IllegalArgumentException("해당 기록은 없습니다."));
//
//        GpsDataResponseDto response = new GpsDataResponseDto(gps);
//
//        return response;
//    }

}
