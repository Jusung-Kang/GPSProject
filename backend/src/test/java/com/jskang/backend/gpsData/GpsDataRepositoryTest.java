package com.jskang.backend.gpsData;

import com.jskang.backend.domain.ExerciseHistory;
import com.jskang.backend.domain.GpsData;
import com.jskang.backend.domain.SportType;
import com.jskang.backend.domain.UserM;
import com.jskang.backend.exerciseHistory.ExerciseHistoryRepository;
import com.jskang.backend.sportType.SportTypeRepository;
import com.jskang.backend.userM.UserMRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
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
import static org.assertj.core.api.Assertions.tuple;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class GpsDataRepositoryTest {

    @Autowired
    private UserMRepository userMRepository;
    @Autowired
    private SportTypeRepository sportTypeRepository;
    @Autowired
    private ExerciseHistoryRepository exerciseHistoryRepository;
    @Autowired
    private GpsDataRepository gpsDataRepository;

    @PersistenceContext
    private EntityManager em;

    private ExerciseHistory savedHistory;
    private UserM savedUser;
    private SportType savedSportType;

    @BeforeEach
    public void setup(){


        UserM userM = UserM.builder()
                .email("test@gmail.com")
                .phoneNumber("01012345678")
                .build();
        savedUser = userMRepository.save(userM);

        SportType sportType = SportType.builder()
                .sportId(10L)
                .sportNm("축구")
                .build();
        savedSportType = sportTypeRepository.save(sportType);

        ExerciseHistory history = ExerciseHistory.builder()
                .userM(userM)
                .sportType(sportType)
                .dt(LocalDateTime.now())
                .seq(1)
                .build();
        savedHistory = exerciseHistoryRepository.save(history);

    }

    @Test
    @DisplayName("생성")
    void create(){

        GpsData gps = GpsData.builder()
                .longitude(new BigDecimal("1.1"))
                .latitude(new BigDecimal("2.2"))
                .altitude(new BigDecimal("3.3"))
                .speed(new BigDecimal("4.4"))
                .distance(new BigDecimal("5.5"))
                .exerciseHistory(savedHistory)
                .build();

        GpsData saved =  gpsDataRepository.save(gps);

        assertThat(saved.getGpsId()).isEqualTo(gps.getGpsId());
        assertThat(saved.getLongitude()).isEqualTo(gps.getLongitude());
        assertThat(saved.getLatitude()).isEqualTo(gps.getLatitude());
        assertThat(saved.getAltitude()).isEqualTo(gps.getAltitude());
        assertThat(saved.getDistance()).isEqualTo(gps.getDistance());
        assertThat(saved.getSpeed()).isEqualTo(gps.getSpeed());

    }

    @Test
    @DisplayName("업데이트")
    void update(){

        GpsData gps = GpsData.builder()
                .longitude(new BigDecimal("1.1"))
                .latitude(new BigDecimal("2.2"))
                .altitude(new BigDecimal("3.3"))
                .speed(new BigDecimal("4.4"))
                .maxSpeed(new BigDecimal("6.6"))
                .distance(new BigDecimal("5.5"))
                .maxDistance(new BigDecimal("7.7"))
                .exerciseHistory(savedHistory)
                .build();

        GpsData saved =  gpsDataRepository.save(gps);

        gps.setMaxRecord(new BigDecimal("8.8"), new BigDecimal("9.9"));

        em.flush();
        em.clear();

        GpsData updatedGps = gpsDataRepository.findById(saved.getGpsId()).orElseThrow();

        assertThat(updatedGps.getGpsId()).isEqualTo(gps.getGpsId());
        assertThat(updatedGps.getMaxDistance()).isEqualByComparingTo(new BigDecimal("8.8"));
        assertThat(updatedGps.getMaxSpeed()).isEqualByComparingTo(new BigDecimal("9.9"));

    }

    @Test
    @DisplayName("운동 히스토리 아이디와 연관된 전체 GPS 데이터 조회")
    void findAllByExerciseHistory_HistoryId() {

        GpsData gps1 = GpsData.builder()
                .exerciseHistory(savedHistory)
                .longitude(new BigDecimal("127.001"))
                .latitude(new BigDecimal("37.001"))
                .altitude(new BigDecimal("10.0"))
                .speed(new BigDecimal("5.0"))
                .distance(new BigDecimal("0.0"))
                .gpsSeq(1)
                .build();

        GpsData gps2 = GpsData.builder()
                .exerciseHistory(savedHistory)
                .longitude(new BigDecimal("127.002"))
                .latitude(new BigDecimal("37.002"))
                .altitude(new BigDecimal("12.0"))
                .speed(new BigDecimal("6.0"))
                .distance(new BigDecimal("10.0"))
                .gpsSeq(2)
                .build();

        gpsDataRepository.saveAll(List.of(gps1, gps2));

        List<GpsData> saved =  gpsDataRepository.findAllByExerciseHistory_HistoryId(savedHistory.getHistoryId());

        assertThat(saved.size()).isEqualTo(2);
        assertThat(saved)
                .extracting("longitude", "latitude")
                .containsExactly(
                        tuple(gps1.getLongitude(), gps1.getLatitude())
                        , tuple(gps2.getLongitude(), gps2.getLatitude())
                );
    }

    @Test
    @DisplayName("GPSID로 조회")
    void findById(){

        ExerciseHistory existingHistory = exerciseHistoryRepository.findById(savedHistory.getHistoryId())
                .orElseThrow(() -> new IllegalArgumentException("히스토리가 없습니다."));

        GpsData gps = GpsData.builder()
                .longitude(new BigDecimal("1.1"))
                .latitude(new BigDecimal("2.2"))
                .altitude(new BigDecimal("3.3"))
                .speed(new BigDecimal("4.4"))
                .maxSpeed(new BigDecimal("6.6"))
                .distance(new BigDecimal("5.5"))
                .maxDistance(new BigDecimal("7.7"))
                .exerciseHistory(existingHistory)
                .build();

        gpsDataRepository.save(gps);

        GpsData saved = gpsDataRepository.findById(gps.getGpsId())
                .orElseThrow(() -> new IllegalArgumentException("해당 정보는 없습니다."));

        assertThat(saved.getGpsId()).isEqualTo(gps.getGpsId());
        assertThat(saved.getLongitude()).isEqualTo(gps.getLongitude());
        assertThat(saved.getLatitude()).isEqualTo(gps.getLatitude());
        assertThat(saved.getAltitude()).isEqualTo(gps.getAltitude());
        assertThat(saved.getDistance()).isEqualTo(gps.getDistance());


    }
}