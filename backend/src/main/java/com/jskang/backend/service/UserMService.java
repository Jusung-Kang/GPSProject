package com.jskang.backend.service;

import com.jskang.backend.domain.UserM;
import com.jskang.backend.dto.SaveUserRequest;
import com.jskang.backend.repository.UserMRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserMService {

    private UserMRepository userMRepository;

    public UserM save(SaveUserRequest saveUserRequest) {
        return userMRepository.save(saveUserRequest.toUser());
    }

}
