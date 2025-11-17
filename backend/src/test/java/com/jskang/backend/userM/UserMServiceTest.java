package com.jskang.backend.userM;

import com.jskang.backend.domain.UserM;
import com.jskang.backend.userM.dto.SaveUserMRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

        // save 호출 시 리턴될 엔티티 정의
        UserM savedUser = UserM.builder()
                .id(1L)
                .email("test@example.com") // requestDto값과 같게 설정
                .phoneNumber("01012345678")
                .build();

        // repository.save()가 호출되면 savedUser를 리턴하도록 가짜 행동 정의
        given(userMRepository.save(any(UserM.class))).willReturn(savedUser);

        // when
        UserM result = userMService.createUserM(requestDto);

        // then
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getEmail()).isEqualTo("test@example.com");
        // 실제로 save가 1번 호출되었는지 검증
        verify(userMRepository).save(any(UserM.class));
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
                .id(userId)
                .email("old@example.com")
                .phoneNumber("01000000000")
                .build();

        // findById 호출 시 existingUser 리턴
        given(userMRepository.findById(userId)).willReturn(Optional.of(existingUser));

        // when
        UserM updatedUser = userMService.updateUserM(userId, updateDto);

        // then
        // 이제 DTO에 값이 들어갔으므로 updatedUser의 값도 변경되어 있어야 합니다.
        assertThat(updatedUser.getEmail()).isEqualTo("update@example.com");
        assertThat(updatedUser.getPhoneNumber()).isEqualTo("01099998888");
    }

    @Test
    @DisplayName("회원 수정 실패 테스트 - 유저 없음")
    void updateUserM_Fail() {
        // given
        Long wrongId = 999L;
        SaveUserMRequestDto dto = new SaveUserMRequestDto();

        // findById 호출 시 빈 Optional 리턴 (유저 없음 시나리오)
        given(userMRepository.findById(wrongId)).willReturn(Optional.empty());

        // when & then
        // 예외가 발생하는지 검증
        assertThatThrownBy(() -> userMService.updateUserM(wrongId, dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("해당 아이디의 유저가 없습니다");
    }

    @Test
    @DisplayName("전체 조회(FindAll) 테스트")
    void findAll() {
        // given
        UserM u1 = UserM.builder().email("a@a.com").build();
        UserM u2 = UserM.builder().email("b@b.com").build();

        given(userMRepository.findAll()).willReturn(List.of(u1, u2));

        // when
        List<UserM> list = userMService.findAll();

        // then
        assertThat(list).hasSize(2);
    }

    @Test
    @DisplayName("단건 조회(FindById) 테스트")
    void findById() {
        // given
        Long userId = 1L;
        UserM user = UserM.builder().id(userId).email("test@a.com").build();

        given(userMRepository.findById(userId)).willReturn(Optional.of(user));

        // when
        UserM result = userMService.findById(userId);

        // then
        assertThat(result.getId()).isEqualTo(userId);
        assertThat(result.getEmail()).isEqualTo("test@a.com");
    }
}