package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.binaryContent.ProfileCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.message.MessageCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.message.MessageUpdateRequestDto;
import com.sprint.mission.discodeit.dto.request.user.UserCreateRequestDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entityElement.MessageElement;
import com.sprint.mission.discodeit.repository.*;
import org.assertj.core.api.Assertions;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BasicMessageServiceTest {
    private static final Logger log = LoggerFactory.getLogger(BasicMessageServiceTest.class);
    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private BasicMessageService basicMessageService;

    @Autowired
    private ChannelRepository channelRepository;

    @Autowired
    private BinaryContentRepository binaryContentRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        messageRepository.resetMessageRepository();
        channelRepository.resetChannelRepository();
        binaryContentRepository.resetBinaryContentRepository();
    }


    @Test
    @DisplayName("[정상 케이스] - 메시지 생성")
    void createMessage() {
        //given
        var user = userRepository.saveUser(User.builder().userName("testUser").name("테스트 유저").email("").password("").build());
        var channel = channelRepository.saveChannel(Channel.builder().name("테스트 채널").description("테스트 채널 설명").build());
        var profileDto1 = new ProfileCreateRequestDto("test.png".getBytes());
        var profileDto2 = new ProfileCreateRequestDto("sibal.png".getBytes());
        //when

        var message = basicMessageService.createMessage
                (new MessageCreateRequestDto("test message",
                        user.getId(), false, channel.getId()
                        ,new HashSet<>(List.of(profileDto1, profileDto2)))
                );
        var actualResult = messageRepository.getMessageById(message.getId());
        var actualChannelResult = channelRepository.getChannelById(channel.getId()).orElseThrow();
        //then
        assertThat(message).as("메세지 객체 확인").isEqualTo(actualResult.orElseThrow());
        assertThat(message.getAttachmentIdList()).size().as("binaryContentRepo 확인").isEqualTo(2);
        assertThat(actualChannelResult.getMessageIdList().size()).as("channelRepo 확인").isEqualTo(1);

    }

    @Test
    @DisplayName("[정상 케이스] - 메시지 읽기")
    void readMessage() {
        //given
        var user = userRepository.saveUser(User.builder().userName("testUser").name("테스트 유저").email("").password("").build());
        var channel = channelRepository.saveChannel(Channel.builder().name("테스트 채널").description("테스트 채널 설명").build());
        var profileDto1 = new ProfileCreateRequestDto("test.png".getBytes());
        var profileDto2 = new ProfileCreateRequestDto("test2.png".getBytes());
        var message = basicMessageService.createMessage
                (new MessageCreateRequestDto("test message",
                        user.getId(), false, channel.getId()
                        ,new HashSet<>(List.of(profileDto1, profileDto2)))
                );
        //when
        var actualResult = basicMessageService.readMessage(message);

        //then
        assertThat(actualResult.getContent()).isEqualTo(message.getContent());
    }

    @Test
    @DisplayName("[정상 케이스] - 메시지 삭제")
    void deleteMessage() {
        //given
        var user = userRepository.saveUser(User.builder().userName("testUser").name("테스트 유저").email("").password("").build());
        var channel = channelRepository.saveChannel(Channel.builder().name("테스트 채널").description("테스트 채널 설명").build());
        var profileDto1 = new ProfileCreateRequestDto("test.png".getBytes());
        var profileDto2 = new ProfileCreateRequestDto("test2.png".getBytes());
        var message = basicMessageService.createMessage
                (new MessageCreateRequestDto("test message",
                        user.getId(), false, channel.getId()
                        ,new HashSet<>(List.of(profileDto1, profileDto2)))
                );
        //when
        basicMessageService.deleteMessage(message.getId());
        var actualResultBinaryContent = binaryContentRepository.readAllBinaryContent();
        var actualResultChannel = channelRepository.getAllChannel().stream().noneMatch(c -> c.getMessageIdList().contains(message.getId()));
        var actualResultMessage = messageRepository.getAllMessage();
        //then
        assertThat(actualResultBinaryContent.size()).as("binaryContent 객체 확인").isEqualTo(0);
        assertThat(actualResultChannel).as("channel 객체 확인").isTrue();
        assertThat(actualResultMessage.size()).as("message 객체 확인").isEqualTo(0);

    }

    @Test
    @DisplayName("[정상 케이스] - 메시지 업데이트")
    void updateMessage() {
        //given
        var user = userRepository.saveUser(User.builder().userName("testUser").name("테스트 유저").email("").password("").build());
        var channel = channelRepository.saveChannel(Channel.builder().name("테스트 채널").description("테스트 채널 설명").build());
        var profileDto1 = new ProfileCreateRequestDto("test.png".getBytes());
        var profileDto2 = new ProfileCreateRequestDto("test2.png".getBytes());
        var message = basicMessageService.createMessage
                (new MessageCreateRequestDto("test message",
                        user.getId(), false, channel.getId()
                        ,new HashSet<>(List.of(profileDto1, profileDto2)))
                );
        //when

        basicMessageService.updateMessage(
                new MessageUpdateRequestDto<>
                        (message.getId(), MessageElement.CONTENT, "test message update"));
        var actualResult = messageRepository.getMessageById(message.getId());

        //then
        assertThat(actualResult.orElseThrow().getContent()).isEqualTo("test message update");
    }

    @Test
    @DisplayName("[정상 케이스] - 채널 메시지 조회")
    void findallByChannelId() {
        //given
        var user = userRepository.saveUser(User.builder().userName("testUser").name("테스트 유저").email("").password("").build());
        var channel = channelRepository.saveChannel(Channel.builder().name("테스트 채널").description("테스트 채널 설명").build());
        var profileDto1 = new ProfileCreateRequestDto("test.png".getBytes());
        var profileDto2 = new ProfileCreateRequestDto("test2.png".getBytes());
        var message = basicMessageService.createMessage
                (new MessageCreateRequestDto("test message",
                        user.getId(), false, channel.getId()
                        ,new HashSet<>(List.of(profileDto1, profileDto2)))
                );
        var message2 = basicMessageService.createMessage(
                new MessageCreateRequestDto("test message2",
                        user.getId(), false, channel.getId())
        );
        //when
        var actualResult = basicMessageService.findallByChannelId(channel.getId());

        //then
        assertThat(actualResult.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("[예외 케이스] - 없는 채널 id로 조회")
    void findallByChannelIdNotExistChannel(){
        //given

        var message = messageRepository.saveMessage(Instancio.of(Message.class).create());
        var message2 = messageRepository.saveMessage(Instancio.of(Message.class).create());
        //when & then
        assertThatThrownBy(()->basicMessageService.findallByChannelId(UUID.randomUUID()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}