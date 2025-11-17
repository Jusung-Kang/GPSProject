package com.jskang.backend.userM;

import com.jskang.backend.domain.UserM;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserMRepositoryTest {

    @Autowired
    private  UserMRepository userMRepository;

    @Test
    void saveAndFind(){
        //given
        UserM userM = UserM.builder()
                .email("aaaa@gmail.com")
                .phoneNumber("01011112222")
                .build();

        //when
        UserM savedUserM = userMRepository.save(userM);

        //then
        assertThat(savedUserM.getId()).isNotNull();
        assertThat(savedUserM.getEmail()).isEqualTo("aaaa@gmail.com");
        assertThat(savedUserM.getPhoneNumber()).isEqualTo("01011112222");

    }

}