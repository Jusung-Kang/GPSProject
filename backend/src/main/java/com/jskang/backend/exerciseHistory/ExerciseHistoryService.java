package com.jskang.backend.exerciseHistory;

import com.jskang.backend.domain.ExerciseHistory;
import com.jskang.backend.domain.UserM;
import com.jskang.backend.exerciseHistory.dto.SaveExerciseHistoryRequestDto;
import com.jskang.backend.userM.UserMRepository;
import com.jskang.backend.userM.dto.SaveUserMRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ExerciseHistoryService {

    private final UserMRepository userMRepository;
    private final ExerciseHistoryRepository exerciseHistoryRepository;



}
