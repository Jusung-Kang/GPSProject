package com.jskang.backend.userM;

import com.jskang.backend.domain.UserM;
import com.jskang.backend.userM.dto.SaveUserMRequestDto;
import jdk.jfr.Frequency;
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

// 1. Mockito 프레임워크를 사용하여 순수 단위 테스트 진행
@ExtendWith(MockitoExtension.class)
class UserMServiceTest {

    @Mock // 가짜 Repository 생성
    private UserMRepository userMRepository;

    @InjectMocks // 가짜 Repository를 주입받을 진짜 Service
    private UserMService userMService;

    @Test
    @DisplayName("회원 생성(Create) 테스트")
    void createUserM() {
        // given
        SaveUserMRequestDto requestDto = new SaveUserMRequestDto();
        requestDto.setEmail("test@example.com");
        requestDto.setPhoneNumber("01012345678");

        // repository.save()가 호출되면 savedUser를 리턴하도록 가짜 행동 정의
        given(userMRepository.save(any(UserM.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

        // when
        userMService.createUserM(requestDto);

        ArgumentCaptor<UserM> captor = ArgumentCaptor.forClass(UserM.class);
        verify(userMRepository).save(captor.capture());

        UserM savedUserM = captor.getValue();

        // then
        assertThat(savedUserM.getEmail()).isEqualTo(requestDto.getEmail());
        assertThat(savedUserM.getPhoneNumber()).isEqualTo(requestDto.getPhoneNumber());
        assertThat(savedUserM.getUserId()).isNull();
    }

    @Test
    @DisplayName("회원 수정(Update) 성공 테스트")
    void updateUserM() {
        // given
        Long userId = 1L;

        SaveUserMRequestDto updateDto = new SaveUserMRequestDto();
        updateDto.setEmail("update@example.com");
        updateDto.setPhoneNumber("01099998888");

        // DB에 이미 존재하는 기존 유저 (Mock)
        UserM existingUser = UserM.builder()
                .userId(userId)
                .email("old@example.com")
                .phoneNumber("01000000000")
                .build();

        // findById 호출 시 existingUser 리턴
        given(userMRepository.findById(userId)).willReturn(Optional.of(existingUser));

        // when
        userMService.updateUserM(userId, updateDto);

        // then
        // 이제 DTO에 값이 들어갔으므로 updatedUser의 값도 변경되어 있어야 합니다.
        assertThat(existingUser.getEmail()).isEqualTo(updateDto.getEmail());
        assertThat(existingUser.getPhoneNumber()).isEqualTo(updateDto.getPhoneNumber());
    }

    @Test
    @DisplayName("회원 수정 실패 테스트 - 유저 없음")
    void updateUserM_Fail() {
        // given
        Long wrongId = 999L;
        SaveUserMRequestDto requestDto = new SaveUserMRequestDto();

        // findById 호출 시 빈 Optional 리턴 (유저 없음 시나리오)
        given(userMRepository.findById(wrongId)).willReturn(Optional.empty());

        // when & then
        // 예외가 발생하는지 검증
        assertThatThrownBy(() -> userMService.updateUserM(wrongId, requestDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("해당 아이디의 유저가 없습니다");
    }

    @Test
    @DisplayName("전체 조회(FindAll) 테스트")
    void findAll() {
        // given
        UserM u1 = UserM.builder().email("a@a.com")
                .phoneNumber("01011112222")
                .build();
        UserM u2 = UserM.builder().email("b@b.com")
                .phoneNumber("01012345678")
                .build();

        given(userMRepository.findAll()).willReturn(List.of(u1, u2));

        // when
        List<UserM> list = userMService.findAll();

        // then
        assertThat(list).hasSize(2);
        assertThat(list)
                .extracting("email",  "phoneNumber")
                .containsExactly(
                        tuple(u1.getEmail(),  u1.getPhoneNumber())
                        ,tuple(u2.getEmail(),  u2.getPhoneNumber())
                );
    }

    @Test
    @DisplayName("단건 조회(FindById) 테스트")
    void findById() {
        // given
        Long userId = 1L;
        UserM user = UserM.builder()
                .userId(userId)
                .email("test@a.com")
                .build();

        given(userMRepository.findById(userId)).willReturn(Optional.of(user));

        // when
        UserM result = userMService.findById(userId);

        // then
        assertThat(result.getUserId()).isEqualTo(userId);
        assertThat(result.getEmail()).isEqualTo(user.getEmail());

        verify(userMRepository).findById(userId);
    }

    @Test
    @DisplayName("단건 조회 실패 테스트 - 존재하지 않는 유저")
    void findById_Fail() {
        // given
        Long wrongId = 999L;

        // "없는 ID를 찾으면 빈(empty) 값을 줘라"라고 설정
        given(userMRepository.findById(wrongId)).willReturn(Optional.empty());

        // when & then
        // 예외가 발생하는지 검증
        assertThatThrownBy(() -> userMService.findById(wrongId))
                .isInstanceOf(IllegalArgumentException.class) // 발생하는 예외 클래스
                .hasMessageContaining("해당 유저를 찾을수 없습니다"); // 예외 메시지 검증

        // (선택) 호출은 되었는지 확인
        verify(userMRepository).findById(wrongId);
    }
}