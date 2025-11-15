package com.jskang.backend.exerciseHistory;

import com.jskang.backend.domain.ExerciseHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExerciseHistoryRepository extends JpaRepository<ExerciseHistory, Integer> {

}
