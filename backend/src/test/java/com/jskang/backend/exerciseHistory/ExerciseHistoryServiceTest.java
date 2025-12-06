package com.jskang.backend.exerciseHistory;

import com.jskang.backend.domain.*;
import com.jskang.backend.exerciseHistory.dto.ExerciseHistoryResponseDto;
import com.jskang.backend.exerciseHistory.dto.SaveExerciseHistoryRequestDto;
import com.jskang.backend.gpsData.GpsDataRepository;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ExerciseHistoryServiceTest {

    @Mock
    private UserMRepository userMRepository;
    @Mock
    private SportTypeRepository sportTypeRepository;
    @Mock
    private ExerciseHistoryRepository exerciseHistoryRepository;

    @Mock
    private GpsDataRepository gpsDataRepository;

    @InjectMocks
    private ExerciseHistoryService exerciseHistoryService;

    private UserM savedUser;
    private SportType savedSport;
    private SportType savedSport2;

    @BeforeEach
    void setUp() {
        // 모든 테스트 실행 전에 수행됨 (중복 제거)
        savedUser = UserM.builder()
                .userId(1L)
                .email("test@gmail.com")
                .phoneNumber("01012345678")
                .build();

        savedSport = SportType.builder()
                .sportId(10L)
                .sportNm("축구")
                .build();

        savedSport2 = SportType.builder()
                .sportId(20L)
                .sportNm("야구")
                .build();
    }

    @Test
    @DisplayName("신규생성")
    void create() {

        SaveExerciseHistoryRequestDto requestDto = new SaveExerciseHistoryRequestDto();
        requestDto.setUserId(savedUser.getUserId());
        requestDto.setSportId(savedSport.getSportId());
        requestDto.setDt(LocalDateTime.now());
        requestDto.setSeq(1);

        given(userMRepository.findById(savedUser.getUserId())).willReturn(Optional.of(savedUser));
        given(sportTypeRepository.findById(savedSport.getSportId())).willReturn(Optional.of(savedSport));

        given(exerciseHistoryRepository.save(any(ExerciseHistory.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

        exerciseHistoryService.create(savedUser.getUserId(), requestDto);

        ArgumentCaptor<ExerciseHistory> exerciseHistoryArgumentCaptor = ArgumentCaptor.forClass(ExerciseHistory.class);

        verify(exerciseHistoryRepository).save(exerciseHistoryArgumentCaptor.capture());

        ExerciseHistory savedEntity = exerciseHistoryArgumentCaptor.getValue();

        assertThat(savedEntity.getUserM().getUserId()).isEqualTo(requestDto.getUserId());
        assertThat(savedEntity.getSportType().getSportId()).isEqualTo(requestDto.getSportId());
        assertThat(savedEntity.getDt()).isEqualTo(requestDto.getDt());
        assertThat(savedEntity.getSeq()).isEqualTo(requestDto.getSeq());

    }

    @Test
    @DisplayName("업데이트")
    void update() {

        Long historyId = 100L;

        SaveExerciseHistoryRequestDto requestDto = new SaveExerciseHistoryRequestDto();
        requestDto.setSportId(savedSport2.getSportId());

        ExerciseHistory existing = ExerciseHistory.builder()
                .historyId(historyId)
                .userM(savedUser)
                .sportType(savedSport)
                .dt(LocalDateTime.now())
                .seq(1)
                .build();

        given(sportTypeRepository.findById(savedSport2.getSportId())).willReturn(Optional.of(savedSport2));
        given(exerciseHistoryRepository.findById(historyId))
                .willReturn(Optional.of(existing));

        exerciseHistoryService.update(historyId, requestDto);

        assertThat(existing.getSportType()).isEqualTo(savedSport2);

    }

    @Test
    @DisplayName("업데이트")
    void update_fail(){
        Long wrongId =  9999L;
        SaveExerciseHistoryRequestDto requestDto = new SaveExerciseHistoryRequestDto();
        given(exerciseHistoryRepository.findById(wrongId)).willReturn(Optional.empty());
        assertThatThrownBy(() -> exerciseHistoryService.update(wrongId, requestDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("해당 정보를 찾을수 없습니다.");
    }

    @Test
    @DisplayName("운동종료")
    void finishExercise(){

        ExerciseHistory history = ExerciseHistory.builder()
                .historyId(100L)
                .userM(savedUser)
                .sportType(savedSport)
                .dt(LocalDateTime.now())
                .seq(1)
                .build();

        GpsData gpsData1 = GpsData.builder()
                .exerciseHistory(history)
                .distance(new BigDecimal("1.0"))
                .speed(new BigDecimal("1.1"))
                .longitude(new BigDecimal("1.1"))
                .latitude(new BigDecimal("1.1"))
                .altitude(new BigDecimal("1.1"))
                .build();
        GpsData gpsData2 = GpsData.builder()
                .exerciseHistory(history)
                .distance(new BigDecimal("2.0"))
                .speed(new BigDecimal("2.2"))
                .longitude(new BigDecimal("2.2"))
                .latitude(new BigDecimal("2.2"))
                .altitude(new BigDecimal("2.2"))
                .build();
        GpsData gpsData3 = GpsData.builder()
                .exerciseHistory(history)
                .distance(new BigDecimal("3.0"))
                .speed(new BigDecimal("3.3"))
                .longitude(new BigDecimal("3.3"))
                .latitude(new BigDecimal("3.3"))
                .altitude(new BigDecimal("3.3"))
                .build();

        history.addGpsData(gpsData1);
        history.addGpsData(gpsData2);
        history.addGpsData(gpsData3);

        given(exerciseHistoryRepository.findById(history.getHistoryId()))
                .willReturn(Optional.of(history));

        ExerciseHistoryResponseDto response = exerciseHistoryService.finishExercise(history.getHistoryId());

        assertThat(response.getTotalDistance()).isEqualByComparingTo(new BigDecimal("6.0"));
        assertThat(response.getMaxSpeed()).isEqualByComparingTo(new BigDecimal("3.3"));
        assertThat(response.getTotalTime()).isEqualTo(3L);
        assertThat(response.getAveragePace()).isEqualByComparingTo(new BigDecimal("0.5"));

    }

    @Test
    @DisplayName("전체 조회(FindAll) 테스트")
    void findAll() {

        ExerciseHistory history1 = ExerciseHistory.builder()
                .userM(savedUser)
                .sportType(savedSport)
                .dt(LocalDateTime.now())
                .seq(1)
                .build();

        ExerciseHistory history2 = ExerciseHistory.builder()
                .userM(savedUser)
                .sportType(savedSport2)
                .dt(LocalDateTime.now())
                .seq(2)
                .build();

        given(exerciseHistoryRepository.findAllWithFetchJoin())
                .willReturn(List.of(history1, history2));

        List<ExerciseHistoryResponseDto> list =  exerciseHistoryService.findAll();

        assertThat(list).hasSize(2);
        assertThat(list).extracting("seq")
                .containsExactly(1, 2);

    }

    @Test
    @DisplayName("단건 조회(FindById) 테스트")
    void findById() {

        Long historyId = 100L;

        ExerciseHistory history1 = ExerciseHistory.builder()
                .historyId(historyId)
                .userM(savedUser)
                .sportType(savedSport)
                .dt(LocalDateTime.now())
                .seq(1)
                .build();

        given(exerciseHistoryRepository.findById(historyId)).willReturn(Optional.of(history1));

        ExerciseHistoryResponseDto response = exerciseHistoryService.findById(historyId);

        assertThat(response).isNotNull();
        assertThat(response.getHistoryId()).isEqualTo(historyId);
        assertThat(response.getSeq()).isEqualTo(history1.getSeq());

    }

    @Test
    @DisplayName("단건 조회(FindById) 실패 테스트")
    void findById_Fail() {

        Long wrongId =  9999L;

        given(exerciseHistoryRepository.findById(wrongId)).willReturn(Optional.empty());

        assertThatThrownBy(() -> exerciseHistoryService.findById(wrongId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("해당 정보를 찾을수 없습니다.");

        verify(exerciseHistoryRepository).findById(wrongId);
    }
}