package com.jskang.backend.exerciseHistory;

import com.jskang.backend.availableSports.dto.SaveAvailableSportsRequestDto;
import com.jskang.backend.domain.*;
import com.jskang.backend.exerciseHistory.dto.ExerciseHistoryResponseDto;
import com.jskang.backend.exerciseHistory.dto.SaveExerciseHistoryRequestDto;
import com.jskang.backend.sportType.SportTypeRepository;
import com.jskang.backend.userM.UserMRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ExerciseHistoryService {

    private final UserMRepository userMRepository;
    private final SportTypeRepository sportTypeRepository;
    private final ExerciseHistoryRepository exerciseHistoryRepository;


    @Transactional
    public ExerciseHistoryResponseDto create(Long userId, SaveExerciseHistoryRequestDto requestDto) {

        UserM userM = userMRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을수 없습니다."));

        SportType sports = sportTypeRepository.findById(requestDto.getSportId())
                .orElseThrow(() -> new IllegalArgumentException("해당 스포츠를 찾을수 없습니다."));

        ExerciseHistory newExerciseHistory = ExerciseHistory.builder()
                .seq(requestDto.getSeq())
                .dt(requestDto.getDt())
                .userM(userM)
                .sportType(sports)
                .build();

        userM.addExerciseHistory(newExerciseHistory);

        exerciseHistoryRepository.save(newExerciseHistory);

        return new ExerciseHistoryResponseDto(newExerciseHistory);
    }

    @Transactional
    public ExerciseHistoryResponseDto update(Long historyId, SaveExerciseHistoryRequestDto requestDto) {

        ExerciseHistory history = exerciseHistoryRepository.findById(historyId)
                .orElseThrow(() -> new IllegalArgumentException("해당 정보를 찾을수 없습니다."));

        SportType sports = sportTypeRepository.findById(requestDto.getSportId())
                .orElseThrow(() -> new IllegalArgumentException("해당 스포츠를 찾을수 없습니다."));

        history.changeSportType(sports);

        return new ExerciseHistoryResponseDto(history);

    }

    @Transactional
    public ExerciseHistoryResponseDto finishExercise(Long historyId) {
        ExerciseHistory history = exerciseHistoryRepository.findById(historyId)
                .orElseThrow(() -> new IllegalArgumentException("운동기록이 없습니다."));

        List<GpsData> gpsList = history.getGpsDataList();

        if(gpsList.isEmpty()){
            return new ExerciseHistoryResponseDto(history);
        }

        BigDecimal totalDistance = gpsList.stream()
                .map(GpsData::getDistance)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal maxSpeed = gpsList.stream()
                .map(GpsData::getSpeed)
                .filter(Objects::nonNull)
                .max(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);

        //1초에 한번씩 소통한다고 했을때 하나의 데이터는 1초라고 가정함.
        //만약 5초에 한번씩 gpsdata에 데이터를 입력한다고 하면 * 5 해주면 됌.
        long totalTime = (long)gpsList.size();

        BigDecimal averagePace = BigDecimal.ZERO;
        if (totalDistance.compareTo(BigDecimal.ZERO) > 0) {
            averagePace = BigDecimal.valueOf(totalTime)
                    .divide(totalDistance, 2, RoundingMode.HALF_UP);
        }

        history.changeTotalData(totalDistance, totalTime);
        history.changeMaxSpeed(maxSpeed);
        history.changeAveragePace(averagePace);

        return new ExerciseHistoryResponseDto(history);
    }

    public List<ExerciseHistoryResponseDto> findAll() {

        List<ExerciseHistory> exerciseHistoryList = exerciseHistoryRepository.findAllWithFetchJoin();

        return ExerciseHistoryResponseDto.from(exerciseHistoryList);
    }

    public ExerciseHistoryResponseDto findById(Long historyId) {

        ExerciseHistory exerciseHistory = exerciseHistoryRepository.findById(historyId)
                .orElseThrow(() -> new IllegalArgumentException("해당 정보를 찾을수 없습니다."));

        return new ExerciseHistoryResponseDto(exerciseHistory);
    }


}
