package com.jskang.backend.service;

import com.jskang.backend.domain.AvailableSports;
import com.jskang.backend.dto.SaveAvailableSportsRequest;
import com.jskang.backend.repository.AvailableSportsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AvailableSportsService {

    private final AvailableSportsRepository availableSportsRepository;

    public AvailableSports save(SaveAvailableSportsRequest saveAvailableSportsRequest) {
        return availableSportsRepository.save(saveAvailableSportsRequest.toSports());
    }

    public List<AvailableSports> findAll(){
        return availableSportsRepository.findAll();
    }

    public AvailableSports findById(String id){
        return availableSportsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No Available Sports"));
    }

}
