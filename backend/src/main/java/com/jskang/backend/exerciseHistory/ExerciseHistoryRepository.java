package com.jskang.backend.exerciseHistory;

import com.jskang.backend.domain.ExerciseHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ExerciseHistoryRepository extends JpaRepository<ExerciseHistory, Long> {
    // ExerciseHistoryRepository.java
    @Query("SELECT eh FROM ExerciseHistory eh JOIN FETCH eh.sportType JOIN FETCH eh.userM")
    List<ExerciseHistory> findAllWithFetchJoin();
}
