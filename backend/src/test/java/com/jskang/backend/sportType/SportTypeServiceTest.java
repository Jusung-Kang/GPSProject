package com.jskang.backend.sportType;

import com.jskang.backend.domain.SportType;
import com.jskang.backend.domain.UserM;
import com.jskang.backend.sportType.dto.SaveSportTypeRequestDto;
import com.jskang.backend.userM.UserMRepository;
import com.jskang.backend.userM.UserMService;
import com.jskang.backend.userM.dto.SaveUserMRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SportTypeServiceTest {

    @Mock // 가짜 Repository 생성
    private SportTypeRepository sportTypeRepository;

    @InjectMocks // 가짜 Repository를 주입받을 진짜 Service
    private SportTypeService sportTypeService;

    @Test
    @DisplayName("스포츠 생성(Create) 테스트")
    void createSportType() {
        // given
        SaveSportTypeRequestDto requestDto = new SaveSportTypeRequestDto();
        requestDto.setSportId(1L);
        requestDto.setSportNm("test@example.com");

        // repository.save()가 호출되면 savedUser를 리턴하도록 가짜 행동 정의
        given(sportTypeRepository.save(any(SportType.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

        // when
        sportTypeService.createSportType(requestDto);

        ArgumentCaptor<SportType> captor = ArgumentCaptor.forClass(SportType.class);
        verify(sportTypeRepository).save(captor.capture());

        SportType savedSportType = captor.getValue();

        // then
        assertThat(savedSportType.getSportId()).isEqualTo(requestDto.getSportId());
        assertThat(savedSportType.getSportNm()).isEqualTo(requestDto.getSportNm());
    }
    
    @Test
    @DisplayName("스포츠 수정(Update) 성공 테스트")
    void updateSportType() {
        // given
        Long sportId = 1L;

        SaveSportTypeRequestDto updateDto = new SaveSportTypeRequestDto();
        updateDto.setSportNm("야구");

        // DB에 이미 존재하는 기존 유저 (Mock)
        SportType existingSport = SportType.builder()
                .sportId(sportId)
                .sportNm("축구")
                .build();

        // findById 호출 시 existingSport 리턴
        given(sportTypeRepository.findById(sportId)).willReturn(Optional.of(existingSport));

        // when
        sportTypeService.updateSportType(sportId, updateDto);

        // then
        // 이제 DTO에 값이 들어갔으므로 updateDto 값도 변경되어 있어야 합니다.
        assertThat(existingSport.getSportNm()).isEqualTo(updateDto.getSportNm());
    }

    @Test
    @DisplayName("전체 조회(FindAll) 테스트")
    void findSportTypeAll() {
        // given
        SportType sport1 = SportType.builder()
                .sportId(1L)
                .sportNm("축구")
                .build();

        SportType sport2 = SportType.builder()
                .sportId(2L)
                .sportNm("축구")
                .build();

        given(sportTypeRepository.findAll()).willReturn(List.of(sport1, sport2));

        // when
        List<SportType> list = sportTypeRepository.findAll();

        // then
        assertThat(list).hasSize(2);
        assertThat(list)
                .extracting("sportId",  "sportNm")
                .containsExactly(
                        tuple(sport1.getSportId(),  sport1.getSportNm())
                        , tuple(sport2.getSportId(),  sport2.getSportNm())
                );
    }

    @Test
    @DisplayName("단건 조회(FindById) 테스트")
    void findById() {
        // given
        Long sportId = 2L;
        SportType sport = SportType.builder()
                .sportId(sportId)
                .sportNm("축구")
                .build();

        given(sportTypeRepository.findById(sportId)).willReturn(Optional.of(sport));

        // when
        SportType result = sportTypeService.findById(sportId);

        // then
        assertThat(result.getSportId()).isEqualTo(sportId);
        assertThat(result.getSportNm()).isEqualTo(sport.getSportNm());

        verify(sportTypeRepository).findById(sportId);
    }

    @Test
    @DisplayName("단건 조회 실패 테스트 - 존재하지 않는 유저")
    void findById_Fail() {
        // given
        Long wrongId = 999L;

        // "없는 ID를 찾으면 빈(empty) 값을 줘라"라고 설정
        given(sportTypeRepository.findById(wrongId)).willReturn(Optional.empty());

        // when & then
        // 예외가 발생하는지 검증
        assertThatThrownBy(() -> sportTypeService.findById(wrongId))
                .isInstanceOf(IllegalArgumentException.class) // 발생하는 예외 클래스
                .hasMessageContaining("해당 운동이 존재하지 않습니다. id=" + wrongId); // 예외 메시지 검증

        // (선택) 호출은 되었는지 확인
        verify(sportTypeRepository).findById(wrongId);
    }
}