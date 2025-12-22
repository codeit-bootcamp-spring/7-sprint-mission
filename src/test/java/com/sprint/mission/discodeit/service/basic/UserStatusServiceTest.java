package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.userstatus.request.UpdateUserStatusRequestDto;
import com.sprint.mission.discodeit.dto.userstatus.response.UserStatusResponseDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.global.exception.userstatus.UserStatusNotFoundException;
import com.sprint.mission.discodeit.mapper.UserStatusMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("userstatus 서비스 단위 테스트")
class UserStatusServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserStatusRepository userStatusRepository;

    @Mock
    private UserStatusMapper userStatusMapper;

    @InjectMocks
    private BasicUserStatusService userStatusService;

    @Test
    @DisplayName("정상적으로 userId로 userstatus를 수정할 수 있다.")
    void updateUserStatus_Success() {
        // given
        Instant newLastActiveAt = Instant.now();
        UUID userId = UUID.randomUUID();

        User user = new User(
                "test",
                "test@naver.com",
                "test1234",
                null
        );
        ReflectionTestUtils.setField(user, "id", userId);

        UserStatus userStatus = new UserStatus(user);

        when(userStatusRepository.findByUserId(user.getId()))
                .thenReturn(Optional.of(userStatus));

        when(userStatusRepository.save(userStatus))
                .thenAnswer(i -> i.getArgument(0));

        when(userStatusMapper.toResponseDto(userStatus))
                .thenReturn(mock(UserStatusResponseDto.class));

        UpdateUserStatusRequestDto request = new UpdateUserStatusRequestDto(
                newLastActiveAt
        );

        // when
        UserStatusResponseDto response = userStatusService.updateByUserId(user.getId(), request);

        // then
        assertThat(response).isNotNull();

        verify(userStatusRepository, times(1)).findByUserId(user.getId());
        verify(userStatusRepository, times(1)).save(userStatus);
        verify(userStatusMapper, times(1)).toResponseDto(userStatus);
    }

    @Test
    @DisplayName("존재하지 않는 userId는 userstatus를 수정할 수 없다.")
    void updateUserStatus_WhenUserDoesNotExist_Fail() {
        // given
        Instant newLastActiveAt = Instant.now();

        when(userStatusRepository.findByUserId(any(UUID.class)))
                .thenReturn(Optional.empty());

        UpdateUserStatusRequestDto request = new UpdateUserStatusRequestDto(
                newLastActiveAt
        );

        // when & then
        assertThatThrownBy(() -> userStatusService.updateByUserId(UUID.randomUUID(), request))
                .isInstanceOf(UserStatusNotFoundException.class);

        verify(userStatusRepository, times(1)).findByUserId(any(UUID.class));
        verify(userStatusRepository, never()).save(any(UserStatus.class));
        verify(userStatusMapper, never()).toResponseDto(any(UserStatus.class));
    }

}