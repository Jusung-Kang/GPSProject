package com.jskang.backend.sportType;

import com.jskang.backend.domain.SportType;
import com.jskang.backend.sportType.dto.SaveSportTypeRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly=true)
@RequiredArgsConstructor
@Service
public class SportTypeService {

    private final SportTypeRepository sportTypeRepository;

    @Transactional
    public SportType createSportType(SaveSportTypeRequestDto saveSportType) {

        SportType sportType = SportType.builder()
                .sportId(saveSportType.getSportId())
                .sportNm(saveSportType.getSportNm())
                .build();

        return sportTypeRepository.save(sportType);
    }

    @Transactional
    public SportType updateSportType(Long sportId, SaveSportTypeRequestDto saveSportType) {

        SportType sportType = sportTypeRepository.findById(sportId)
                .orElseThrow(() -> new IllegalArgumentException("해당 운동이 존재하지 않습니다. id=" +  sportId));

        sportType.setSportNm(saveSportType.getSportNm());

        return sportType;

    }

    public List<SportType> findSportTypeAll(){

        return sportTypeRepository.findAll();
    }

    public SportType findById(Long sportId){

        return sportTypeRepository.findById(sportId)
                .orElseThrow(() -> new IllegalArgumentException("해당 운동이 존재하지 않습니다. id=" + sportId));

    }

}
