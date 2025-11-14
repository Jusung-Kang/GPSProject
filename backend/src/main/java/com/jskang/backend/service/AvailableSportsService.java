package com.jskang.backend.service;

import com.jskang.backend.domain.AvailableSports;
import com.jskang.backend.dto.SaveAvailableSportsRequestDto;
import com.jskang.backend.repository.AvailableSportsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AvailableSportsService {

    private final AvailableSportsRepository availableSportsRepository;



}
