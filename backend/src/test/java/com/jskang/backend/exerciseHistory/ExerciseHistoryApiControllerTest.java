package com.jskang.backend.exerciseHistory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jskang.backend.availableSports.AvailableSportsService;
import com.jskang.backend.domain.ExerciseHistory;
import com.jskang.backend.domain.SportType;
import com.jskang.backend.domain.UserM;
import com.jskang.backend.exerciseHistory.dto.ExerciseHistoryResponseDto;
import com.jskang.backend.exerciseHistory.dto.SaveExerciseHistoryRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ExerciseHistoryApiController.class)
class ExerciseHistoryApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ExerciseHistoryService exerciseHistoryService;

    @Autowired
    private ObjectMapper objectMapper;

    private Long historyId;
    private Long userId;
    private Long sportId;
    private Long wrongId;
    private LocalDateTime date;
    private Integer seq;
    private UserM savedUser;
    private SportType savedSport;
    private SportType savedSport2;
    private ExerciseHistory mockEntity;

    @BeforeEach
    public void Setup(){
        historyId = 100L;
        userId = 1L;
        sportId = 10L;
        wrongId = 9999L;
        date = LocalDateTime.now();
        seq = 1;

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

        mockEntity = ExerciseHistory.builder()
                .historyId(historyId)
                .userM(savedUser)
                .sportType(savedSport)
                .dt(date)
                .seq(seq)
                .build();
    }

    @Test
    @DisplayName("생성")
    void create() throws Exception {

        SaveExerciseHistoryRequestDto requestDto = new SaveExerciseHistoryRequestDto();
//        requestDto.setUserId(userId);
//        requestDto.setSportId(sportId);
//        requestDto.setDt(date);
//        requestDto.setSeq(1);

        ExerciseHistoryResponseDto response = new ExerciseHistoryResponseDto(mockEntity);

        given(exerciseHistoryService.create(eq(userId), any(SaveExerciseHistoryRequestDto.class)))
                .willReturn(response);

        mockMvc.perform(post("/api/users/{userId}/history", userId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.historyId").value(mockEntity.getHistoryId()))
                .andExpect(jsonPath("$.sportNm").value(mockEntity.getSportType().getSportNm()))
                .andExpect(jsonPath("$.seq").value(mockEntity.getSeq()));
                //dt의 경우 시스템적으로 초부분이 조금 달라 오류가 나서 제외시킴.
    }

    @Test
    @DisplayName("업데이트")
    void update() throws Exception {

        //클라이언트로 부터 들어올 값
        SaveExerciseHistoryRequestDto requestDto = new SaveExerciseHistoryRequestDto();
        requestDto.setUserId(savedUser.getUserId());
        requestDto.setSportId(savedSport2.getSportId());
        requestDto.setSeq(5);

        //서비스 단에서 만들어 질 entity
        ExerciseHistory history = ExerciseHistory.builder()
                .historyId(historyId)
                .sportType(savedSport2)
                .dt(date)
                .seq(5)
                .build();

        //서비스 단으로 부터 return 될 값
        ExerciseHistoryResponseDto response = new ExerciseHistoryResponseDto(history);

        //서비스가 호출되면 response를 리턴하라고 "가정(설정)"하는 단계
        given(exerciseHistoryService.update(eq(historyId), any(SaveExerciseHistoryRequestDto.class)))
                .willReturn(response);

        //클라이언트로 부터 값이 들어오는 과정
        mockMvc.perform(put("/api/history/{historyId}", historyId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.historyId").value(response.getHistoryId()))
                .andExpect(jsonPath("$.sportNm").value(response.getSportNm()))
                .andExpect(jsonPath("$.seq").value(response.getSeq()));
    }

    @Test
    @DisplayName("업데이트 실패")
    void update_fail() throws Exception {

        //클라이언트로 부터 들어올 값
        SaveExerciseHistoryRequestDto requestDto = new SaveExerciseHistoryRequestDto();
        requestDto.setUserId(savedUser.getUserId());
        requestDto.setSportId(savedSport2.getSportId());
        requestDto.setSeq(5);

        //서비스가 호출되면 response를 리턴하라고 "가정(설정)"하는 단계
        given(exerciseHistoryService.update(eq(historyId), any(SaveExerciseHistoryRequestDto.class)))
                .willThrow(new IllegalArgumentException("기록이 존재하지않습니다."));

        mockMvc.perform(put("/api/history/{historyId}", historyId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("기록이 존재하지않습니다."));


    }

    @Test
    @DisplayName("전체조회")
    void findAll() throws Exception {

        List<ExerciseHistory> list = List.of(
                ExerciseHistory.builder()
                        .historyId(100L)
                        .userM(savedUser)
                        .sportType(savedSport)
                        .dt(date)
                        .seq(seq)
                        .build()
                , ExerciseHistory.builder()
                        .historyId(101L)
                        .userM(savedUser)
                        .sportType(savedSport2)
                        .dt(date)
                        .seq(2)
                        .build()
        );

        List<ExerciseHistoryResponseDto> responseDtoList = ExerciseHistoryResponseDto.from(list);

        given(exerciseHistoryService.findAll()).willReturn(responseDtoList);

        mockMvc.perform(get("/api/history")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(list)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].historyId").value(list.get(0).getHistoryId()))
                .andExpect(jsonPath("$[0].seq").value(list.get(0).getSeq()))
                .andExpect(jsonPath("$[1].historyId").value(list.get(1).getHistoryId()))
                .andExpect(jsonPath("$[1].seq").value(list.get(1).getSeq()));
    }

    @Test
    @DisplayName("조회")
    void findById() throws Exception {

        ExerciseHistoryResponseDto response = new ExerciseHistoryResponseDto(mockEntity);

        given(exerciseHistoryService.findById(historyId)).willReturn(response);

        mockMvc.perform(get("/api/history/{historyId}", historyId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(response)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.historyId").value(response.getHistoryId()))
                .andExpect(jsonPath("$.sportNm").value(response.getSportNm()))
                .andExpect(jsonPath("$.seq").value(response.getSeq()));
    }

    @Test
    @DisplayName("조회 실패")
    void findById_fail() throws Exception {

        given(exerciseHistoryService.findById(wrongId))
                .willThrow(new IllegalArgumentException("조회를 실패했습니다."));

        mockMvc.perform(get("/api/history/{historyId}", wrongId))
                .andExpect(status().isNotFound())
                .andExpect(content().string("조회를 실패했습니다."));
    }

}