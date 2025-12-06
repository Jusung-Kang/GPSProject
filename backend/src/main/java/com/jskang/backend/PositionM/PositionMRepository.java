package com.jskang.backend.PositionM;

import com.jskang.backend.domain.AvailableSports;
import com.jskang.backend.domain.AvailableSportsId;
import com.jskang.backend.domain.PositionM;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PositionMRepository extends JpaRepository<PositionM, Long> {
}
