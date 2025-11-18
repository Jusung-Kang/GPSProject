package com.jskang.backend.availableSports;

import com.jskang.backend.availableSports.dto.AvailableSportsResponseDto;
import com.jskang.backend.domain.AvailableSports;
import com.jskang.backend.domain.AvailableSportsId;
import com.jskang.backend.domain.SportType;
import com.jskang.backend.domain.UserM;
import com.jskang.backend.sportType.SportTypeRepository;
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
class AvailableSportsRepositoryTest {

    @Autowired
    private AvailableSportsRepository availableSportsRepository;

    @Autowired
    private UserMRepository userMRepository;

    @Autowired
    private SportTypeRepository sportTypeRepository;

    @Test
    @DisplayName("복합키를 이용한 저장 및 조회 테스트")
    void SaveAndFind(){


        UserM userM = UserM.builder()
                .email("aaaa@gmail.com")
                .phoneNumber("01000001111")
                .build();
        userMRepository.save(userM);

        SportType sportType = SportType.builder()
                .sportId(10L)
                .sportNm("축구")
                .build();
        sportTypeRepository.save(sportType);

        AvailableSportsId pk = new AvailableSportsId(userM.getId(), sportType.getSportId());

        AvailableSports availableSports = AvailableSports.builder()
                .pk(pk)
                .userM(userM)
                .sportType(sportType)
                .level("1")
                .positionCd("1")
                .build();

        AvailableSports saved = availableSportsRepository.save(availableSports);

        AvailableSports found = availableSportsRepository.findById(pk)
                .orElseThrow();

        assertThat(found.getPk().getId().equals(userM.getId()));
        assertThat(found.getPk().getSportId().equals(sportType.getSportId()));
        assertThat(found.getLevel()).isEqualTo("1");
        assertThat(found.getPositionCd()).isEqualTo("1");

    }
    
    @Test
    @DisplayName("전체조회 성공")
    void findAll(){

        UserM userM = UserM.builder()
                .email("aaaa@gmail.com")
                .phoneNumber("01000001111")
                .build();
        userMRepository.save(userM);

        SportType sportType1 = SportType.builder()
                .sportId(10L)
                .sportNm("축구")
                .build();
        sportTypeRepository.save(sportType1);

        SportType sportType2 = SportType.builder()
                .sportId(20L)
                .sportNm("야구")
                .build();
        sportTypeRepository.save(sportType2);

        AvailableSports availableSports1 = AvailableSports.builder()
                .pk(new AvailableSportsId(userM.getId(), sportType1.getSportId()))
                .userM(userM)
                .sportType(sportType1)
                .level("1")
                .positionCd("1")
                .build();

        AvailableSports availableSports2 = AvailableSports.builder()
                .pk(new AvailableSportsId(userM.getId(), sportType2.getSportId()))
                .userM(userM)
                .sportType(sportType2)
                .level("2")
                .positionCd("2")
                .build();

        availableSportsRepository.save(availableSports1);
        availableSportsRepository.save(availableSports2);

        List<AvailableSports> findAllSports = availableSportsRepository.findAll();

        assertThat(findAllSports.size()).isEqualTo(2);
        assertThat(findAllSports).extracting("level")
                .containsExactlyInAnyOrder("1", "2");
    }

}