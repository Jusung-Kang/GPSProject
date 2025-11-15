package com.jskang.backend.sportType;

import com.jskang.backend.domain.SportType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SportTypeRepository extends JpaRepository<SportType, Long> {
}
