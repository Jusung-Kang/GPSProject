package com.jskang.backend.availableSports;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.jskang.backend.availableSports.dto.SaveAvailableSportsRequestDto;
import com.jskang.backend.domain.AvailableSports;
import com.jskang.backend.domain.AvailableSportsId;
import com.jskang.backend.domain.SportType;
import com.jskang.backend.domain.UserM;
import com.jskang.backend.sportType.SportTypeService;
import com.jskang.backend.sportType.dto.SaveSportTypeRequestDto;
import com.jskang.backend.userM.UserMService;
import com.jskang.backend.userM.dto.SaveUserMRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
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

        //service 사용하여 가능한 운동 생성
        given(availableSportsService.createAvailableSports(eq(userId), any(SaveAvailableSportsRequestDto.class)))
                .willReturn(mockEntity);

        mockMvc.perform(post("/api/users/{userId}/sports", userId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.level").value(mockEntity.getLevel()))
                .andExpect(jsonPath("$.positionCd").value(mockEntity.getPositionCd()));

    }

    @Test
    void updateSport() {

        

    }

    @Test
    void getSportAll() {
    }

    @Test
    void getSportById() {
    }

    @Test
    void handleIllegalArgumentException() {
    }
}