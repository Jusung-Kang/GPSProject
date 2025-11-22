package com.jskang.backend.userM;

import com.jskang.backend.domain.UserM;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserMRepositoryTest {

    @Autowired
    private  UserMRepository userMRepository;

    @Test
    @DisplayName("사용자 저장 및 ID 조회 테스트")
    void saveAndFind(){
        //given
        UserM userM = UserM.builder()
                .email("aaaa@gmail.com")
                .phoneNumber("01011112222")
                .build();

        //when
        UserM savedUserM = userMRepository.save(userM);

        //then
        assertThat(savedUserM.getUserId()).isNotNull();
        assertThat(savedUserM.getEmail()).isEqualTo("aaaa@gmail.com");
        assertThat(savedUserM.getPhoneNumber()).isEqualTo("01011112222");

    }
    
    @Test
    @DisplayName("사용자 전체조회 테스트")
    void findAll(){

        UserM userM1 = UserM.builder()
                .email("aaaa@gmail.com")
                .phoneNumber("01011112222")
                .build();

        UserM userM2 = UserM.builder()
                .email("bbbb@gmail.com")
                .phoneNumber("01022223333")
                .build();

        userMRepository.save(userM1);
        userMRepository.save(userM2);

        List<UserM> userM = userMRepository.findAll();
        assertThat(userM.size()).isEqualTo(2);
        assertThat(userM).extracting("email")
                .containsExactly(userM1.getEmail(), userM2.getEmail());
        assertThat(userM).extracting("phoneNumber")
                .containsExactly(userM1.getPhoneNumber(), userM2.getPhoneNumber());
    }
    

}