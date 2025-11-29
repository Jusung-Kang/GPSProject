package com.jskang.backend.availableSports;

import com.jskang.backend.domain.AvailableSports;
import com.jskang.backend.domain.AvailableSportsId;
import com.jskang.backend.domain.SportType;
import com.jskang.backend.domain.UserM;
import com.jskang.backend.sportType.SportTypeRepository;
import com.jskang.backend.userM.UserMRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
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

    @Autowired
    EntityManager em;

    private UserM savedUser;
    private SportType savedSport;
    private SportType savedSport2;

    @BeforeEach
    void setUp() {
        // 모든 테스트 실행 전에 수행됨 (중복 제거)
        UserM userM = UserM.builder()
                .email("test@gmail.com")
                .phoneNumber("01012345678")
                .build();
        savedUser = userMRepository.save(userM);

        SportType sportType = SportType.builder()
                .sportId(10L)
                .sportNm("축구")
                .build();
        savedSport = sportTypeRepository.save(sportType);

        SportType sportType2 = SportType.builder()
                .sportId(20L)
                .sportNm("야구")
                .build();
        savedSport2 = sportTypeRepository.save(sportType2);
    }

    @Test
    @DisplayName("복합키를 이용한 저장 및 조회 테스트")
    void SaveAndFind(){

        AvailableSportsId pk = new AvailableSportsId(savedUser.getUserId(), savedSport.getSportId());

        AvailableSports availableSports = AvailableSports.builder()
                .pk(pk)
                .userM(savedUser)
                .sportType(savedSport)
                .level("1")
                .positionCd("1")
                .build();

        AvailableSports saved = availableSportsRepository.save(availableSports);

        AvailableSports found = availableSportsRepository.findById(pk)
                .orElseThrow();

        assertThat(found.getPk().getUserId().equals(savedUser.getUserId()));
        assertThat(found.getPk().getSportId().equals(savedSport.getSportId()));
        assertThat(found.getLevel()).isEqualTo("1");
        assertThat(found.getPositionCd()).isEqualTo("1");

    }

    @Test
    @DisplayName("수정 테스트 (Dirty Checking)")
    void update(){

        AvailableSportsId pk = new AvailableSportsId(savedUser.getUserId(), savedSport.getSportId());
        AvailableSports availableSports = AvailableSports.builder()
                .pk(pk)
                .userM(savedUser)
                .sportType(savedSport)
                .level("1")
                .positionCd("1")
                .build();

        AvailableSports saved = availableSportsRepository.save(availableSports);
        saved.changeAvailableInfo("2", "3");

        em.flush();
        em.clear();

        AvailableSports found = availableSportsRepository.findById(pk)
                .orElseThrow();

        assertThat(found.getPk().getUserId()).isEqualTo(savedUser.getUserId());
        assertThat(found.getPk().getSportId()).isEqualTo(savedSport.getSportId());
        assertThat(found.getLevel()).isEqualTo("2");
        assertThat(found.getPositionCd()).isEqualTo("3");
    }

    @Test
    @DisplayName("전체조회 성공")
    void findAll(){

        AvailableSports availableSports1 = AvailableSports.builder()
                .pk(new AvailableSportsId(savedUser.getUserId(), savedSport.getSportId()))
                .userM(savedUser)
                .sportType(savedSport)
                .level("1")
                .positionCd("1")
                .build();

        AvailableSports availableSports2 = AvailableSports.builder()
                .pk(new AvailableSportsId(savedUser.getUserId(), savedSport2.getSportId()))
                .userM(savedUser)
                .sportType(savedSport2)
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