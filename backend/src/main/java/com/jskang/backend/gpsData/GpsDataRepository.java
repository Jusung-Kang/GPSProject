package com.jskang.backend.gpsData;

import com.jskang.backend.domain.GpsData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GpsDataRepository extends JpaRepository<GpsData,Integer> {

}
