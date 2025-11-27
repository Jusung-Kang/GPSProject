package com.jskang.backend.availableSports;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.jskang.backend.availableSports.dto.AvailableSportsResponseDto;
import com.jskang.backend.availableSports.dto.SaveAvailableSportsRequestDto;
import com.jskang.backend.domain.AvailableSports;
import com.jskang.backend.domain.AvailableSportsId;
import com.jskang.backend.domain.SportType;
import com.jskang.backend.domain.UserM;
import com.jskang.backend.sportType.SportTypeService;
import com.jskang.backend.sportType.dto.SaveSportTypeRequestDto;
import com.jskang.backend.userM.UserMService;
import com.jskang.backend.userM.dto.SaveUserMRequestDto;
import com.jskang.backend.userM.dto.UserMResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@WebMvcTest(AvailableSportsApiController.class)
class AvailableSportsApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AvailableSportsService availableSportsService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("스포츠 생성 테스트")
    void createSport() throws Exception {

        Long userId = 1L;
        Long sportId = 10L;

        //given
        SaveAvailableSportsRequestDto requestDto = new SaveAvailableSportsRequestDto();
        requestDto.setSportId(sportId);
        requestDto.setLevel("초보");
        requestDto.setPositionCd("수비");

        //pk 설정
        AvailableSportsId pk = new AvailableSportsId(userId, sportId);
        //가짜 Entity 생성
        AvailableSports mockEntity = AvailableSports.builder()
                .pk(pk)
                .level("초보")
                .positionCd("수비")
                .userM(UserM.builder().userId(userId).build())
                .sportType(SportType.builder().sportId(sportId).build())
                .build();

        AvailableSportsResponseDto response = new AvailableSportsResponseDto(mockEntity);

        //service 사용하여 가능한 운동 생성
        given(availableSportsService.create(eq(userId), any(SaveAvailableSportsRequestDto.class)))
                .willReturn(response);

        mockMvc.perform(post("/api/users/{userId}/sports", userId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.level").value(mockEntity.getLevel()))
                .andExpect(jsonPath("$.positionCd").value(mockEntity.getPositionCd()));

    }

    @Test
    @DisplayName("스포츠 수정(PUT) 테스트")
    void updateSport() throws Exception {

        Long userId = 1L;
        Long sportId = 10L;

        //given
        SaveAvailableSportsRequestDto requestDto = new SaveAvailableSportsRequestDto();
        requestDto.setLevel("1");
        requestDto.setPositionCd("2");

        AvailableSports mockEntity = AvailableSports.builder()
                        .pk(new AvailableSportsId(userId, sportId))
                        .level("1")
                        .positionCd("2")
                        .userM(UserM.builder().userId(userId).build())
                        .sportType(SportType.builder().sportId(sportId).build())
                        .build();

        given(availableSportsService.update(eq(userId), eq(sportId), any(SaveAvailableSportsRequestDto.class)))
                .willReturn(mockEntity);

        mockMvc.perform(put("/api/users/{userId}/sports/{sportId}", userId, sportId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.level").value(requestDto.getLevel()))
                .andExpect(jsonPath("$.positionCd").value(requestDto.getPositionCd()));
    }

    @Test
    @DisplayName("전체 유저 조회(GET) 테스트")
    void getSportAll() throws Exception {

        Long userId1 = 1L;
        Long sportId1 = 10L;

        Long userId2 = 2L;
        Long sportId2 = 20L;

        List<AvailableSports> mockEntity = List.of(
                AvailableSports.builder()
                        .pk(new AvailableSportsId(userId1, sportId1))
                        .level("기초")
                        .positionCd("포드")
                        .userM(UserM.builder().userId(userId1).build())
                        .sportType(SportType.builder().sportId(sportId1).build())
                        .build()
                , AvailableSports.builder()
                        .pk(new AvailableSportsId(userId2, sportId2))
                        .level("아마추어")
                        .positionCd("수비")
                        .userM(UserM.builder().userId(userId2).build())
                        .sportType(SportType.builder().sportId(sportId2).build())
                        .build()
        );

        List<AvailableSportsResponseDto> response = AvailableSportsResponseDto.from(mockEntity);

        given(availableSportsService.findAll()).willReturn(response);

        mockMvc.perform(get("/api/sports"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].level").value(mockEntity.get(0).getLevel()))
                .andExpect(jsonPath("$[0].positionCd").value(mockEntity.get(0).getPositionCd()))
                .andExpect(jsonPath("$[1].level").value(mockEntity.get(1).getLevel()))
                .andExpect(jsonPath("$[1].positionCd").value(mockEntity.get(1).getPositionCd()));

    }

    @Test
    void getSportById() throws  Exception {

        Long userId = 1L;
        Long sportId = 10L;

        AvailableSports mockEntity = AvailableSports.builder()
                .pk(new AvailableSportsId(userId, sportId))
                .level("기초")
                .positionCd("포드")
                .userM(UserM.builder().userId(userId).build())
                .sportType(SportType.builder().sportId(sportId).build())
                .build();

        given(availableSportsService.findById(userId, sportId)).willReturn(mockEntity);

        mockMvc.perform(get("/api/users/{userId}/sports/{sportId}", userId, sportId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.level").value(mockEntity.getLevel()))
                .andExpect(jsonPath("$.positionCd").value(mockEntity.getPositionCd()));


    }

}