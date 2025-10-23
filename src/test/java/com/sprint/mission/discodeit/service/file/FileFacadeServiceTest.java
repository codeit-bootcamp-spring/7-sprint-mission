package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileManager;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.NoSuchElementException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class FileFacadeServiceTest {
    private Path testUserFilePath;
    private Path testChannelFilePath;
    private Path testMessageFilePath;

    private FileManager<User> fileUserManager;
    private FileManager<Channel> channelFileManager;
    private FileManager<Message> messageFileManager;

    private UserService userService;
    private ChannelService channelService;
    private MessageService messageService;
    private FileFacadeService facadeService;

    @BeforeEach
    void setUp() throws IOException {
        testUserFilePath = Paths.get("build/tmp/test-filemanager.ser");
        testChannelFilePath =  Paths.get("build/tmp/test-channel.ser");
        testMessageFilePath = Paths.get("build/tmp/test-message.ser");

        Files.createDirectories(testUserFilePath.getParent());
        Files.createDirectories(testChannelFilePath.getParent());
        Files.createDirectories(testMessageFilePath.getParent());

        Files.deleteIfExists(testUserFilePath);
        fileUserManager = new FileManager<>(testUserFilePath);
        channelFileManager = new FileManager<>(testChannelFilePath);
        messageFileManager = new FileManager<>(testMessageFilePath);


        userService = new FileUserService(
                new FileUserRepository(
                        fileUserManager
                )
        );
        channelService = new FileChannelService(
                new FileChannelRepository(
                        channelFileManager
                )
        );

        messageService = new FileMessageService(
                new FileMessageRepository(
                        messageFileManager
                )
        );

        facadeService = new FileFacadeService(userService, channelService, messageService);

    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(testUserFilePath);
        Files.deleteIfExists(testChannelFilePath);
        Files.deleteIfExists(testMessageFilePath);
    }

    @Test
    @DisplayName("[정상 케이스] - 메세지 생성 시 유저, 채널 관계도 형성 테스트")
    void givenMessage_whenCreateMessageAndUserAddChannel_thenSuccess()  {
        // given
        User user = userService.createUser("testUser", "email@test.com", "010-1111-2222", "pronoun");
        Channel channel = channelService.createChannel(Channel.ChannelType.PRIVATE, "private", "사설");
        
        // when
        Message message = facadeService.createMessageWithRelation(user.getCommon().getId(), channel.getCommon().getId(), "content");

        // then 유저에 추가되었고, update 적용 확인
        User updatedUser = userService.getUser(user.getCommon().getId());
        assertTrue(updatedUser.getJoinChannels().contains(channel.getCommon().getId()));
        assertNotEquals(updatedUser.getCommon().createAt, updatedUser.getCommon().updateAt); // 채널 추가 시, 업데이트 되었는지

        //메세지 삭제, 메세지 잘 생성되었는지 확인
        assertEquals(1, messageService.getAllMessages().size());
        Message createdMessage = messageService.getMessage(message.getCommon().getId());
        assertEquals("content", createdMessage.getContent());
        assertEquals(user.getCommon().getId(), createdMessage.getUserId());
        assertEquals(channel.getCommon().getId(), createdMessage.getChannelId());
    }

    @Test
    @DisplayName("[정상 케이스] - 메시지 삭제 시 유저, 채널 존재 확인 후 삭제 테스트")
    void givenMessage_whenDeleteMessageWithRelation_thenSuccess() {
        // given
        User user = userService.createUser("testUser", "email@test.com", "010-1111-2222", "pronoun");
        Channel channel = channelService.createChannel(Channel.ChannelType.PRIVATE, "private", "사설");
        Message message = messageService.createMessage("content", channel.getCommon().getId(), user.getCommon().getId());

        // when
        facadeService.deleteMessageWithRelation(message.getCommon().getId());

        // then
        assertTrue(messageService.getAllMessages().isEmpty());
        assertThrows(NoSuchElementException.class,
                () -> messageService.getMessage(message.getCommon().getId()));
    }

    @Test
    @DisplayName("[비정상 케이스 - 존재하지 않는 유저] - 메시지 삭제 시  예외 발생 테스트")
    void givenMessage_whenUserNotExists_thenThrows() {
        User user = userService.createUser("testUser", "email@test.com", "010-1111-2222", "pronoun");
        Channel channel = channelService.createChannel(Channel.ChannelType.PRIVATE, "private", "사설");
        Message message = messageService.createMessage("content", channel.getCommon().getId(), user.getCommon().getId());

        // 유저 삭제
        userService.deleteUser(user.getCommon().getId());

        // then
        NoSuchElementException exception = assertThrows(NoSuchElementException.class,
                () -> facadeService.deleteMessageWithRelation(message.getCommon().getId()));
        assertTrue(exception.getMessage().contains("찾을 수 없는 유저"));
    }

    @Test()
    @DisplayName("[비정상 케이스 - 존재하지 않는 채널] - 메시지 삭제 시 예외 발생 테스트")
    void givenUserAndChannelAndMessage_whenChannelNotExists_thenThrows() {
        User user = userService.createUser("testUser", "email@test.com", "010-1111-2222", "pronoun");
        Channel channel = channelService.createChannel(Channel.ChannelType.PRIVATE, "private", "사설");
        Message message = messageService.createMessage("content", channel.getCommon().getId(), user.getCommon().getId());

        // 채널 삭제
        channelService.deleteChannel(channel.getCommon().getId());

        // then
        NoSuchElementException exception = assertThrows(NoSuchElementException.class,
                () -> facadeService.deleteMessageWithRelation(message.getCommon().getId()));
        assertTrue(exception.getMessage().contains("찾을 수 없는 채널"));
    }

    @Test
    @DisplayName("[정상 케이스] - 유저가 채널에 추가되는 테스트")
    void givenUserAndChannel_whenAddChannel_thenSuccess() {
        // given
        User user = userService.createUser("testUser", "email@test.com", "010-1111-2222", "pronoun");
        Channel channel = channelService.createChannel(Channel.ChannelType.PRIVATE, "private", "사설");

        // when
        facadeService.addChannelToUserWithRelation(user.getCommon().getId(), channel.getCommon().getId());

        // then
        User updatedUser = userService.getUser(user.getCommon().getId());
        assertTrue(updatedUser.getJoinChannels().contains(channel.getCommon().getId()));
    }

    @Test
    @DisplayName("[비정상 케이스 - 존재하지 않는 유저] 메세지 생성 테스트")
    void givenNonUser_whenAddChannel_thenThrows() {
        UUID randomUserId = java.util.UUID.randomUUID();
        Channel channel = channelService.createChannel(Channel.ChannelType.PRIVATE, "private", "사설");

        assertThrows(NoSuchElementException.class,
                () -> facadeService.addChannelToUserWithRelation(randomUserId, channel.getCommon().getId()));
    }

    @Test
    @DisplayName("[비정상 케이스 - 존재하지 않는 채널] - 메세지 생성 테스트 ")
    void givenNonExistentChannel_whenAddChannel_thenThrows() {
        User user = userService.createUser("testUser", "email@test.com", "010-1111-2222", "pronoun");
        UUID fakeChannelId = java.util.UUID.randomUUID();

        assertThrows(NoSuchElementException.class,
                () -> facadeService.addChannelToUserWithRelation(user.getCommon().getId(), fakeChannelId));
    }

    @Test
    @DisplayName("[정상 케이스] - 채널 삭제 시 연관 유저, 메시지 삭제 확인")
    void givenUsersAndMessages_whenDeleteChannelWithRelation_thenSuccess() {
        // given
        User user1 = userService.createUser("user1", "email1@test.com", "010-1111-2222", "pronoun1");
        User user2 = userService.createUser("user2", "email2@test.com", "010-1111-3333", "pronoun2");
        Channel channel = channelService.createChannel(Channel.ChannelType.PRIVATE, "private", "사설");

        // 유저들을 채널에 추가
        facadeService.addChannelToUserWithRelation(user1.getCommon().getId(), channel.getCommon().getId());
        facadeService.addChannelToUserWithRelation(user2.getCommon().getId(), channel.getCommon().getId());

        // 채널 관련 메시지 생성
        Message message1 = messageService.createMessage("msg1", channel.getCommon().getId(), user1.getCommon().getId());
        Message message2 = messageService.createMessage("msg2", channel.getCommon().getId(), user2.getCommon().getId());

        // when
        facadeService.deleteChannelWithRelation(channel.getCommon().getId());

        // then
        // 채널 삭제 확인
        assertThrows(NoSuchElementException.class,
                () -> channelService.getChannel(channel.getCommon().getId()));

        // 유저 채널 제거 확인
        assertFalse(userService.getUser(user1.getCommon().getId()).getJoinChannels().contains(channel.getCommon().getId()));
        assertFalse(userService.getUser(user2.getCommon().getId()).getJoinChannels().contains(channel.getCommon().getId()));

        // 메시지 삭제 확인
        assertTrue(messageService.getAllMessages().isEmpty());
    }

    @Test
    @DisplayName("[정상 케이스] - 유저 삭제 시 해당 유저 메시지도 삭제 확인")
    void givenUserWithMessages_whenDeleteUserWithRelation_thenSuccess() {
        // given
        User user = userService.createUser("user1", "email1@test.com", "010-1111-2222", "pronoun1");
        Channel channel = channelService.createChannel(Channel.ChannelType.PRIVATE, "private", "사설");

        // 메시지 생성
        Message message1 = messageService.createMessage("msg1", channel.getCommon().getId(), user.getCommon().getId());
        Message message2 = messageService.createMessage("msg2", channel.getCommon().getId(), user.getCommon().getId());

        // when
        facadeService.deleteUserWithRelation(user.getCommon().getId());

        // then
        // 유저 삭제 확인
        assertThrows(NoSuchElementException.class,
                () -> userService.getUser(user.getCommon().getId()));

        // 해당 유저 메시지 삭제 확인
        assertTrue(messageService.getAllMessages().isEmpty());
    }









}