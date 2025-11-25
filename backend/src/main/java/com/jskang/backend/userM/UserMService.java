package com.jskang.backend.userM;

import com.jskang.backend.userM.dto.SaveUserMRequestDto;
import com.jskang.backend.userM.dto.UserMResponseDto;
import org.springframework.transaction.annotation.Transactional;
import com.jskang.backend.domain.UserM;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserMService {

    private final UserMRepository userMRepository;

    @Transactional
    public UserMResponseDto create(SaveUserMRequestDto requestUser){

        UserM newUserM = UserM.builder()
                .email(requestUser.getEmail())
                .phoneNumber(requestUser.getPhoneNumber())
                .build();

        UserM userM = userMRepository.save(newUserM);

        return new UserMResponseDto(userM);
    }

    @Transactional
    public UserMResponseDto update(Long userId, SaveUserMRequestDto requestUser) {
        UserM userM = userMRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 아이디의 유저가 없습니다. id=" + userId));

        userM.updateContactInfo(requestUser.getEmail(), requestUser.getPhoneNumber());
        return new UserMResponseDto(userM);

    }

    public List<UserMResponseDto> findAll() {

        List<UserM> userM = userMRepository.findAll();

        return UserMResponseDto.from(userM);
    }

    public UserMResponseDto findById(Long userId) {

        UserM userM = userMRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을수 없습니다"));

        return new UserMResponseDto(userM);
    }

}
