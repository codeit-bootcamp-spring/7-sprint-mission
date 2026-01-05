package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.common.exception.user.UserAlreadyExistsException;
import com.sprint.mission.discodeit.common.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.dto.request.user.UserCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.user.UserUpdateRequestDto;
import com.sprint.mission.discodeit.dto.response.user.UserResponseDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class BasicUserServiceTest {
    @Mock
    UserRepository userRepository;

    @Mock
    UserMapper userMapper;

    @InjectMocks
    BasicUserService userService;

    @Mock
    UserStatusRepository userStatusRepository;

    @Mock
    BinaryContentService binaryContentService;

    @Mock
    BinaryContentRepository binaryContentRepository;

    @Test
    @DisplayName("create 성공: 중복 확인, 프로필 없을 때 유저 생성 + userStatus 생성")
    void create_success_withoutProfile() {
        // given
        UserCreateRequestDto userCreateRequestDto
                = new UserCreateRequestDto("이형일", "qqq123", "dlguddlf@naver.com");

        given(userRepository.existsByUsername("이형일")).willReturn(false);
        given(userRepository.existsByEmail("dlguddlf@naver.com")).willReturn(false);

        User user = new User("이형일", "qqq123", "dlguddlf@naver.com", null);
        setId(user, UUID.randomUUID());
        given(userRepository.save(any(User.class))).willReturn(user);

        UserResponseDto responseDto = mock(UserResponseDto.class);
        given(userMapper.toDto(user, true)).willReturn(responseDto);

        
        // when
        UserResponseDto created = userService.create(userCreateRequestDto, null);

        // then
        assertThat(created).isSameAs(responseDto);

        then(userRepository).should().existsByUsername("이형일");
        then(userRepository).should().existsByEmail("dlguddlf@naver.com");
        then(userRepository).should().save(any(User.class));

        then(userStatusRepository).should().save(any());
        then(userMapper).should().toDto(user, true);
        then(binaryContentService).shouldHaveNoInteractions();
        
    }

    @Test
    @DisplayName("create 실패: username 중복이면 exception 발생")
    void create_fail_duplicateUsername() {
        // given
        UserCreateRequestDto userCreateRequestDto
                = new UserCreateRequestDto("이형일", "qqq123", "dlguddlf@naver.com");
        given(userRepository.existsByUsername("이형일")).willReturn(true);

        // when & then
        assertThatThrownBy(() -> userService.create(userCreateRequestDto, null))
                .isInstanceOf(UserAlreadyExistsException.class);

        then(userRepository).should().existsByUsername("이형일");
        then(userRepository).should(never()).existsByEmail(anyString());
        then(userRepository).should(never()).save(any());
        then(userStatusRepository).shouldHaveNoInteractions();

    }
/*
    Fail 원인 : 기존 서비스 로직에서는 db에 저장된 유저는 이미 id값이 있기 때문에
    단위 테스트에서 mock이 반환하는 user에는 id값이 세팅되지 않음.
    그래서 서비스에서 update후 저장된 유저의 id를 online 상태값을 가져오기 위해 사용 시 NPE가 발생.
    유저 도메인에는 setter가 없기 때문에 id를 직접 셋팅이 되지 않음.
    ReflectionTestUtils를 사용하면 field에 값을 넣을 수 있으니 이 방법을 사용.
 */
    @Test
    @DisplayName("update 성공: username/email/password 변경")
    void update_success_changeField() {
        // given
        UUID userId = UUID.randomUUID();
        User user = new User("이형일", "qqq123", "dlguddlf@naver.com", null);
        setId(user, userId);

        UserUpdateRequestDto userUpdateRequestDto
                = new UserUpdateRequestDto("이형일2", "dlguddlf@gmail.com", "qqqqqq123");

        UserResponseDto userResponseDto = new UserResponseDto(
                userId, "이형일2", "dlguddlf@gmail.com", null, false);

        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(userRepository.findByUsername("이형일2")).willReturn(List.of());
        given(userRepository.findByEmail("dlguddlf@gmail.com")).willReturn(Optional.empty());
        given(userRepository.save(any(User.class))).willAnswer(invo -> invo.getArgument(0));
        given(userStatusRepository.findByUserId(userId)).willReturn(Optional.empty());

        given(userMapper.toDto(any(User.class), eq(false))).willReturn(userResponseDto);


        // when
        UserResponseDto updated = userService.update(userId, userUpdateRequestDto, null);

        // then
        assertThat(updated).isSameAs(userResponseDto);
        assertThat(user.getUsername()).isEqualTo("이형일2");
        assertThat(user.getEmail()).isEqualTo("dlguddlf@gmail.com");

        then(userRepository).should().findById(userId);
        then(userRepository).should().save(user);
        then(userMapper).should().toDto(same(user), eq(false));


    }

    @Test
    @DisplayName("update 실패: email이 중복이면 exception 발생")
    void update_fail_duplicateEmail() {
        // given
        UUID userId = UUID.randomUUID();
        User user1 = new User("이형일", "abcd1234", "dlguddlf@naver.com", null);
        setId(user1, userId);

        UserUpdateRequestDto userUpdateRequestDto
                = new UserUpdateRequestDto(null, "dlguddlf3@gmail.com", null);

        given(userRepository.findById(userId)).willReturn(Optional.of(user1));

        User user2 = new User("이형일3", "efgh1234", "dlguddlf3@gmail.com", null);
        setId(user2, UUID.randomUUID());

        given(userRepository.findById(userId)).willReturn(Optional.of(user1));
        given(userRepository.findByEmail("dlguddlf3@gmail.com")).willReturn(Optional.of(user2));

        // when & then
        assertThatThrownBy(() -> userService.update(userId, userUpdateRequestDto, null))
                .isInstanceOf(UserAlreadyExistsException.class);

        then(userRepository).should().findByEmail("dlguddlf3@gmail.com");
        then(userRepository).should(never()).save(any());
        then(userMapper).shouldHaveNoInteractions();

    }
    @Test
    @DisplayName("delete 성공: 유저 삭제 성공")
    void delete_success() {
        // given
        UUID userId = UUID.randomUUID();
        User user = new User("이형일", "abcde12345", "dlguddlf@naver.com", null);

        given(userRepository.findById(userId)).willReturn(Optional.of(user));

        // when
        boolean deleted = userService.delete(userId);

        // then
        assertThat(deleted).isTrue();
        then(userRepository).should().findById(userId);
        then(userRepository).should().delete(user);


    }

    @Test
    @DisplayName("delete 실패: 삭제할 유저가 없다면 exception 발생")
    void delete_fail_userNotFound() {
        // given
        UUID userId = UUID.randomUUID();
        given(userRepository.findById(userId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> userService.delete(userId))
                .isInstanceOf(UserNotFoundException.class);
        then(userRepository).should().findById(userId);
        then(userRepository).should(never()).delete(any());

    }

    private static void setId(User user, UUID userId) {
        ReflectionTestUtils.setField(user, "id", userId);
    }
}
