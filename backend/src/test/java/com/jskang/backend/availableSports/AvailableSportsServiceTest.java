package com.jskang.backend.availableSports;

import com.jskang.backend.availableSports.dto.SaveAvailableSportsRequestDto;
import com.jskang.backend.domain.AvailableSports;
import com.jskang.backend.domain.AvailableSportsId;
import com.jskang.backend.domain.SportType;
import com.jskang.backend.domain.UserM;
import com.jskang.backend.sportType.SportTypeRepository;
import com.jskang.backend.userM.UserMRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor; // 필수 import
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AvailableSportsServiceTest {

    @Mock
    private UserMRepository userMRepository;
    @Mock
    private AvailableSportsRepository availableSportsRepository;
    @Mock
    private SportTypeRepository sportTypeRepository;

    @InjectMocks
    private AvailableSportsService availableSportsService;

    @Test
    @DisplayName("가능한 운동 생성")
    void createAvailableSports() {
        // 1. [Given] 데이터 준비
        Long userId = 1L;
        // DTO 세팅 (검증의 기준값이 됨)
        SaveAvailableSportsRequestDto requestDto = new SaveAvailableSportsRequestDto();
        requestDto.setSportId(10L);
        requestDto.setLevel("기본");
        requestDto.setPositionCd("포드");

        // 연관 관계 조회를 위한 가짜 객체 (ID만 있으면 됨)
        UserM userM = UserM.builder().userId(userId).build();
        SportType sportType = SportType.builder().sportId(10L).build();

        // 2. [Given] Mocking (Stubbing)
        given(userMRepository.findById(userId)).willReturn(Optional.of(userM));
        given(sportTypeRepository.findById(10L)).willReturn(Optional.of(sportType));

        // [핵심] save 호출 시 "들어온 객체를 그대로 리턴"하도록 설정
        // 이렇게 하면 테스트용 Entity를 따로 만들 필요가 없습니다.
        given(availableSportsRepository.save(any(AvailableSports.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

        // 3. [When] 실행
        availableSportsService.createAvailableSports(userId, requestDto);

        // 4. [Then] ArgumentCaptor로 낚아채서 검증
        // 캡쳐 도구 생성
        ArgumentCaptor<AvailableSports> captor = ArgumentCaptor.forClass(AvailableSports.class);

        // 리포지토리의 save 메서드에 들어간 인자를 캡쳐!
        verify(availableSportsRepository).save(captor.capture());

        // 캡쳐된 객체 꺼내기
        AvailableSports savedEntity = captor.getValue();

        // 5. [Then] 값 검증 (DTO 값 vs 캡쳐된 Entity 값)
        assertThat(savedEntity.getPk().getUserId()).isEqualTo(userId);
        assertThat(savedEntity.getPk().getSportId()).isEqualTo(10L);
        assertThat(savedEntity.getLevel()).isEqualTo(requestDto.getLevel());      // "기본"
        assertThat(savedEntity.getPositionCd()).isEqualTo(requestDto.getPositionCd()); // "포드"
    }

    @Test
    @DisplayName("가능한 운동 업데이트")
    void updateAvailableSports() {

        Long userId = 1L;
        Long sportId = 10L;

        SaveAvailableSportsRequestDto requestDto = new SaveAvailableSportsRequestDto();
        requestDto.setLevel("프로");
        requestDto.setPositionCd("포드");

        AvailableSports existing = AvailableSports.builder()
                .pk(new AvailableSportsId(userId, sportId))
                .level("아마추어")
                .positionCd("수비")
                .build();

        given(availableSportsRepository.findById(new AvailableSportsId(userId, sportId))).willReturn(Optional.of(existing));

        availableSportsService.updateAvailableSports(userId, sportId, requestDto);

        assertThat(existing.getLevel()).isEqualTo(requestDto.getLevel());
        assertThat(existing.getPositionCd()).isEqualTo(requestDto.getPositionCd());
    }

    @Test
    @DisplayName("가능한 운동 업데이트 실패")
    void updateAvailableSports_Fail(){
        Long wrongUserId = 999L;
        Long wrongSportId = 9999L;

        SaveAvailableSportsRequestDto requestDto = new SaveAvailableSportsRequestDto();

        AvailableSportsId wrongPk = new AvailableSportsId(wrongUserId, wrongSportId);

        given(availableSportsRepository.findById(wrongPk)).willReturn(Optional.empty());

        assertThatThrownBy(() -> availableSportsService.updateAvailableSports(wrongUserId, wrongSportId, requestDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("해당 정보를 찾을수 없습니다.");
    }

    @Test
    @DisplayName("전체 조회(FindAll) 테스트")
    void findAll() {

        // given
        AvailableSports sport1 = AvailableSports.builder()
                .level("1")
                .build();
        AvailableSports sport2 = AvailableSports.builder()
                .level("2")
                .build();

        given(availableSportsRepository.findAll()).willReturn(List.of(sport1, sport2));

        List<AvailableSports> list = availableSportsService.findAll();

        assertThat(list).hasSize(2);
        assertThat(list).extracting("level")
                .containsExactly("1", "2");
    }

    @Test
    @DisplayName("단건 조회(FindById) 테스트")
    void findById() {

        Long userId1 = 1L;
        Long sportId1 = 10L;


        AvailableSportsId pk1 = new AvailableSportsId(userId1, sportId1);

        AvailableSports sport1 = AvailableSports.builder()
                .pk(pk1)
                .level("1")
                .build();

        given(availableSportsRepository.findById(pk1)).willReturn(Optional.of(sport1));

        AvailableSports result1 = availableSportsService.findById(userId1, sportId1);

        assertThat(result1.getLevel()).isEqualTo("1");

        verify(availableSportsRepository).findById(pk1);
    }

    @Test
    @DisplayName("단건 조회(FindById) 실패 테스트")
    void findById_Fail() {

        Long wrongUserId1 = 99L;
        Long wrongSportId1 = 999L;

        AvailableSportsId wrongPk1 = new AvailableSportsId(wrongUserId1, wrongSportId1);

        given(availableSportsRepository.findById(wrongPk1)).willReturn(Optional.empty());

        assertThatThrownBy(() -> availableSportsService.findById(wrongUserId1, wrongSportId1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("해당 스포츠가 존재하지 않습니다. id="+wrongSportId1);

        verify(availableSportsRepository).findById(wrongPk1);
    }
}