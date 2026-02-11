package com.sprint.mission.discodeit.unit.service;

import com.sprint.mission.discodeit.dto.user.*;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapperManual;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import com.sprint.mission.discodeit.service.reader.UserReader;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // NOTE: strict stubs(엄격 모드)로 진행, 안쓰이는 when(스텁)있을시 실패 에러발생
class UserServiceUnitTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserReader userReader;

    @Mock
    private UserStatusRepository userStatusRepository;

    @Mock
    private BinaryContentRepository binaryContentRepository;

    @Mock
    private BinaryContentService binaryContentService;

    @Mock
    private UserMapperManual userMapper;

    @InjectMocks
    private BasicUserService userService;

    @Nested
    @DisplayName("signUp")
    class SignUp {
        @Test
        @DisplayName("[Behavior] 회원가입 - 유효한 입력값일때 userRepository.save() 호출")
        void signUp_shouldCallUserRepositorySave_whenValidInput() {
            // given
            String nickname = "Taeeon";
            String email = "taeeon@test.com";
            String password = "1234";

            when(userRepository.save(any(User.class)))
                    .thenReturn(User.create(nickname, email, password, null));

            UserSignupCommand command = UserSignupCommand.from(new UserSignupRequestDto(nickname, email, password), null);

            // when
            userService.signUp(command); // 흐름검증

            // then
            verify(userRepository, times(1)).save(any(User.class)); // 행위 검증
        }

        @Test
        @DisplayName("[Branch][Negative] 회원가입 - 유효하지않은 입력값일때 DiscodeitException 예외 발생 및 repository.save()미호출")
        void signUp_shouldThrowException_whenInValidInput() {
            UserSignupCommand userNameBlankCommand = UserSignupCommand.from(new UserSignupRequestDto("", "a@b.com", "123"), null);
            UserSignupCommand emailBlankCommand = UserSignupCommand.from(new UserSignupRequestDto("nick", "", "pw"), null);
            UserSignupCommand passwordBlankCommand = UserSignupCommand.from(new UserSignupRequestDto("nick", "a@b.com", ""), null);

            UserSignupCommand userNameBlankCommand2 = UserSignupCommand.from(new UserSignupRequestDto(" ", "a@b.com", "123"), null);
            UserSignupCommand emailBlankCommand2 = UserSignupCommand.from(new UserSignupRequestDto("nick", " ", "pw"), null);
            UserSignupCommand passwordBlankCommand2 = UserSignupCommand.from(new UserSignupRequestDto("nick", "a@b.com", " "), null);

            UserSignupCommand userNameNullCommand = UserSignupCommand.from(new UserSignupRequestDto(" ", "a@b.com", "123"), null);
            UserSignupCommand emailNullCommand = UserSignupCommand.from(new UserSignupRequestDto("nick", " ", "pw"), null);
            UserSignupCommand passwordNullCommand = UserSignupCommand.from(new UserSignupRequestDto("nick", "a@b.com", " "), null);

            // isBlank
            assertThrows(DiscodeitException.class, () ->
                    userService.signUp(userNameBlankCommand));
            assertThrows(DiscodeitException.class, () ->
                    userService.signUp(emailBlankCommand));
            assertThrows(DiscodeitException.class, () ->
                    userService.signUp(passwordBlankCommand));

            assertThrows(DiscodeitException.class, () ->
                    userService.signUp(userNameBlankCommand2));
            assertThrows(DiscodeitException.class, () ->
                    userService.signUp(emailBlankCommand2));
            assertThrows(DiscodeitException.class, () ->
                    userService.signUp(passwordBlankCommand2));

            // null
            assertThrows(DiscodeitException.class, () ->
                    userService.signUp(userNameNullCommand));
            assertThrows(DiscodeitException.class, () ->
                    userService.signUp(emailNullCommand));
            assertThrows(DiscodeitException.class, () ->
                    userService.signUp(passwordNullCommand));

            verify(userRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("getUserById")
    class GetUserById {
        @Test
        @DisplayName("[Behavior] 회원조회 - userReader.findUserOrThrow(id) 호출 (조회 위임 검증)")
        void getUserById_shouldDelegateToUserReader_whenCalled() {
            // given
            User user = User.create("name", "email@emc.com", "password", null);

            when(userReader.findUserOrThrow(user.getId())).thenReturn(User.create("nickname", "email@exa.com", "pwd", null));

            // when
            userService.getUserById(user.getId());

            verify(userReader).findUserOrThrow(user.getId());
        }

        @Test
        @DisplayName("[Behavior + Branch][Positive] 회원조회 - Reader에 위임하고 결과 그대로 반환")
        void getUserById_shouldReturnUser_whenFound() {

            // given
            UUID id = UUID.randomUUID();
            User user = User.create("taeeon", "a@b.com", "pw", null);
            when(userReader.findUserOrThrow(id)).thenReturn(user); // Stub
            when(userMapper.toDto(user)).thenReturn(
                    new UserResponseDto(
                            id,
                            user.getUsername(),
                            user.getEmail(),
                            null,
                            false,
                            user.getCreatedAt(),
                            user.getUpdatedAt())
            );
            // when
            UserResponseDto result = userService.getUserById(id); // 흐름 검증

            assertEquals(user.getUsername(), result.username()); // 분기 검증
            assertEquals(user.getEmail(), result.email());

            // then
            verify(userReader).findUserOrThrow(id); // 행위검증
        }

        @Test
        @DisplayName("[Exception] 회원조회 - 기존 회원이 없다면 UserNotFoundException 예외 전파")
        void getUserById_shouldPropagateException_whenReaderThrowNotFound() {
            UUID id = UUID.randomUUID();
            when(userReader.findUserOrThrow(id)).thenThrow(new UserNotFoundException(id));

            assertThrows(UserNotFoundException.class, () -> userService.getUserById(id));
        }

        @Test
        @DisplayName("[Branch][Negative] 회원조회 - 유효하지않은 입력값일때 DiscodeitException 예외 발생")
        void getUserById_shouldThrowException_whenIdIsInvalid() {
            when(userReader.findUserOrThrow(null)).thenThrow(new DiscodeitException(ErrorCode.INVALID_INPUT));
            assertThrows(DiscodeitException.class, () -> userService.getUserById(null));
            verify(userReader).findUserOrThrow(null);
        }
    }

    @Nested
    @DisplayName("deleteUser")
    class DeleteUser {
        @Test
        @DisplayName("[Behavior] 회원삭제 - userRepository.deleteById() 위임 호출")
        void deleteUser_shouldCallRepositoryDelete_whenValidId() {

            // given
            UUID id = UUID.randomUUID();
            User user = User.builder()
                    .nickname("taeeon")
                    .password("pwd")
                    .email("dfds@exmap.com")
                    .build();
            UserStatus userStatus = new UserStatus(user);
            when(userReader.findUserOrThrow(any())).thenReturn(user);

            // when
            userService.deleteUser(id);

            // then
            verify(userRepository, times(1)).deleteById(user.getId());

        }

        @Test
        @DisplayName("[Branch][Negative] 회원삭제 - 유효하지않은 입력값일때 DiscodeitException 발생 및 Repository 미호출")
        void deleteUser_shouldThrowException_whenIdIsNull() {
            DiscodeitException discodeitException = assertThrows(DiscodeitException.class, () -> userService.deleteUser(null));
            assertEquals(ErrorCode.INVALID_INPUT, discodeitException.getErrorCode());
            assertEquals(HttpStatus.BAD_REQUEST, discodeitException.getErrorCode().getStatus());
            assertEquals(ErrorCode.INVALID_INPUT.getCode(), discodeitException.getErrorCode().getCode());
            verify(userRepository, never()).deleteById(any());
        }
    }

    @Nested
    @DisplayName("updateUser")
    class UpdateUser {

        @Test
        @DisplayName("[Exception] 회원수정 - 미존재 유저 일경우 UserNotFoundException 전파")
        void updateUser_shouldThrowException_whenUserNotFound() {
            //given
            UUID id = UUID.randomUUID();
            when(userReader.findUserOrThrow(id)).thenThrow(new UserNotFoundException(id));
            UserUpdateCommand updateCommand = UserUpdateCommand.from(id, new UserUpdateRequestDto("new", null, null), null);
            //when+then
            assertThrows(
                    UserNotFoundException.class,
                    () -> userService.updateUser(updateCommand)
            );

            //then
            verify(userReader).findUserOrThrow(id);
            verify(userRepository, never()).save(any());
        }

        @Disabled("jpa dirty checking으로 더이상 도메인에서 update flag없이 먹등성 그대로 save요청됨 추후 이부분 검토")
        @Test
        @DisplayName("[Behavior + Branch] 회원수정 - 닉네임 변경 시 userRepository.save() 호출")
        void updateUser_shouldCallRepositorySave_whenNicknameChanged() {

            // given (Stub 설정, 외부 협력자와 반환값 고정)
            UUID id = UUID.randomUUID();
            User real = User.create("nick", "a@b.com", "pw", null);
            when(userReader.findUserOrThrow(id)).thenReturn(real);
            UserUpdateCommand updateCommand = UserUpdateCommand.from(id, new UserUpdateRequestDto("new", null, null), null);
            when(userRepository.save(real)).thenReturn(real);
            // when (행위 실행 : 실제 서비스 호출)
            userService.updateUser(updateCommand);
            InOrder inOrder = inOrder(userReader, userRepository);

            // then (검증 : 협력자 호출/순서 확인)
            inOrder.verify(userReader).findUserOrThrow(real.getId());
            inOrder.verify(userRepository).save(real);
        }

        @Disabled("jpa dirty checking으로 더이상 도메인에서 update flag없이 먹등성 그대로 save요청됨 추후 이부분 검토")
        @Test
        @DisplayName("[Behavior + Branch][Negative] 회원수정 - 닉네임 미변경시 userRepository.save() 미호출")
        void updateUser_shouldNotCallRepositorySave_whenNicknameNotChanged() {

            // given (Stub 설정, 외부 협력자와 반환값 고정)
            UUID id = UUID.randomUUID();
            User real = User.create("same", "a@b.com", "pw", null);
            when(userReader.findUserOrThrow(id)).thenReturn(real);
            UserUpdateCommand updateCommand = UserUpdateCommand.from(id, new UserUpdateRequestDto("same", null, null), null);

            // when (행위 실행 : 실제 서비스 호출)
            userService.updateUser(updateCommand);
            InOrder inOrder = inOrder(userReader, userRepository);

            // then (검증 : 협력자 호출/순서 확인)
            inOrder.verify(userReader).findUserOrThrow(id);
            inOrder.verify(userRepository, never()).save(real);
        }

        @Test
        @DisplayName("[Behavior + Branch] 회원수정 - 이메일 변경 시 userRepository.save() 호출")
        void updateUser_shouldCallRepositorySave_whenEmailChanged() {
            // given
            UUID id = UUID.randomUUID();
            User real = User.create("nick", "a@b.com", "pw", null);
            when(userReader.findUserOrThrow(id)).thenReturn(real);
            UserUpdateCommand updateCommand = UserUpdateCommand.from(id, new UserUpdateRequestDto(null, "change@email.com", null), null);
            when(userRepository.save(real)).thenReturn(real);
            // when
            userService.updateUser(updateCommand);

            // then (순서 + 위임 검증)
            InOrder inOrder = inOrder(userReader, userRepository);
            inOrder.verify(userReader).findUserOrThrow(id);
            inOrder.verify(userRepository).save(any(User.class));
        }

        @Test
        @DisplayName("[Behavior + Branch] 회원수정 - 비밀번호 변경 시 userRepository.save() 호출")
        void updateUser_shouldCallRepositorySave_whenPasswordChanged() {
            // given

            UUID id = UUID.randomUUID();
            User real = User.create("nick", "a@b.com", "pw", null);
            when(userReader.findUserOrThrow(id)).thenReturn(real);
            UserUpdateCommand updateCommand = UserUpdateCommand.from(id, new UserUpdateRequestDto(null, null, "vjhsngr"), null);
            when(userRepository.save(real)).thenReturn(real);
            // when
            userService.updateUser(updateCommand);

            // then
            InOrder inOrder = inOrder(userReader, userRepository);
            inOrder.verify(userReader).findUserOrThrow(id);
            inOrder.verify(userRepository).save(real);
        }

        @Test
        @DisplayName("[Behavior + Branch] 회원수정 - 프로필이미지 id 변경 시 userRepository.save() 호출")
        void updateUser_shouldCallRepositorySave_whenProfileIdChanged() {
            // given
            UUID id = UUID.randomUUID();
            User real = User.create("nick", "a@b.com", "pw", null);
            when(userReader.findUserOrThrow(any())).thenReturn(real);

            MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "test.txt", "text/plain", "test".getBytes());
            UserUpdateCommand updateCommand = UserUpdateCommand.from(id, new UserUpdateRequestDto(null, null, null), mockMultipartFile);
            UUID binaryId = UUID.randomUUID();
            BinaryContent binaryContent = updateCommand.profile()
                    .map((binaryCommand) ->
                            new BinaryContent(
                                    binaryCommand.fileName(),
                                    binaryCommand.contentType(),
                                    binaryCommand.size()
                            )).orElse(null);

            when(binaryContentService.uploadBinaryContent(updateCommand.profile().orElse(null))).thenReturn(binaryId);
            when(userRepository.save(real)).thenReturn(real);
            // when
            userService.updateUser(updateCommand);

            // then
            InOrder inOrder = inOrder(userReader, userRepository);
            inOrder.verify(userReader).findUserOrThrow(id);
            inOrder.verify(userRepository).save(real);
            // TODO: binary verify는 안넣어도 되는지 추후 검토
        }

        @Disabled("jpa dirty checking으로 더이상 도메인에서 update flag없이 먹등성 그대로 save요청됨 추후 이부분 검토")
        @Test
        @DisplayName("[Behavior + Branch][Negative] 회원수정 - 모든필드 null일때 미변경 userRepository.save() 미호출")
        void updateUser_shouldNotCallRepositorySave_whenAllFieldNull() {
            // given
            UUID id = UUID.randomUUID();
            User real = User.create("nick", "a@b.com", "pw", null);
            when(userReader.findUserOrThrow(id)).thenReturn(real);
            UserUpdateCommand updateCommand = UserUpdateCommand.from(id, new UserUpdateRequestDto(null, null, null), null);
            // when
            userService.updateUser(updateCommand);

            // then
            InOrder inOrder = inOrder(userReader, userRepository);
            inOrder.verify(userReader).findUserOrThrow(id);
            inOrder.verify(userRepository, never()).save(any());
        }

        @Disabled("jpa dirty checking으로 더이상 도메인에서 update flag없이 먹등성 그대로 save요청됨 추후 이부분 검토")
        @Test
        @DisplayName("[Behavior + Branch][Negative] 회원수정 - 기존과 동일값 일때 userRepository.save() 미호출")
        void updateUser_shouldNotCallRepositorySave_whenNoFieldChanged() {
            // given
            UUID id = UUID.randomUUID();
            User real = User.create("nick", "a@b.com", "pw", null);
            when(userReader.findUserOrThrow(id)).thenReturn(real);
            UserUpdateCommand updateCommand = UserUpdateCommand.from(id, new UserUpdateRequestDto("nick", "a@b.com", "pw"), null);

            // when
            userService.updateUser(updateCommand);

            // then
            InOrder inOrder = inOrder(userReader, userRepository);
            inOrder.verify(userReader).findUserOrThrow(id);
            inOrder.verify(userRepository, never()).save(any());
        }
    }

//    @Nested
//    @DisplayName("getAllUsers")
//    class GetAllUsers {
//        @Test
//        @DisplayName("[Behavior + Flow] 모든회원조회 - 리포지토리 결과를 그대로 반환")
//        void getAllUsers_shouldReturnListFromRepository() {
//            List<User> users = List.of(User.create("a", "a@b.com", "p", RoleType.USER, "010", null));
//            List<UserResponseDto> origin =
//                    users.stream().map(user ->
//
//                    UserResponseDto.from(user, null)
//                    ).toList();
//            when(userRepository.findAll()).thenReturn(users);
//            when(userStatusRepository.findById(any())).thenReturn(users.get(0));
//
//            List<UserResponseDto> result = userService.getAllUsers();
//
//            assertEquals(users, result); // flow 검증 (결과 전달이 잘 되었는가)
//            verify(userRepository).findAll(); // behavior 검증(호출/위임이 잘되었는가)
//        }
//    }

    @Nested
    @DisplayName("getUsersByIds")
    class GetUsersByIds {
        @Test
        @DisplayName("[Branch][Negative] 특정 회원리스트 조회- 입력이 null이면 DiscodeitException 예외 발생")
        void getUsersByIds_shouldThrowException_whenInputNull() {
            DiscodeitException discodeitException = assertThrows(DiscodeitException.class, () -> userService.getUsersByIds(null));
            assertEquals(ErrorCode.INVALID_INPUT, discodeitException.getErrorCode());
            assertEquals(HttpStatus.BAD_REQUEST, discodeitException.getErrorCode().getStatus());
            assertEquals(ErrorCode.INVALID_INPUT.getCode(), discodeitException.getErrorCode().getCode());
            verify(userRepository, never()).findAllById(any()); // TODO: 단위테스트에서도 해당 jpa 메서드 호출하는지 한번 검토 문제없을시 다른부분도 안봐도됨
        }

        @Test
        @DisplayName("[Behavior + Flow] 특정 회원리스트 조회 - 주어진 id 리스트에 해당하는 유저 목록 반환")
        void getUsersByIds_shouldReturnUsers_whenIdsValid() {

            //give
            List<UUID> ids = List.of(UUID.randomUUID());
            List<User> users = List.of(User.create("a", "a@b.com", "p", null));
            when(userRepository.findAllById(ids)).thenReturn(users);

            //when
            List<User> result = userService.getUsersByIds(ids);

            //then
            assertEquals(users, result);
            verify(userRepository).findAllById(ids);
        }

        @Test
        @DisplayName("[Behavior + Flow] 특정 회원리스트 조회 - 빈 ID 목록시 빈 리스트 반환 ")
        void getUsersByIds_ShouldReturnEmptyList_whenIdsEmpty() {
            //given
            List<UUID> ids = List.of();
            List<User> users = List.of();
            when(userRepository.findAllById(ids)).thenReturn(users);

            //when
            List<User> result = userService.getUsersByIds(ids);

            //then
            assertEquals(users, result);
            verify(userRepository).findAllById(ids);
        }
    }
}