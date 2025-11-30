package com.jskang.backend.gpsData;

import com.jskang.backend.domain.GpsData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GpsDataRepository extends JpaRepository<GpsData,Long> {
    List<GpsData> findAllByExerciseHistory_HistoryId(Long historyId);
}
