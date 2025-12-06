package com.jskang.backend.PositionM;

import com.jskang.backend.PositionM.dto.PositionMResponseDto;
import com.jskang.backend.PositionM.dto.SavePositionMRequestDto;
import com.jskang.backend.domain.PositionM;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PositionMServiceTest {

    @Mock
    private PositionMRepository positionMRepository;

    @InjectMocks
    private PositionMService positionMService;

    @Test
    @DisplayName("생성")
    void create() {

        SavePositionMRequestDto request = new SavePositionMRequestDto();
        request.setPositionNm("test");

        PositionM positionM = PositionM.builder()
                .positionNm("test")
                .build();

        positionMService.create(request);

        ArgumentCaptor<PositionM> captor = ArgumentCaptor.forClass(PositionM.class);
        verify(positionMRepository).save(captor.capture());

        PositionM savedEntity = captor.getValue();

        assertThat(savedEntity.getPositionNm()).isEqualTo("test");

    }

    @Test
    @DisplayName("업데이트")
    void update() {
        SavePositionMRequestDto request = new SavePositionMRequestDto();
        request.setPositionNm("test2");

        PositionM positionM = PositionM.builder()
                .positionNm("test")
                .build();

        positionMService.create(request);

        given(positionMRepository.findById(positionM.getPositionId()))
                .willReturn(Optional.of(positionM));

        positionMService.update(positionM.getPositionId(), request);

        assertThat(positionM.getPositionNm()).isEqualTo("test2");

    }

    @Test
    @DisplayName("업데이트 실패")
    void update_fail() {

        Long wrongId = 999L;

        SavePositionMRequestDto request = new SavePositionMRequestDto();
        given(positionMRepository.findById(wrongId))
                .willReturn(Optional.empty());

        assertThatThrownBy(() -> positionMService.update(wrongId, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("해당 포지션이 없습니다.");

    }

    @Test
    @DisplayName("전체조회")
    void findAll() {

        PositionM positionM1 = PositionM.builder()
                .positionNm("test")
                .build();

        PositionM positionM2 = PositionM.builder()
                .positionNm("test2")
                .build();

        given(positionMRepository.findAll())
                .willReturn(List.of(positionM1, positionM2));

        List<PositionMResponseDto> list = positionMService.findAll();

        assertThat(list).hasSize(2);
        assertThat(list.get(0).getPositionNm()).isEqualTo("test");
        assertThat(list.get(1).getPositionNm()).isEqualTo("test2");

    }

    @Test
    @DisplayName("아이디 조회")
    void findById() {

        Long positionId = 100L;

        PositionM positionM = PositionM.builder()
                .positionNm("test")
                .build();

        given(positionMRepository.findById(positionId))
                .willReturn(Optional.of(positionM));

        PositionMResponseDto response = positionMService.findById(positionId);

        assertThat(response.getPositionNm()).isEqualTo("test");

    }

    @Test
    @DisplayName("아이디 조회 실패")
    void findById_fail() {

        Long wrongId = 999L;

        PositionM positionM = PositionM.builder()
                .positionNm("test")
                .build();

        given(positionMRepository.findById(wrongId))
                .willReturn(Optional.empty());

        assertThatThrownBy(() -> positionMService.findById(wrongId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("해당 포지션이 없습니다.");

        verify(positionMRepository).findById(wrongId);

    }
}