package com.jskang.backend.userM;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jskang.backend.domain.UserM;
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

@WebMvcTest(UserMApiController.class)
class UserMApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // @MockBean 대신 @MockitoBean 사용 (Spring Boot 3.4+)
    @MockitoBean
    private UserMService userMService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("유저 생성(POST) 테스트")
    void createUser() throws Exception {
        // given
        SaveUserMRequestDto requestDto = new SaveUserMRequestDto();
        requestDto.setEmail("test@example.com");
        requestDto.setPhoneNumber("01012345678");

        UserM createdUser = UserM.builder()
                .userId(1L)
                .email("test@example.com")
                .phoneNumber("01012345678")
                .build();

        given(userMService.createUserM(any(SaveUserMRequestDto.class)))
                .willReturn(createdUser);

        // when & then
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.phoneNumber").value("01012345678"));
    }

    @Test
    @DisplayName("유저 수정(PUT) 테스트")
    void updateUser() throws Exception {
        // given
        Long userId = 1L;
        SaveUserMRequestDto requestDto = new SaveUserMRequestDto();
        requestDto.setEmail("update@example.com");
        requestDto.setPhoneNumber("01099998888");

        UserM updatedUser = UserM.builder()
                .userId(userId)
                .email("update@example.com")
                .phoneNumber("01099998888")
                .build();

        given(userMService.updateUserM(eq(userId), any(SaveUserMRequestDto.class)))
                .willReturn(updatedUser);

        // when & then
         mockMvc.perform(put("/api/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("update@example.com"));
    }

    @Test
    @DisplayName("전체 유저 조회(GET) 테스트")
    void getAllUserM() throws Exception {
        List<UserM> users = List.of(
                UserM.builder().email("a@a.com").build(),
                UserM.builder().email("b@b.com").build()
        );

        given(userMService.findAll()).willReturn(users);

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].email").value("a@a.com"));
    }

    @Test
    @DisplayName("단건 유저 조회(GET) 테스트")
    void getUserMById() throws Exception {
        Long userId = 1L;
        UserM user = UserM.builder().userId(userId).email("target@a.com").build();

        given(userMService.findById(userId)).willReturn(user);

        mockMvc.perform(get("/api/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("target@a.com"));
    }

    @Test
    @DisplayName("예외 처리 테스트")
    void handleIllegalArgumentException() throws Exception {
        Long wrongId = 999L;

        given(userMService.findById(wrongId))
                .willThrow(new IllegalArgumentException("User not found"));

        mockMvc.perform(get("/api/users/{id}", wrongId))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User not found"));
    }
}