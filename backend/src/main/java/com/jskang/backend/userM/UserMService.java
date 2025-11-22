package com.jskang.backend.userM;

import com.jskang.backend.userM.dto.SaveUserMRequestDto;
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
                .orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을수 없습니다"));
    }

}
