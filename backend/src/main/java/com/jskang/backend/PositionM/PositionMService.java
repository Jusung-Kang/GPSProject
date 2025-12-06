package com.jskang.backend.PositionM;

import com.jskang.backend.PositionM.dto.PositionMResponseDto;
import com.jskang.backend.PositionM.dto.SavePositionMRequestDto;
import com.jskang.backend.availableSports.AvailableSportsRepository;
import com.jskang.backend.domain.PositionM;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class PositionMService {

    private final PositionMRepository positionMRepository;
    private final AvailableSportsRepository availableSportsRepository;

    @Transactional
    public PositionMResponseDto create(SavePositionMRequestDto request) {

        PositionM positionM = PositionM.builder()
                .positionNm(request.getPositionNm())
                .build();

        positionMRepository.save(positionM);

        return new PositionMResponseDto(positionM);

    }

    @Transactional
    public PositionMResponseDto update(Long positionId, SavePositionMRequestDto request) {
        PositionM positionM  = positionMRepository.findById(positionId)
                .orElseThrow(()-> new IllegalThreadStateException("해당 포지션이 없습니다."));

        positionM.changePositionNm(request.getPositionNm());

        return new PositionMResponseDto(positionM);

    }

    public List<PositionMResponseDto> findAll() {
        List<PositionM> positionM = positionMRepository.findAll();

        return PositionMResponseDto.from(positionM);
    }

    public PositionMResponseDto findById(Long positionId) {
        PositionM positionM = positionMRepository.findById(positionId)
                .orElseThrow(() -> new IllegalArgumentException("해당 포지션이 없습니다."));

        return new PositionMResponseDto(positionM);
    }


}
