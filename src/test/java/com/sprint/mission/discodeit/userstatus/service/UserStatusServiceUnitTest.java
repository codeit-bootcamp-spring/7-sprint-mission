package com.sprint.mission.discodeit.userstatus.service;

import com.sprint.mission.discodeit.TestFixture;
import com.sprint.mission.discodeit.dto.request.userStatus.UserStatusCreateRequestDto;
import com.sprint.mission.discodeit.dto.response.userStatus.UserStatusDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.mapper.UserStatusMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.basic.BasicUserStatusService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
@DisplayName( "UserStatusService Unit Test")

public class UserStatusServiceUnitTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserStatusRepository userStatusRepository;

    @Mock
    private UserStatusMapper userStatusMapper;

    @InjectMocks
    private BasicUserStatusService userStatusService;

    private User user;
    private UUID userId ;
    private UserStatusDto userStatusDto;
    private UserStatus userStatus;

    @BeforeEach
    void setUp() {
        user = User.createUserFactory("user1","111@user","1234");
        userId = UUID.randomUUID();
        ReflectionTestUtils.setField(user,"id",userId);
        userStatusDto = new UserStatusDto(
                UUID.randomUUID(),
                userId,
                Instant.now()
        );
        userStatus = new UserStatus(user,Instant.now());

    }

    @Test
    @DisplayName("[정상 케이스] 유저 상태 변경 성공")
    void patchUserStatus_Success() {
        //given
        given(userRepository.findById(any(UUID.class))).willReturn(Optional.of(user));
        given(userStatusRepository.findAll())
                .willReturn( List.of(userStatus));
        given(userStatusRepository.save(any(UserStatus.class)))
                .willReturn(userStatus);
        given(userStatusMapper.toDto(any(UserStatus.class)))
        .willReturn(userStatusDto);
        given(userRepository.save(any(User.class)))
        .willReturn(user);

        //when
        UserStatusDto response = userStatusService.patchUserStatus(userId
                , TestFixture.userStatusPatchFactory());

        assertThat(response).isEqualTo(userStatusDto);
        assertThat(response.userId()).isEqualTo(userId);

        //then
        then(userRepository).should(times(1)).findById(any(UUID.class));
        then(userStatusRepository).should(times(1)).findAll();
        then(userStatusRepository).should(times(1)).save(any(UserStatus.class));
        then(userStatusMapper).should(times(1)).toDto(any(UserStatus.class));
        then(userRepository).should(times(1)).save(any(User.class));


    }

    @Test
    @DisplayName("[정상 케이스] 유저 상태 생성 성공")
    void createUserStatus_Success() {

        given(userRepository.findById(any(UUID.class)))
                .willReturn(Optional.of(user));
        given(userStatusMapper.toDto(any(UserStatus.class)))
                .willReturn(userStatusDto);
        given(userStatusRepository.save(any(UserStatus.class)))
                .willReturn(new UserStatus(null,null));

        UserStatusDto response = userStatusService.createUserStatus(
                new UserStatusCreateRequestDto(
                        userId,
                        Instant.now()
                )
        );
        assertThat(response).isEqualTo(userStatusDto);
        assertThat(response.userId()).isEqualTo(userId);

        then(userRepository).should(times(1)).findById(any(UUID.class));
        then(userStatusRepository).should(times(1)).save(any(UserStatus.class));
        then(userStatusMapper).should(times(1)).toDto(any(UserStatus.class));

    }

    @Test
    @DisplayName("[정상 케이스] 유저 상태 삭제")
    void deleteUserStatus_Success() {
        doNothing().when(userStatusRepository).deleteById(any(UUID.class));
        userStatusService.deleteUserStatus(userId);
        then(userStatusRepository).should(times(1))
                .deleteById(any(UUID.class));
    }
}
