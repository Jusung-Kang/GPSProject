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

import java.util.List;

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
    public ExerciseHistoryResponseDto update(Long historyId,  SaveExerciseHistoryRequestDto requestDto) {

        ExerciseHistory history = exerciseHistoryRepository.findById(historyId)
                .orElseThrow(() -> new IllegalArgumentException("해당 정보를 찾을수 없습니다."));

        SportType sports = sportTypeRepository.findById(requestDto.getSportId())
                .orElseThrow(() -> new IllegalArgumentException("해당 스포츠를 찾을수 없습니다."));

        history.changeSportType(sports);

        return new ExerciseHistoryResponseDto(history);

    }

    public List<ExerciseHistoryResponseDto> findAll(){

        List<ExerciseHistory> exerciseHistoryList = exerciseHistoryRepository.findAllWithFetchJoin();

        return ExerciseHistoryResponseDto.from(exerciseHistoryList);
    }

    public ExerciseHistoryResponseDto findById(Long historyId) {

        ExerciseHistory exerciseHistory = exerciseHistoryRepository.findById(historyId)
                .orElseThrow(() -> new IllegalArgumentException("해당 정보를 찾을수 없습니다."));

        return new ExerciseHistoryResponseDto(exerciseHistory);
    }



}
