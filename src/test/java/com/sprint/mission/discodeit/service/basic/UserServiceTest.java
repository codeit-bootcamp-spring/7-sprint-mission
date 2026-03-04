//package com.sprint.mission.discodeit.service.basic;
//
//import static org.assertj.core.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.*;
//
//import com.sprint.mission.discodeit.dto.UserCreateRequest;
//import com.sprint.mission.discodeit.entity.User;
//import com.sprint.mission.discodeit.exception.UserAlreadyExistsException;
//import com.sprint.mission.discodeit.exception.UserNotFoundException;
//import com.sprint.mission.discodeit.exception.UserStatusNotFoundException;
//import com.sprint.mission.discodeit.mapper.UserMapper;
//import com.sprint.mission.discodeit.mapper.dto.UserDto;
//import com.sprint.mission.discodeit.mapper.dto.UserUpdateRequest;
//import com.sprint.mission.discodeit.repository.jpa.BinaryContentsRepository;
//import com.sprint.mission.discodeit.repository.jpa.UsersRepository;
//import com.sprint.mission.discodeit.storage.BinaryContentStorage;
//import java.util.Optional;
//import java.util.UUID;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//@ExtendWith(MockitoExtension.class)
//class UserServiceTest {
//
//    @InjectMocks
//    private UserService userService;
//
//    @Mock
//    private UsersRepository userRepository;
//
////    @Mock
////    private UserStatusesRepository userStatusRepository;
//
//    @Mock
//    private BinaryContentsRepository binaryContentRepository;
//
//    @Mock
//    private BinaryContentStorage binaryContentStorage;
//
//    @Mock
//    private UserMapper userMapper;
//
//    /* =========================
//       create()
//       ========================= */
//
//    @Test
//    void create_success_without_profile() {
//        // given
//        UserCreateRequest request =
//            new UserCreateRequest("user1", "user1@test.com", "pw");
//
//        when(userRepository.findUserByUsername("user1"))
//            .thenReturn(Optional.empty());
//        when(userRepository.findUserByEmail("user1@test.com"))
//            .thenReturn(Optional.empty());
//
//        User user = new User("user1", "user1@test.com", "pw", null);
//        user.initUserStatus();
//
//        when(userRepository.save(any(User.class))).thenReturn(user);
//
//        UserDto dto =
//            new UserDto(UUID.randomUUID(), "user1", "user1@test.com", null, true);//  user.getUserStatus().isOnline()
//        when(userMapper.toDto(any(User.class))).thenReturn(dto);
//
//        // when
//        UserDto result = userService.create(request, Optional.empty());
//
//        // then
//        assertThat(result.username()).isEqualTo("user1");
//        verify(userRepository).save(any(User.class));
//    }
//
//    @Test
//    void create_fail_duplicate_username() {
//        UserCreateRequest request =
//            new UserCreateRequest("user1", "user1@test.com", "pw");
//
//        when(userRepository.findUserByUsername("user1"))
//            .thenReturn(Optional.of(mock(User.class)));
//
//        assertThatThrownBy(() ->
//            userService.create(request, Optional.empty())
//        ).isInstanceOf(UserAlreadyExistsException.class);
//    }
//
//    @Test
//    void create_fail_duplicate_email() {
//        UserCreateRequest request =
//            new UserCreateRequest("user1", "user1@test.com", "pw");
//
//        when(userRepository.findUserByUsername("user1"))
//            .thenReturn(Optional.empty());
//        when(userRepository.findUserByEmail("user1@test.com"))
//            .thenReturn(Optional.of(mock(User.class)));
//
//        assertThatThrownBy(() ->
//            userService.create(request, Optional.empty())
//        ).isInstanceOf(UserAlreadyExistsException.class);
//    }
//
//    /* =========================
//       find()
//       ========================= */
//
//    @Test
//    void find_success() {
//        UUID userId = UUID.randomUUID();
//
//        User user = new User("user", "email@test.com", "pw", null);
//        UserStatus status = new UserStatus(user, java.time.Instant.now());
////        status.setOnline(true);
//
//        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
//        when(userStatusRepository.findUserStatusByUserId(userId))
//            .thenReturn(Optional.of(status));
//
//        UserDto dto =
//            new UserDto(userId, "user", "email@test.com", null, true);
//        when(userMapper.toDto(user)).thenReturn(dto);
//
//        UserDto result = userService.find(userId);
//
//        assertThat(result.username()).isEqualTo("user");
//    }
//
//    @Test
//    void find_fail_user_not_found() {
//        UUID userId = UUID.randomUUID();
//
//        when(userRepository.findById(userId))
//            .thenReturn(Optional.empty());
//
//        assertThatThrownBy(() ->
//            userService.find(userId)
//        ).isInstanceOf(UserNotFoundException.class);
//    }
//
//    @Test
//    void find_fail_user_status_not_found() {
//        UUID userId = UUID.randomUUID();
//
//        when(userRepository.findById(userId))
//            .thenReturn(Optional.of(mock(User.class)));
//        when(userStatusRepository.findUserStatusByUserId(userId))
//            .thenReturn(Optional.empty());
//
//        assertThatThrownBy(() ->
//            userService.find(userId)
//        ).isInstanceOf(UserStatusNotFoundException.class);
//    }
//
//    /* =========================
//       update()
//       ========================= */
//
//    @Test
//    void update_success() {
//        UUID userId = UUID.randomUUID();
//
//        User user = new User("old", "old@test.com", "pw", null);
//
//        UserUpdateRequest update =
//            new UserUpdateRequest("new", "new@test.com", "newPw");
//
//        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
//        when(userRepository.findUserByUsername("new"))
//            .thenReturn(Optional.empty());
//        when(userRepository.findUserByEmail("new@test.com"))
//            .thenReturn(Optional.empty());
//
//        UserDto dto =
//            new UserDto(userId, "new", "new@test.com", null, false);
//        when(userMapper.toDto(user)).thenReturn(dto);
//
//        UserDto result =
//            userService.update(userId, update, Optional.empty());
//
//        assertThat(result.username()).isEqualTo("new");
//        assertThat(user.getPassword()).isEqualTo("newPw");
//    }
//
//    @Test
//    void update_fail_duplicate_username() {
//        UUID userId = UUID.randomUUID();
//        User user = new User("old", "old@test.com", "pw", null);
//
//        UserUpdateRequest update =
//            new UserUpdateRequest("dup", "new@test.com", null);
//
//        when(userRepository.findById(userId))
//            .thenReturn(Optional.of(user));
//        when(userRepository.findUserByUsername("dup"))
//            .thenReturn(Optional.of(mock(User.class)));
//
//        assertThatThrownBy(() ->
//            userService.update(userId, update, Optional.empty())
//        ).isInstanceOf(IllegalArgumentException.class);
//    }
//
//    /* =========================
//       delete()
//       ========================= */
//
//    @Test
//    void delete_success() {
//        UUID userId = UUID.randomUUID();
//        User user = new User("user", "test@test.com", "pw", null);
//
//        when(userRepository.findById(userId))
//            .thenReturn(Optional.of(user));
//
//        when(userStatusRepository.findUserStatusByUserId(any()))
//            .thenReturn(Optional.empty());
//
//        userService.delete(userId);
//
//        verify(userRepository).deleteById(userId);
//    }
//
//
//    @Test
//    void delete_fail_user_not_found() {
//        UUID userId = UUID.randomUUID();
//
//        when(userRepository.findById(userId))
//            .thenReturn(Optional.empty());
//
//        assertThatThrownBy(() ->
//            userService.delete(userId)
//        ).isInstanceOf(UserNotFoundException.class);
//    }
//}