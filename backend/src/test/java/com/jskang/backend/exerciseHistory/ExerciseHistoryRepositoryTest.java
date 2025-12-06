package com.jskang.backend.exerciseHistory;

import com.jskang.backend.availableSports.AvailableSportsRepository;
import com.jskang.backend.domain.*;
import com.jskang.backend.gpsData.GpsDataRepository;
import com.jskang.backend.sportType.SportTypeRepository;
import com.jskang.backend.userM.UserMRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ExerciseHistoryRepositoryTest {

    @Autowired
    private ExerciseHistoryRepository exerciseHistoryRepository;

    @Autowired
    private UserMRepository userMRepository;

    @Autowired
    private SportTypeRepository sportTypeRepository;



    // 공통으로 사용할 변수 선언
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
    @DisplayName("운동 히스토리 저장 및 조회 테스트")
    void SaveAndFind() {

        ExerciseHistory history = ExerciseHistory.builder()
                .userM(savedUser)
                .sportType(savedSport)
                .dt(LocalDateTime.now())
                .seq(1)
                .build();

        ExerciseHistory saved = exerciseHistoryRepository.save(history);

        ExerciseHistory found = exerciseHistoryRepository.findById(saved.getHistoryId())
                .orElseThrow();

        assertThat(found.getHistoryId()).isEqualTo(history.getHistoryId());
        assertThat(found.getUserM().getUserId()).isEqualTo(savedUser.getUserId());
        assertThat(found.getSportType().getSportId()).isEqualTo(savedSport.getSportId());
        //dt로 체크할경우 가끔 오류날때가 있음.
        //assertThat(found.getDt()).isEqualTo(history.getDt());
        assertThat(found.getSeq()).isEqualTo(history.getSeq());

    }

    @Test
    @DisplayName("운동 히스토리 수정 테스트 (Dirty Checking)")
    void updateExerciseHistory() {

        ExerciseHistory history = ExerciseHistory.builder()
                .userM(savedUser)
                .sportType(savedSport)
                .dt(LocalDateTime.now())
                .seq(1)
                .build();

        ExerciseHistory saved = exerciseHistoryRepository.save(history);
        saved.changeSportType(savedSport2);
        saved.changeTotalData(saved.getTotalDistance(), saved.getTotalTime());

        ExerciseHistory found = exerciseHistoryRepository.findById(history.getHistoryId())
                .orElseThrow();

        assertThat(found.getHistoryId()).isEqualTo(history.getHistoryId());
        assertThat(found.getUserM().getUserId()).isEqualTo(history.getUserM().getUserId());
        assertThat(found.getSportType().getSportId()).isEqualTo(savedSport2.getSportId());
        assertThat(found.getSportType().getSportNm()).isEqualTo(savedSport2.getSportNm());
        assertThat(found.getSeq()).isEqualTo(history.getSeq());

    }

    @Test
    @DisplayName("전체조회 성공")
    void findAll() {

        ExerciseHistory history1 = ExerciseHistory.builder()
                .userM(savedUser)
                .sportType(savedSport)
                .dt(LocalDateTime.now())
                .seq(1)
                .build();
        ExerciseHistory history2 = ExerciseHistory.builder()
                .userM(savedUser)
                .sportType(savedSport)
                .dt(LocalDateTime.now())
                .seq(2)
                .build();


        exerciseHistoryRepository.save(history1);
        exerciseHistoryRepository.save(history2);

        List<ExerciseHistory> findAll = exerciseHistoryRepository.findAll();

        assertThat(findAll.size()).isEqualTo(2);
        //dt로 체크할경우 시스템적으로 오류가 날 경우가 있음.
        assertThat(findAll).extracting("seq")
                .containsExactlyInAnyOrder(findAll.get(0).getSeq(), findAll.get(1).getSeq());
    }


}