package com.jskang.backend.sportType;

import com.jskang.backend.domain.SportType;
import com.jskang.backend.domain.UserM;
import com.jskang.backend.userM.UserMRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class SportTypeRepositoryTest {

    @Autowired
    private SportTypeRepository sportTypeRepository;

    @Test
    @DisplayName("스포츠 저장 및 ID 조회 테스트")
    void saveAndFind(){
        //given

        SportType sportType = SportType.builder()
                .sportId(1L)
                .sportNm("축구")
                .build();

        //when
        SportType savedSport = sportTypeRepository.save(sportType);

        //then
        assertThat(savedSport.getSportId()).isNotNull();
        assertThat(savedSport.getSportNm()).isEqualTo(sportType.getSportNm());

    }

    @Test
    @DisplayName("스포츠 전체조회 테스트")
    void findAll(){

        SportType sportType1 = SportType.builder()
                .sportId(1L)
                .sportNm("축구")
                .build();

        SportType sportType2 = SportType.builder()
                .sportId(2L)
                .sportNm("야구")
                .build();

        sportTypeRepository.save(sportType1);
        sportTypeRepository.save(sportType2);

        List<SportType> sport = sportTypeRepository.findAll();
        assertThat(sport.size()).isEqualTo(2);
        assertThat(sport).extracting("sportNm")
                .containsExactly(sportType1.getSportNm(), sportType2.getSportNm());

    }


}