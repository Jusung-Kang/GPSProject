package com.jskang.backend.gpsData;

import com.jskang.backend.domain.ExerciseHistory;
import com.jskang.backend.domain.GpsData;
import com.jskang.backend.domain.SportType;
import com.jskang.backend.domain.UserM;
import com.jskang.backend.exerciseHistory.ExerciseHistoryRepository;
import com.jskang.backend.gpsData.dto.GpsDataResponseDto;
import com.jskang.backend.gpsData.dto.SaveGpsDataRequestDto;
import com.jskang.backend.sportType.SportTypeRepository;
import com.jskang.backend.userM.UserMRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class GpsDataServiceTest {

    @Mock
    UserMRepository userMRepository;
    @Mock
    SportTypeRepository sportTypeRepository;
    @Mock
    ExerciseHistoryRepository exerciseHistoryRepository;
    @Mock
    GpsDataRepository gpsDataRepository;

    @InjectMocks
    private GpsDataService gpsDataService;

    private ExerciseHistory savedHistory;
    private UserM savedUser;
    private SportType savedSportType;

    @BeforeEach
    public void setup(){


        savedUser = UserM.builder()
                .email("test@gmail.com")
                .phoneNumber("01012345678")
                .build();

        savedSportType = SportType.builder()
                .sportId(10L)
                .sportNm("축구")
                .build();

        savedHistory = ExerciseHistory.builder()
                .historyId(10L)
                .userM(savedUser)
                .sportType(savedSportType)
                .dt(LocalDateTime.now())
                .seq(1)
                .build();

    }


    @Test
    @DisplayName("생성")
    void create() {

        SaveGpsDataRequestDto request = new SaveGpsDataRequestDto();
        request.setLongitude(new BigDecimal("1.1"));
        request.setLatitude(new BigDecimal("2.2"));
        request.setAltitude(new BigDecimal("3.3"));
        request.setSpeed(new BigDecimal("4.4"));
        request.setDistance(new BigDecimal("5.5"));
        request.setGpsSeq(1);

        GpsData gps = GpsData.builder()
                .longitude(request.getLongitude())
                .latitude(request.getLatitude())
                .altitude(request.getAltitude())
                .speed(request.getSpeed())
                .distance(request.getDistance())
                .exerciseHistory(savedHistory)
                .build();

        given(exerciseHistoryRepository.findById(savedHistory.getHistoryId()))
                .willReturn(Optional.of(savedHistory));

        given(gpsDataRepository.save(any(GpsData.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

        gpsDataService.create(savedHistory.getHistoryId(), request);
        ArgumentCaptor<GpsData> argument = ArgumentCaptor.forClass(GpsData.class);
        verify(gpsDataRepository).save(argument.capture());
        GpsData saved = argument.getValue();

        //DB에 저장되기전에 잡아왔기때문에 id는 null이 되어야한다.
        assertThat(saved.getGpsId()).isNull();

        assertThat(saved.getLongitude()).isEqualByComparingTo(gps.getLongitude());
        assertThat(saved.getLatitude()).isEqualByComparingTo(gps.getLatitude());
        assertThat(saved.getAltitude()).isEqualByComparingTo(gps.getAltitude());
        assertThat(saved.getSpeed()).isEqualByComparingTo(gps.getSpeed());
    }

//    @Test
//    @DisplayName("업데이트")
//    void update() {
//
//        SaveGpsDataRequestDto request = new SaveGpsDataRequestDto();
//        request.setMaxSpeed(new BigDecimal("4.4"));
//        request.setMaxDistance(new BigDecimal("5.5"));
//        request.setGpsSeq(1);
//
//        GpsData gps = GpsData.builder()
//                .longitude(new BigDecimal("1.1"))
//                .latitude(new BigDecimal("2.2"))
//                .altitude(new  BigDecimal("3.3"))
//                .exerciseHistory(savedHistory)
//                .build();
//
//        given(gpsDataRepository.findById(savedHistory.getHistoryId()))
//                .willReturn(Optional.of(gps));
//
//        gpsDataService.update(savedHistory.getHistoryId(), request);
//
//    }

//    @Test
//    @DisplayName("업데이트 실패")
//    void update_fail() {
//
//        Long wrongId = 99L;
//
//        SaveGpsDataRequestDto request = new SaveGpsDataRequestDto();
//        request.setMaxSpeed(new BigDecimal("4.4"));
//        request.setMaxDistance(new BigDecimal("5.5"));
//        request.setGpsSeq(1);
//
//        GpsData gps = GpsData.builder()
//                .longitude(new BigDecimal("1.1"))
//                .latitude(new BigDecimal("2.2"))
//                .altitude(new  BigDecimal("3.3"))
//                .maxSpeed(new  BigDecimal("1.1"))
//                .maxDistance(new  BigDecimal("1.1"))
//                .exerciseHistory(savedHistory)
//                .build();
//
//        given(gpsDataRepository.findById(wrongId))
//                .willReturn(Optional.empty());
//
//        assertThatThrownBy(() -> gpsDataService.update(wrongId, request))
//                .isInstanceOf(IllegalArgumentException.class)
//                .hasMessageContaining("해당 기록은 없습니다.");
//    }

    @Test
    @DisplayName("히스토리ID로 조회")
    void findAllByHistoryId() {

        Long historyId = savedHistory.getHistoryId();

        GpsData gps1 = GpsData.builder()
                .longitude(new BigDecimal("1.1"))
                .latitude(new BigDecimal("1.2"))
                .altitude(new  BigDecimal("1.3"))
                .exerciseHistory(savedHistory)
                .build();

        GpsData gps2 = GpsData.builder()
                .longitude(new BigDecimal("2.1"))
                .latitude(new BigDecimal("2.2"))
                .altitude(new  BigDecimal("2.3"))
                .exerciseHistory(savedHistory)
                .build();

        given(gpsDataRepository.findAllByExerciseHistory_HistoryId(historyId))
                .willReturn(List.of(gps1, gps2));

        List<GpsDataResponseDto> list = gpsDataService.findAllByHistoryId(historyId);

        assertThat(list).hasSize(2);
        assertThat(list)
                .extracting("longitude", "latitude")
                .containsExactly(
                        tuple(gps1.getLongitude(), gps1.getLatitude())
                        , tuple(gps2.getLongitude(), gps2.getLatitude())
                );
    }

    @Test
    @DisplayName("히스토리ID로 조회 실패")
    void findAllByHistoryId_fail() {

        Long wrongId = 99L;

        GpsData gps1 = GpsData.builder()
                .longitude(new BigDecimal("1.1"))
                .latitude(new BigDecimal("1.2"))
                .altitude(new  BigDecimal("1.3"))
                .exerciseHistory(savedHistory)
                .build();

        GpsData gps2 = GpsData.builder()
                .longitude(new BigDecimal("2.1"))
                .latitude(new BigDecimal("2.2"))
                .altitude(new  BigDecimal("2.3"))
                .exerciseHistory(savedHistory)
                .build();

        given(gpsDataRepository.findAllByExerciseHistory_HistoryId(wrongId))
                .willReturn(List.of());

        List<GpsDataResponseDto> list = gpsDataService.findAllByHistoryId(wrongId);

        assertThat(list).isEmpty();
        assertThat(list.size()).isEqualTo(0);
    }

//    @Test
//    @DisplayName("히스토리ID로 조회")
//    void findById() {
//
//        GpsData gps1 = GpsData.builder()
//                .gpsId(1L)
//                .longitude(new BigDecimal("1.1"))
//                .latitude(new BigDecimal("1.2"))
//                .altitude(new  BigDecimal("1.3"))
//                .maxSpeed(new  BigDecimal("1.1"))
//                .maxDistance(new  BigDecimal("1.1"))
//                .exerciseHistory(savedHistory)
//                .build();
//
//
//        given(gpsDataRepository.findById(gps1.getGpsId()))
//                .willReturn(Optional.of(gps1));
//
//
//    }
}