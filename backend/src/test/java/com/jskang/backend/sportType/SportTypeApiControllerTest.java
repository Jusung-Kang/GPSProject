package com.jskang.backend.sportType;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jskang.backend.domain.SportType;
import com.jskang.backend.domain.UserM;
import com.jskang.backend.sportType.dto.SaveSportTypeRequestDto;
import com.jskang.backend.userM.UserMApiController;
import com.jskang.backend.userM.UserMService;
import com.jskang.backend.userM.dto.SaveUserMRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(SportTypeApiController.class)
class SportTypeApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SportTypeService sportTypeService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("스포츠 생성(POST) 테스트")
    void createSportType() throws Exception {
        // given
        SaveSportTypeRequestDto requestDto = new SaveSportTypeRequestDto();
        requestDto.setSportId(1L);
        requestDto.setSportNm("축구");

        SportType sportType = SportType.builder()
                .sportId(1L)
                .sportNm("축구")
                .build();

        given(sportTypeService.createSportType(any(SaveSportTypeRequestDto.class)))
                .willReturn(sportType);

        // when & then
        mockMvc.perform(post("/api/sportTypes")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.sportId").value(sportType.getSportId()))
                .andExpect(jsonPath("$.sportNm").value(sportType.getSportNm()));
    }

    @Test
    @DisplayName("스포츠 수정(PUT) 테스트")
    void updateSportType() throws Exception {
        // given
        Long sportId = 1L;
        SaveSportTypeRequestDto requestDto = new SaveSportTypeRequestDto();
        requestDto.setSportId(sportId);
        requestDto.setSportNm("축구");

        SportType updatedSport = SportType.builder()
                .sportId(sportId)
                .sportNm("야구")
                .build();

        given(sportTypeService.updateSportType(eq(sportId), any(SaveSportTypeRequestDto.class)))
                .willReturn(updatedSport);

        // when & then
        mockMvc.perform(put("/api/sportTypes/{sportId}", sportId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sportNm").value(updatedSport.getSportNm()));
    }

    @Test
    @DisplayName("전체 스포츠 조회(GET) 테스트")
    void getSportTypeAll() throws Exception {
        List<SportType> sportTypes = List.of(
                SportType.builder().sportId(1L).sportNm("축구").build(),
                SportType.builder().sportId(2L).sportNm("야구").build()
        );

        given(sportTypeService.findAll()).willReturn(sportTypes);

        mockMvc.perform(get("/api/sportTypes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].sportId").value(sportTypes.get(0).getSportId()))
                .andExpect(jsonPath("$[0].sportNm").value(sportTypes.get(0).getSportNm()))
                .andExpect(jsonPath("$[1].sportId").value(sportTypes.get(1).getSportId()))
                .andExpect(jsonPath("$[1].sportNm").value(sportTypes.get(1).getSportNm()));
    }

    @Test
    @DisplayName("단건 스포츠 조회(GET) 테스트")
    void getSportType() throws Exception {
        Long sportId = 1L;
        SportType sport = SportType.builder().sportId(sportId).sportNm("축구").build();

        given(sportTypeService.findById(sportId)).willReturn(sport);

        mockMvc.perform(get("/api/sportTypes/{sportId}", sportId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sportId").value(sport.getSportId()))
                .andExpect(jsonPath("$.sportNm").value(sport.getSportNm()));
    }

    @Test
    @DisplayName("예외 처리 테스트")
    void handleIllegalArgumentException() throws Exception {
        Long wrongId = 999L;

        given(sportTypeService.findById(wrongId))
                .willThrow(new IllegalArgumentException("User not found"));

        mockMvc.perform(get("/api/sportTypes/{sportId}", wrongId))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User not found"));
    }
}