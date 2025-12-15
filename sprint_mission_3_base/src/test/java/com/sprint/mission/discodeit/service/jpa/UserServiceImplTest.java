package com.sprint.mission.discodeit.service.jpa;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.user.UserAlreadyExistsException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock private UserRepository userRepository;
    @Mock private UserStatusRepository userStatusRepository;
    @Mock private BinaryContentRepository binaryRepo;
    @Mock private BinaryContentStorage storage;

    @InjectMocks private UserServiceImpl userService;

    @Test
    @DisplayName("create 성공: 프로필 없음 -> User 저장 + UserStatus 저장, Binary 저장/스토리지 저장 없음")
    void create_success_withoutProfile() {
        UserCreateRequest req = new UserCreateRequest("taehun", "taehun@test.com", "password1234");

        given(userRepository.existsByEmail(req.email())).willReturn(false);
        given(userRepository.save(any(User.class))).willAnswer(invocation -> {
            User u = invocation.getArgument(0);
            ReflectionTestUtils.setField(u, "id", UUID.randomUUID());
            return u;
        });

        UserDto result = userService.create(req, Optional.empty());

        assertThat(result).isNotNull();
        then(userRepository).should().existsByEmail(req.email());
        then(userRepository).should().save(any(User.class));
        then(userStatusRepository).should().save(any()); // new UserStatus(user, now)

        then(binaryRepo).shouldHaveNoInteractions();
        then(storage).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("create 실패: 이메일 중복 -> UserAlreadyExistsException")
    void create_fail_emailAlreadyExists() {
        UserCreateRequest req = new UserCreateRequest("taehun", "dup@test.com", "password1234");
        given(userRepository.existsByEmail(req.email())).willReturn(true);

        assertThatThrownBy(() -> userService.create(req, Optional.empty()))
                .isInstanceOf(UserAlreadyExistsException.class);

        then(userRepository).should().existsByEmail(req.email());
        then(userRepository).should(never()).save(any());
        then(userStatusRepository).shouldHaveNoInteractions();
        then(binaryRepo).shouldHaveNoInteractions();
        then(storage).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("create 성공: 프로필 있음 -> Binary 저장 + storage.put 호출")
    void create_success_withProfile() {
        UserCreateRequest req = new UserCreateRequest("taehun", "taehun2@test.com", "password1234");
        byte[] bytes = "hello".getBytes();
        BinaryContentCreateRequest profileReq = new BinaryContentCreateRequest("p.png", "image/png", bytes);

        given(userRepository.existsByEmail(req.email())).willReturn(false);
        given(userRepository.save(any(User.class))).willAnswer(invocation -> {
            User u = invocation.getArgument(0);
            ReflectionTestUtils.setField(u, "id", UUID.randomUUID());
            return u;
        });

        given(binaryRepo.save(any(BinaryContent.class))).willAnswer(invocation -> {
            BinaryContent b = invocation.getArgument(0);
            // storage.put(binary.getId(), ...) 때문에 id 세팅
            ReflectionTestUtils.setField(b, "id", UUID.randomUUID());
            return b;
        });

        UserDto result = userService.create(req, Optional.of(profileReq));

        assertThat(result).isNotNull();
        then(binaryRepo).should().save(any(BinaryContent.class));

        then(storage).should().put(nullable(UUID.class), eq(bytes));
        then(userStatusRepository).should().save(any());
    }

    @Test
    @DisplayName("update 성공: 유저 존재 + 프로필 없음 -> UserRepository.save 호출 없이 값 변경(더티체킹 가정)")
    void update_success_withoutProfile() {

        UUID userId = UUID.randomUUID();
        User user = new User("old", "old@test.com", "oldpass1234", null);
        ReflectionTestUtils.setField(user, "id", userId);

        UserUpdateRequest req = mock(UserUpdateRequest.class);
        given(req.newUsername()).willReturn("new");
        given(req.newEmail()).willReturn("new@test.com");
        given(req.newPassword()).willReturn("newpass1234");

        given(userRepository.findById(userId)).willReturn(Optional.of(user));


        UserDto result = userService.update(userId, req, Optional.empty());


        assertThat(result).isNotNull();
        then(userRepository).should().findById(userId);
        then(userRepository).should(never()).save(any());

        then(binaryRepo).shouldHaveNoInteractions();
        then(storage).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("update 실패: 유저 없음 -> UserNotFoundException")
    void update_fail_userNotFound() {

        UUID userId = UUID.randomUUID();

        UserUpdateRequest req = mock(UserUpdateRequest.class);
        given(userRepository.findById(userId)).willReturn(Optional.empty());


        assertThatThrownBy(() -> userService.update(userId, req, Optional.empty()))
                .isInstanceOf(UserNotFoundException.class);

        then(userRepository).should().findById(userId);
        then(userRepository).should(never()).save(any());
        then(binaryRepo).shouldHaveNoInteractions();
        then(storage).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("delete 성공: 유저 존재 -> delete 호출")
    void delete_success() {

        UUID userId = UUID.randomUUID();
        User user = new User("u", "u@test.com", "password1234", null);
        ReflectionTestUtils.setField(user, "id", userId);

        given(userRepository.findById(userId)).willReturn(Optional.of(user));

        userService.delete(userId);


        then(userRepository).should().findById(userId);
        then(userRepository).should().delete(user);
    }

    @Test
    @DisplayName("delete 실패: 유저 없음 -> UserNotFoundException")
    void delete_fail_userNotFound() {

        UUID userId = UUID.randomUUID();
        given(userRepository.findById(userId)).willReturn(Optional.empty());

        assertThatThrownBy(() -> userService.delete(userId))
                .isInstanceOf(UserNotFoundException.class);

        then(userRepository).should().findById(userId);
        then(userRepository).should(never()).delete(any());
    }
}
