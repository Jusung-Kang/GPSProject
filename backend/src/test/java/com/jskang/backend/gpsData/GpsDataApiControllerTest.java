package com.jskang.backend.gpsData;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jskang.backend.availableSports.dto.AvailableSportsResponseDto;
import com.jskang.backend.availableSports.dto.SaveAvailableSportsRequestDto;
import com.jskang.backend.domain.*;
import com.jskang.backend.gpsData.dto.GpsDataResponseDto;
import com.jskang.backend.gpsData.dto.SaveGpsDataRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GpsDataApiController.class)
class GpsDataApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GpsDataService gpsDataService;

    @Autowired
    private ObjectMapper objectMapper;

    private ExerciseHistory savedHistory;
    private UserM savedUser;
    private SportType savedSportType;

    @BeforeEach
    public void setup() {


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
    void create() throws Exception {

        SaveGpsDataRequestDto request = new SaveGpsDataRequestDto();
        request.setLongitude(new BigDecimal("1.1"));
        request.setLatitude(new BigDecimal("2.2"));
        request.setAltitude(new BigDecimal("3.3"));
        request.setSpeed(new BigDecimal("4.4"));
        request.setDistance(new BigDecimal("5.5"));
        request.setGpsSeq(1);

        GpsData mockEntity = GpsData.builder()
                .longitude(request.getLongitude())
                .latitude(request.getLatitude())
                .altitude(request.getAltitude())
                .speed(request.getSpeed())
                .distance(request.getDistance())
                .exerciseHistory(savedHistory)
                .build();

        GpsDataResponseDto response = new GpsDataResponseDto(mockEntity);

        given(gpsDataService.create(eq(savedHistory.getHistoryId()), any(SaveGpsDataRequestDto.class)))
                .willReturn(response);

        mockMvc.perform(post("/api/history/{historyId}/gps", savedHistory.getHistoryId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.longitude").value(mockEntity.getLongitude()))
                .andExpect(jsonPath("$.latitude").value(mockEntity.getLatitude()))
                .andExpect(jsonPath("$.altitude").value(mockEntity.getAltitude()))
                .andExpect(jsonPath("$.speed").value(mockEntity.getSpeed()));

    }

    @Test
    @DisplayName("업데이트")
    void update() throws Exception {

        Long gpsId = 100L;


        SaveGpsDataRequestDto request = new SaveGpsDataRequestDto();
        request.setMaxDistance(new BigDecimal("2.5"));
        request.setMaxSpeed(new BigDecimal("3.5"));

        GpsData mockEntity = GpsData.builder()
                .gpsId(100L)
                .longitude(request.getLongitude())
                .latitude(request.getLatitude())
                .altitude(request.getAltitude())
                .speed(request.getSpeed())
                .distance(request.getDistance())
                .exerciseHistory(savedHistory)
                .build();

        GpsDataResponseDto response = new GpsDataResponseDto(mockEntity);

        given(gpsDataService.update(eq(gpsId), any(SaveGpsDataRequestDto.class)))
                .willReturn(response);

        mockMvc.perform(put("/api/history/gps/{gpsId}", gpsId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.maxDistance").value(mockEntity.getMaxDistance()))
                .andExpect(jsonPath("$.maxSpeed").value(mockEntity.getMaxSpeed()));

    }

    @Test
    @DisplayName("히스토리ID로 조회")
    void findAllByHistoryId() throws Exception {

        GpsData mockEntity1 = GpsData.builder()
                .longitude(new BigDecimal("1.1"))
                .latitude(new BigDecimal("2.2"))
                .altitude(new  BigDecimal("3.3"))
                .maxSpeed(new  BigDecimal("1.1"))
                .maxDistance(new  BigDecimal("1.1"))
                .exerciseHistory(savedHistory)
                .build();

        GpsData mockEntity2 = GpsData.builder()
                .longitude(new BigDecimal("1.1"))
                .latitude(new BigDecimal("2.2"))
                .altitude(new  BigDecimal("3.3"))
                .maxSpeed(new  BigDecimal("1.1"))
                .maxDistance(new  BigDecimal("1.1"))
                .exerciseHistory(savedHistory)
                .build();

        List<GpsData> list = List.of(mockEntity1, mockEntity2);

        List<GpsDataResponseDto> response = GpsDataResponseDto.from(list);

        given(gpsDataService.findAllByHistoryId(eq(savedHistory.getHistoryId())))
                .willReturn(response);

        mockMvc.perform(get("/api/history/{historyId}/gps", savedHistory.getHistoryId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(list)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].longitude").value(mockEntity1.getLongitude()))
                .andExpect(jsonPath("$[0].latitude").value(mockEntity1.getLatitude()))
                .andExpect(jsonPath("$[0].altitude").value(mockEntity1.getAltitude()))
                .andExpect(jsonPath("$[1].longitude").value(mockEntity2.getLongitude()))
                .andExpect(jsonPath("$[1].latitude").value(mockEntity2.getLatitude()))
                .andExpect(jsonPath("$[1].altitude").value(mockEntity2.getAltitude()));



    }
}