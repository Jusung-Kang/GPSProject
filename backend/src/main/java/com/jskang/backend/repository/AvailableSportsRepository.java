package com.jskang.backend.repository;

import com.jskang.backend.domain.AvailableSports;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AvailableSportsRepository extends  JpaRepository<AvailableSports,String> {

}
