package com.jskang.backend.repository;

import com.jskang.backend.domain.AvailableSports;
import com.jskang.backend.domain.AvailableSportsId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AvailableSportsRepository extends  JpaRepository<AvailableSports, AvailableSportsId> {

}
