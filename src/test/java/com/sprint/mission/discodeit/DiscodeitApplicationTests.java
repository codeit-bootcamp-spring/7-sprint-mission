//package com.sprint.mission.discodeit;
//
//import com.sprint.mission.discodeit.dto.request.authService.LoginRequestDto;
//import com.sprint.mission.discodeit.dto.request.binaryContent.BinaryContentCreateRequestDto;
//import com.sprint.mission.discodeit.dto.request.channel.ChannelPrivateCreateRequestDto;
//import com.sprint.mission.discodeit.dto.request.channel.ChannelUpdateRequestDto;
//import com.sprint.mission.discodeit.dto.request.message.MessageCreateRequestDto;
//import com.sprint.mission.discodeit.dto.request.message.MessageUpdateRequestDto;
//import com.sprint.mission.discodeit.dto.request.readStatus.ReadStatusCreateRequestDto;
//import com.sprint.mission.discodeit.dto.request.readStatus.ReadStatusUpdateRequestDto;
//import com.sprint.mission.discodeit.dto.request.user.UserCreateRequestDto;
//import com.sprint.mission.discodeit.dto.request.user.UserUpdateRequestDto;
//import com.sprint.mission.discodeit.dto.request.userStatus.UserStatusCreateRequestDto;
//import com.sprint.mission.discodeit.dto.request.userStatus.UserStatusUpdateRequestDto;
//import com.sprint.mission.discodeit.entity.Channel;
//import com.sprint.mission.discodeit.entity.Message;
//import com.sprint.mission.discodeit.entity.User;
//import com.sprint.mission.discodeit.entity.UserStatus;
//import com.sprint.mission.discodeit.entityElement.*;
//import com.sprint.mission.discodeit.repository.*;
//import com.sprint.mission.discodeit.service.*;
//import com.sprint.mission.discodeit.service.basic.BasicChannelService;
//import com.sprint.mission.discodeit.service.basic.BasicUserService;
//import org.assertj.core.api.Assertions;
//import org.instancio.Instancio;
//import org.instancio.TypeToken;
//import org.instancio.junit.InstancioSource;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.RepeatedTest;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.time.Instant;
//import java.util.HashSet;
//import java.util.List;
//
//import static com.sprint.mission.discodeit.service.util.StaticString.DEFAULT_SENDER;
//import static org.assertj.core.api.Assertions.*;
//import static org.instancio.Select.field;
//
//@SpringBootTest
//class DiscodeitApplicationTests {
//
//    private static final Logger log = LoggerFactory.getLogger(DiscodeitApplicationTests.class);
//    @Autowired private UserService userService;
//    @Autowired private ChannelService channelService;
//    @Autowired private AuthService authService;
//    @Autowired private MessageService messageService;
//    @Autowired private ReadStatusService readStatusService;
//    @Autowired private UserStatusService userStatusService;
//    @Autowired private BinaryContentService binaryContentService;
//    @Autowired private UserRepository userRepository;
//    @Autowired private MessageRepository messageRepository;
//    @Autowired private ChannelRepository channelRepository;
//    @Autowired private UserStatusRepository userStatusRepository;
//    @Autowired private BinaryContentRepository binaryContentRepository;
//    @Autowired private ReadStatusRepository readStatusRepository;
//
//    @BeforeEach
//    public void resetRepositories(){
//        userRepository.resetUserRepository();
//        channelRepository.resetChannelRepository();
//        messageRepository.resetMessageRepository();
//        userStatusRepository.resetRepository();
//        binaryContentRepository.resetBinaryContentRepository();
//        readStatusRepository.resetRepository();
//    }
//
//    @BeforeEach
//    public void setDefaultSender(){
//        userService.createUser(
//                new UserCreateRequestDto(
//                        DEFAULT_SENDER.getUserName(),
//                        DEFAULT_SENDER.getName(),
//                        DEFAULT_SENDER.getEmail(),
//                        DEFAULT_SENDER.getPassword()
//                )
//        );
//    }
//	@RepeatedTest(value = 10, name = "{displayName} {currentRepetition}/{totalRepetitions}")
//    @DisplayName("[정상 케이스] - 객체 생성")
//	void create_Instance_Test() {
//        //given
//        UserCreateRequestDto userCreateRequestDto = Instancio.create(UserCreateRequestDto.class);
//        User user = userService.createUser(userCreateRequestDto);
//
//        ChannelPrivateCreateRequestDto channelPrivateCreateRequestDto = Instancio.of(ChannelPrivateCreateRequestDto.class)
//                .set(field(ChannelPrivateCreateRequestDto::getUserIdList), new HashSet<>(List.of(user.getId())))
//                .create();
//        Channel privateChannel = channelService.createPrivateChannel(channelPrivateCreateRequestDto);
//
//        MessageCreateRequestDto messageCreateRequestDto = Instancio.of(MessageCreateRequestDto.class)
//                .set(field(MessageCreateRequestDto::getChannelId), privateChannel.getId())
//                .set(field(MessageCreateRequestDto::getSenderId), user.getId())
//                .create();
//        Message message = messageService.createMessage(messageCreateRequestDto);
//
//        //
//        var actualResult = channelService.readChannel(privateChannel.getId());
//        var actualResult3 = userService.readUser(user.getId());
//        var actualResult2 = messageService.readMessage(message);
//        var actualResultReadStatus = readStatusService.findAllyByUserId(user.getId());
//        var actualResultUserStatus = userStatusService.findAll();
//
//        //then
//        assertThat(actualResult.getName()).as("channel 객체 생성 테스트").isEqualTo(privateChannel.getName());
//        assertThat(actualResult3.getName()).as("user 객체 생성 테스트").isEqualTo(user.getName());
//        assertThat(actualResult2.getContent()).as("message 객체 생성 테스트").isEqualTo(message.getContent());
//        assertThat(actualResultReadStatus.size()).as("readstatus 객체 생성 테스트").isEqualTo(1);
//        assertThat(actualResultUserStatus.size()).as("userstatus 객체 생성 테스트").isEqualTo(1);
//    }
//
//    @RepeatedTest(value = 10, name = "{displayName} {currentRepetition}/{totalRepetitions}")
//    @DisplayName("[정상 케이스] - 객체 업데이트")
//    void update_Instance_Test(){
//        //given
//        UserCreateRequestDto userCreateRequestDto = Instancio.create(UserCreateRequestDto.class);
//        User user = userService.createUser(userCreateRequestDto);
//
//        ChannelPrivateCreateRequestDto channelPrivateCreateRequestDto = Instancio.of(ChannelPrivateCreateRequestDto.class)
//                .set(field(ChannelPrivateCreateRequestDto::getUserIdList), new HashSet<>(List.of(user.getId())))
//                .create();
//        Channel privateChannel = channelService.createPrivateChannel(channelPrivateCreateRequestDto);
//
//        MessageCreateRequestDto messageCreateRequestDto = Instancio.of(MessageCreateRequestDto.class)
//                .set(field(MessageCreateRequestDto::getChannelId), privateChannel.getId())
//                .set(field(MessageCreateRequestDto::getSenderId), user.getId())
//                .create();
//        Message message = messageService.createMessage(messageCreateRequestDto);
//
//        var userUpdateRequestDto = Instancio.of(new TypeToken<UserUpdateRequestDto<String>>() {
//        }).set(field("type"), UserElement.NAME)
//                .set(field("userId"), user.getId())
//                .create();
//
//        var messageUpdateRequestDto = Instancio.of(new TypeToken<MessageUpdateRequestDto<String>>() {
//                })
//                .set(field("type"), MessageElement.CONTENT)
//                .set(field("messageId"), message.getId())
//                .create();
//
//        var channelUpdateRequestDto = Instancio.of(new TypeToken<ChannelUpdateRequestDto<String>>() {
//        })
//                .set(field("type"), ChannelElement.NAME)
//                .set(field("channelId"), privateChannel.getId())
//                .create();
//        //when
//        userService.updateUser(userUpdateRequestDto);
//        messageService.updateMessage(messageUpdateRequestDto);
//        ;
//
//        var expectedResult = userRepository.getUpdatedUser().size();
//        var expectedResult2 = messageRepository.getUpdatedMessage().size();
//
//        //then
//        assertThat(expectedResult).as("user 업데이트 테스트").isEqualTo(1);
//        assertThat(expectedResult2).as("message 업데이트 테스트").isEqualTo(1);
//        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class,()->channelService.updateChannel(channelUpdateRequestDto));
//
//
//    }
//
//    @RepeatedTest(value = 10, name = "{displayName} {currentRepetition}/{totalRepetitions}")
//    @DisplayName("[정상 케이스] 객체 삭제")
//    void delete_Instance_Test(){
//        UserCreateRequestDto userCreateRequestDto = Instancio.create(UserCreateRequestDto.class);
//        User user = userService.createUser(userCreateRequestDto);
//
//        ChannelPrivateCreateRequestDto channelPrivateCreateRequestDto = Instancio.of(ChannelPrivateCreateRequestDto.class)
//                .set(field(ChannelPrivateCreateRequestDto::getUserIdList), new HashSet<>(List.of(user.getId())))
//                .create();
//        Channel privateChannel = channelService.createPrivateChannel(channelPrivateCreateRequestDto);
//
//        MessageCreateRequestDto messageCreateRequestDto = Instancio.of(MessageCreateRequestDto.class)
//                .set(field(MessageCreateRequestDto::getChannelId), privateChannel.getId())
//                .set(field(MessageCreateRequestDto::getSenderId), user.getId())
//                .create();
//        Message message = messageService.createMessage(messageCreateRequestDto);
//
//        //when
//        userService.deleteUser(user.getId());
//        channelService.deleteChannel(privateChannel.getId());
//
//        //given
//        var actualResultUser = userRepository.getAllUser();
//        var actualResultMessage = messageRepository.getAllMessage();
//        var actualResultChannel = channelRepository.getAllChannel();
//        var actualResultReadStatus = readStatusRepository.readAllReadStatus();
//        var actualResultUserStatus = userStatusRepository.readAllUserStatus();
//
//        assertThat(actualResultUser.size()).as("user 삭제 테스트").isEqualTo(0);
//        assertThat(actualResultMessage.size()).as("message 삭제 테스트").isEqualTo(0);
//        assertThat(actualResultChannel.size()).as("channel 삭제 테스트").isEqualTo(0);
//        assertThat(actualResultReadStatus.size()).as("readstatus 삭제 테스트").isEqualTo(0);
//        assertThat(actualResultUserStatus.size()).as("userstatus 삭제 테스트").isEqualTo(0);
//    }
//    @RepeatedTest(value = 10, name = "{displayName} {currentRepetition}/{totalRepetitions}")
//    @DisplayName("[정상 케이스] - 유저 로그인 ")
//    void authService_login_Test(){
//        //given
//        var userCreateRequestDto1 = Instancio.create(UserCreateRequestDto.class);
//        User user = userService.createUser(userCreateRequestDto1);
//
//        //when
//        var actualResult = authService.checkLoginUser(new LoginRequestDto(user.getUserName(),user.getPassword()));
//
//        //given
//        assertThat(actualResult.getUserName()).as("로그인 성공 테스트").isEqualTo(user.getUserName());
//
//    }
//
//    @RepeatedTest(value = 10, name = "{displayName} {currentRepetition}/{totalRepetitions}")
//    @DisplayName("[예외 케이스] - 일치하는 유저 없음")
//    void authService_logout_Test(){
//
//        //given
//        var userCreateRequestDto1 = Instancio.create(UserCreateRequestDto.class);
//        User user = userService.createUser(userCreateRequestDto1);
//        System.out.println(user.getUserName());
//
//        //when
//        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> authService.checkLoginUser(new LoginRequestDto("testUser",user.getPassword())));
//
//        //given
//    }
//
//    @RepeatedTest(value = 10, name = "{displayName} {currentRepetition}/{totalRepetitions}")
//    @DisplayName("[정상 케이스] - 추가 구현체 생성")
//    void status_create_Test(){
//        //given
//        User user = userRepository.saveUser(Instancio.create(User.class));
//        Channel channel = channelRepository.saveChannel(Instancio.create(Channel.class));
//        Message message = messageRepository.saveMessage(Instancio.of(Message.class)
//                .set(field(Message::getSenderId), user.getId())
//                .create());
//        var readStatusRequestDto = Instancio.of(ReadStatusCreateRequestDto.class)
//                .set(field(ReadStatusCreateRequestDto::getUserId), user.getId())
//                .set(field(ReadStatusCreateRequestDto::getChannelId), channel.getId())
//                .create();
//        var userStatusRequestDto = Instancio.of(UserStatusCreateRequestDto.class)
//                .set(field(UserStatusCreateRequestDto::getUserId), user.getId())
//                .create();
//        var binarayContentRequestDto = Instancio.of(BinaryContentCreateRequestDto.class).create();
//
//var readStatus = readStatusService.createReadStatus(readStatusRequestDto);
//var userStatus= userStatusService.createUserStatus(userStatusRequestDto);
//var binaryContent = binaryContentService.createBinaryContent(binarayContentRequestDto);
//
//var actualResultReadStatus = readStatusRepository.readReadStatus(readStatus.getId());
//var actualResultUserStatus = userStatusRepository.readUserStatus(userStatus.getId());
//var actualResultBinaryContent = binaryContentRepository.readBinaryContent(binaryContent.getId());
//        //then
//        assertThat(actualResultReadStatus.isPresent()).as("readstatus 생성 테스트").isTrue();
//        assertThat(actualResultUserStatus.isPresent()).as("userstatus 생성 테스트").isTrue();
//        assertThat(actualResultBinaryContent.isPresent()).as("binarycontent 생성 테스트").isTrue();
//    }
//
//    @Test
//    @DisplayName("[정상 케이스] - 스테이터스 업데이트")
//    void status_update_Test(){
//        //given
//        User user = userRepository.saveUser(Instancio.create(User.class));
//        Channel channel = channelRepository.saveChannel(Instancio.create(Channel.class));
//        Message message = messageRepository.saveMessage(Instancio.of(Message.class)
//                .set(field(Message::getSenderId), user.getId())
//                .create());
//        var readStatusRequestDto = Instancio.of(ReadStatusCreateRequestDto.class)
//                .set(field(ReadStatusCreateRequestDto::getUserId), user.getId())
//                .set(field(ReadStatusCreateRequestDto::getChannelId), channel.getId())
//                .create();
//        var userStatusRequestDto = Instancio.of(UserStatusCreateRequestDto.class)
//                .set(field(UserStatusCreateRequestDto::getUserId), user.getId())
//                .create();
//
//        var readStatus = readStatusService.createReadStatus(readStatusRequestDto);
//        var userStatus= userStatusService.createUserStatus(userStatusRequestDto);
//
//        var readStatusUpdateRequestDto = Instancio.of(new TypeToken<ReadStatusUpdateRequestDto<Instant>>() {
//                })
//                .set(field("type"), ReadStatusElement.READ_LAST_TIME)
//                        .set(field("readStatusId"), readStatus.getId())
//                                .create();
//        var userStatusUpdateRequestDto = Instancio.of(new TypeToken<UserStatusUpdateRequestDto<Instant>>() {})
//                .set(field("type"), UserStatusElement.LAST_ONLINE_TIME)
//                .set(field("userStatusId"), userStatus.getId())
//                .create();
//        //when
//        readStatusService.updateReadStatus(readStatusUpdateRequestDto);
//        userStatusService.updateUserStatus(userStatusUpdateRequestDto);
//        var actualResultReadStatus = readStatusRepository.readReadStatus(readStatus.getId()).orElseThrow();
//        var actualResultUserStatus = userStatusRepository.readUserStatus(userStatus.getId()).orElseThrow();
//        //then
//        assertThat(actualResultReadStatus.getReadLastTime()).as("readstatus 업데이트 테스트").isEqualTo(readStatusUpdateRequestDto.getUpdateValue());
//        assertThat(actualResultUserStatus.getLastOnlineTime()).as("userstatus 업데이트 테스트").isEqualTo(userStatusUpdateRequestDto.getUpdateValue());
//    }
//
//    @Test
//    @DisplayName("[정상 케이스] - 스테이터스 및 바이트 삭제")
//    void status_delete_Test(){
//        //given
//        User user = userRepository.saveUser(Instancio.create(User.class));
//        Channel channel = channelRepository.saveChannel(Instancio.create(Channel.class));
//        Message message = messageRepository.saveMessage(Instancio.of(Message.class)
//                .set(field(Message::getSenderId), user.getId())
//                .create());
//        var readStatusRequestDto = Instancio.of(ReadStatusCreateRequestDto.class)
//                .set(field(ReadStatusCreateRequestDto::getUserId), user.getId())
//                .set(field(ReadStatusCreateRequestDto::getChannelId), channel.getId())
//                .create();
//        var userStatusRequestDto = Instancio.of(UserStatusCreateRequestDto.class)
//                .set(field(UserStatusCreateRequestDto::getUserId), user.getId())
//                .create();
//        var binaryContentRequestDto = Instancio.of(BinaryContentCreateRequestDto.class).create();
//
//        var readStatus = readStatusService.createReadStatus(readStatusRequestDto);
//        var userStatus= userStatusService.createUserStatus(userStatusRequestDto);
//        var binaryContent = binaryContentService.createBinaryContent(binaryContentRequestDto);
//
//        //when
//        readStatusService.deleteReadStatus(readStatus.getId());
//        userStatusService.deleteUserStatus(userStatus.getId());
//        binaryContentService.deleteBinaryContent(binaryContent.getId());
//        //then
//        assertThatThrownBy(() -> readStatusService.readReadStatus(readStatus.getId()))
//                .as("readstatus 삭제 테스트")
//                .isInstanceOf(IllegalArgumentException.class);
//        assertThatThrownBy(() ->userStatusService.find(userStatus.getId()))
//                .as("userstatus 삭제 테스트")
//                .isInstanceOf(IllegalArgumentException.class);
//        assertThatThrownBy(() -> binaryContentService.find(binaryContent.getId()))
//                .as("binarycontent 삭제 테스트")
//                .isInstanceOf(IllegalArgumentException.class);
//    }
//}
