package com.jskang.backend.service;

import org.springframework.transaction.annotation.Transactional;
import com.jskang.backend.domain.AvailableSports;
import com.jskang.backend.domain.AvailableSportsId;
import com.jskang.backend.domain.SportType;
import com.jskang.backend.domain.UserM;
import com.jskang.backend.dto.SaveAvailableSportsRequestDto;
import com.jskang.backend.dto.SaveUserMRequestDto;
import com.jskang.backend.repository.AvailableSportsRepository;
import com.jskang.backend.repository.SportTypeRepository;
import com.jskang.backend.repository.UserMRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserMService {

    private final UserMRepository userMRepository;
    private final AvailableSportsRepository  availableSportsRepository;
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
                .userM(userM)
                .sportType(sports)
                .level(requestSports.getLevel())
                .positionCd(requestSports.getPositionCd())
                .build();

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

    @Transactional
    public UserM createUserM(SaveUserMRequestDto requestUser){
        UserM userM = UserM.builder()
                .email(requestUser.getEmail())
                .phoneNumber(requestUser.getPhoneNumber())
                .build();

        return userMRepository.save(userM);
    }

    @Transactional
    public UserM updateUserM(Long userId, SaveUserMRequestDto requestUser) {
        UserM userM = userMRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 아이디의 유저가 없습니다. id=" + userId));

        userM.setEmail(requestUser.getEmail());
        userM.setPhoneNumber(requestUser.getPhoneNumber());
        return userM;

    }

    public List<UserM> findAll() {
        return userMRepository.findAll();
    }

    public UserM findById(Long id) {
        return userMRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

}
