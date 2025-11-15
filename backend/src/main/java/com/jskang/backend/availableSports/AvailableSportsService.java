package com.jskang.backend.availableSports;

import com.jskang.backend.availableSports.dto.SaveAvailableSportsRequestDto;
import com.jskang.backend.domain.AvailableSportsId;
import com.jskang.backend.domain.SportType;
import com.jskang.backend.domain.UserM;
import com.jskang.backend.sportType.SportTypeRepository;
import com.jskang.backend.userM.UserMRepository;
import org.springframework.transaction.annotation.Transactional;
import com.jskang.backend.domain.AvailableSports;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class AvailableSportsService {

    private final AvailableSportsRepository availableSportsRepository;
    private final UserMRepository userMRepository;
    private final SportTypeRepository sportTypeRepository;

    @Transactional
    public AvailableSports createAvailableSports(Long userId, SaveAvailableSportsRequestDto requestSports) {

        UserM userM = userMRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을수 없습니다. id=" + userId));

        SportType sports = sportTypeRepository.findById(requestSports.getSportId())
                .orElseThrow(() -> new IllegalArgumentException("해당 스포츠를 찾을수 없습니다. id=" + requestSports.getSportId()));

        AvailableSportsId pk = new AvailableSportsId(userM.getId(), sports.getSportId());

        AvailableSports newAvailableSports = AvailableSports.builder()
                .pk(pk)
                .sportType(sports)
                .level(requestSports.getLevel())
                .positionCd(requestSports.getPositionCd())
                .build();

        userM.addAvailableSports(newAvailableSports);

        return  availableSportsRepository.save(newAvailableSports);
    }

    @Transactional
    public AvailableSports updateAvailableSports(Long userId, Long sportId,  SaveAvailableSportsRequestDto requestSports) {

        AvailableSportsId pk = new AvailableSportsId(userId, sportId);
        AvailableSports sports = availableSportsRepository.findById(pk)
                .orElseThrow(() -> new IllegalArgumentException("해당 정보를 찾을수 없습니다."));

        sports.setLevel(requestSports.getLevel());
        sports.setPositionCd(requestSports.getPositionCd());

        return sports;

    }

    public List<AvailableSports> findAll(){
        return availableSportsRepository.findAll();
    }

    public AvailableSports findById(Long userId, Long sportId) {

        AvailableSportsId pk = new AvailableSportsId(userId, sportId);

        AvailableSports availableSports = availableSportsRepository.findById(pk)
                .orElseThrow(() -> new IllegalArgumentException("해당 스포츠가 존재하지 않습니다. id=" + sportId));

        return availableSports;
    }

}
