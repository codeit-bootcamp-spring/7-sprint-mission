package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.CreateUserCommand;
import com.sprint.mission.discodeit.dto.response.UserResponseDto;
import com.sprint.mission.discodeit.dto.update.UpdateUserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFBinaryContentRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class BasicUserServiceTest {

    private UserService userService;
    private UserRepository userRepository;
    private BinaryContentRepository binaryContentRepository;
    private UserStatusRepository userStatusRepository;

    @BeforeEach
    void setUp() {
        userRepository = new JCFUserRepository();
        binaryContentRepository = new JCFBinaryContentRepository();
        userStatusRepository = new JCFUserStatusRepository();
        userService = new BasicUserService(userRepository, binaryContentRepository, userStatusRepository);
    }

    @Test
    void createUser() {
        //given
        User user = new User("진우", "닉네임", "a@a.com", "1234", null);
        CreateUserCommand createUserCommand = new CreateUserCommand(user.getUsername(), user.getNickName(), user.getEmail(),user.getPassword(), null, null ,null);


        //when (서비스 테스트)
        UserResponseDto dto = userService.createUser(createUserCommand);

        //then
        User userById = userRepository.findById(dto.userId()).orElseThrow();
        assertEquals(user.getUsername(), userById.getUsername());
        assertEquals(user.getNickName(), userById.getNickName());
        assertEquals(user.getEmail(), userById.getEmail());
        assertEquals(user.getPassword(), userById.getPassword());
    }

    @Test
    void find() {
        //given
        User user = new User("진우", "닉네임", "a@a.com", "1234", null);
        User user1 = userRepository.save(user);
        UserStatus userStatus = new UserStatus(user1.getId());
        userStatusRepository.save(userStatus);

        //when
        UserResponseDto dto = userService.find(user1.getId());

        //then
        assertEquals(user1.getId(), dto.userId());
        assertEquals(user1.getUsername(), dto.username());
        assertEquals(user1.getEmail(), dto.email());
        assertEquals(user1.getNickName(), dto.nickName());
    }

    @Test
    void find_throw_when_invalid_id(){
        //given
        UUID invalidId = UUID.randomUUID();

        //when&then
        assertThrows(IllegalArgumentException.class, () -> userService.find(invalidId));
        //then
    }

    @Test
    void findAll() {
        //given
        User user = new User("진우", "닉네임", "a@a.com", "1234", null);
        User user1 = new User("이름", "닉넴", "b", "2345", null);
        userRepository.save(user);
        userRepository.save(user1);

        UserStatus userStatus = new UserStatus(user.getId());
        userStatusRepository.save(userStatus);
        UserStatus userStatus1 = new UserStatus(user1.getId());
        userStatusRepository.save(userStatus1);


        //when
        List<UserResponseDto> all = userService.findAll();

        //then
        assertEquals(2, all.size());

        List<UUID> userIds = all.stream()
                .map(UserResponseDto::userId)
                .toList();

        assertTrue(userIds.contains(user.getId()));
        assertTrue(userIds.contains(user1.getId()));
    }

    @Test
    void updateUser() {
        //given
        User user = new User("진우", "닉네임", "a@a.com", "1234", null);
        User saveUser = userRepository.save(user);
        UserStatus userStatus = new UserStatus(saveUser.getId());
        userStatusRepository.save(userStatus);

        //when
        UpdateUserDto dto = new UpdateUserDto("아토", "하이", "b", saveUser.getId());
        UserResponseDto user2 = userService.updateUser(dto, null);

        //then
        assertEquals(dto.username(), user2.username());
        assertEquals(dto.nickName(), user2.nickName());
        assertEquals(dto.email(), user2.email());

        User updateUser = userRepository.findById(saveUser.getId()).orElseThrow();

        assertEquals(user2.username(), updateUser.getUsername());
        assertEquals(user2.nickName(), updateUser.getNickName());
        assertEquals(user2.email(), updateUser.getEmail());
    }

    @Test
    void deleteUser() {
        //given
        User user = new User("진우", "닉네임", "a@a.com", "1234", null);
        User saveUser = userRepository.save(user);
        UserStatus userStatus = new UserStatus(saveUser.getId());
        userStatusRepository.save(userStatus);

        //when
        userService.deleteUser(saveUser.getId());

        //then
        Optional<User> deleteUser = userRepository.findById(saveUser.getId());

        assertTrue(deleteUser.isEmpty());
    }
}