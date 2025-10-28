package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.channel.ChannelPrivateCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.channel.ChannelPublicCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.channel.ChannelUpdateRequestDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entityElement.ChannelElement;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import org.assertj.core.api.Assertions;
import org.instancio.Instancio;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.invoke.VarHandle;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class BasicChannelServiceTest {

    private static final Logger log = LoggerFactory.getLogger(BasicChannelServiceTest.class);
    @Autowired
    private ChannelRepository channelRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReadStatusRepository readStatusRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private BasicChannelService basicChannelService;
    @Autowired
    private ChannelService channelService;


    @BeforeEach
    void clearRepositories() {
        channelRepository.resetChannelRepository();
        userRepository.resetUserRepository();
        readStatusRepository.resetRepository();
        messageRepository.resetMessageRepository();
    }
    @Test
    @DisplayName("[정상 케이스] - 프라이빗 채널 생성")
    void createPrivateChannel() {
        //given

        var user1 = userRepository.saveUser(User.builder().userName("testUser").name("테스트 유저").email("").password("").build());
        var dto = new ChannelPrivateCreateRequestDto(new HashSet<>(List.of(user1.getId())), "테스트 채널", "테스트 채널 설명", false);
        //when
        var actualResultChannel = basicChannelService.createPrivateChannel(dto);
        var actualResultReadStatus = readStatusRepository.readAllReadStatus();

        //then
        assertThat(basicChannelService.readChannel(actualResultChannel.getId())).isNotNull();
        assertThat(actualResultReadStatus.size()).isEqualTo(1);

    }

    @Test
    @DisplayName( "[정상 케이스] - 퍼블릿 채널 생성")
    void createPublicChannel() {
        //given
        var user1 = userRepository.saveUser(User.builder().userName("testUser").name("테스트 유저").email("").password("").build());
        var dto = new ChannelPublicCreateRequestDto(new HashSet<>(List.of(user1.getId())), "테스트 채널", "테스트 채널 설명",false);
        //when
        var actualResultChannel = basicChannelService.createPublicChannel(dto);
        var actualResultReadStatus = readStatusRepository.readAllReadStatus();

        //then
        assertThat(basicChannelService.readChannel(actualResultChannel.getId())).isNotNull();
        assertThat(basicChannelService.readChannel(actualResultChannel.getId()).isPublic()).isTrue();
        assertThat(actualResultReadStatus.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("[정상 케이스] - 채널 조회")
    void readChannel() {
        //given
        var dto =  new ChannelPublicCreateRequestDto(new HashSet<>(), "테스트 채널", "테스트 채널 설명",false);
        var expectedResult = basicChannelService.createPublicChannel(dto);
        //when
        var actualResult = basicChannelService.readChannel(expectedResult.getId());
        //then
        assertThat(actualResult.getName()).isEqualTo(expectedResult.getName());
    }

    @Test
    @DisplayName("[정상 케이스] - 유저 id로 채널 조회 ")
    void findAllByUserId() {
        //given
        var user1 = userRepository.saveUser(User.builder().userName("testUser").name("테스트 유저").email("").password("").build());
        var user2 = userRepository.saveUser(Instancio.create(User.class));
        var dto = new ChannelPublicCreateRequestDto(new HashSet<>(List.of(user1.getId())), "테스트 채널", "테스트 채널 설명",false);
        var dto2 = new ChannelPrivateCreateRequestDto(new HashSet<>(List.of(user1.getId())), "테스트 채널2", "테스트 채널 설명", false);

        basicChannelService.createPrivateChannel(dto2);
        basicChannelService.createPublicChannel(dto);
        basicChannelService.createPrivateChannel(Instancio.of(ChannelPrivateCreateRequestDto.class)
                .set(field(ChannelPrivateCreateRequestDto::getUserIdList)
                        , new HashSet<>(List.of(user2.getId())))
                .create()
        );

        //when
        var actualResult = basicChannelService.findAllByUserId(user1.getId());

        //then
        assertThat(actualResult.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("[정상 케이스] - 채널 삭제 ")
    void deleteChannel() {
        //given
        var user1 = userRepository.saveUser(User.builder().userName("testUser").name("테스트 유저").email("").password("").build());
        var dto2 = new ChannelPrivateCreateRequestDto(new HashSet<>(List.of(user1.getId())), "테스트 채널2", "테스트 채널 설명", false);

        var channel1 = basicChannelService.createPrivateChannel(dto2);
        //when
        basicChannelService.deleteChannel(channel1.getId());
        var actualResultChannel = channelRepository.getAllChannel();
        var actualResultReadStatus = readStatusRepository.readAllReadStatus();
        var actualResultUser = userRepository.getUserById(user1.getId()).orElseThrow();
        //then
        assertThat(actualResultChannel.size()).isEqualTo(0);
        assertThat(actualResultReadStatus.size()).isEqualTo(0);
       assertThat(actualResultUser.getJoinChannelList().size()).isEqualTo(0);

    }

    @Test
    @DisplayName("[정상 케이스] - 채널 업데이트 ")
    void updateChannel() {
        //given
        var dto1 = new ChannelPublicCreateRequestDto(null, "테스트 채널", "테스트 채널 설명",false);
        var channel1 = basicChannelService.createPublicChannel(dto1);

        //when
        basicChannelService.updateChannel(new ChannelUpdateRequestDto<>(channel1.getId(), ChannelElement.NAME, "테스트 채널업데이트"));
        var actualResult = basicChannelService.readChannel(channel1.getId());
        //then
        assertThat(actualResult.getName()).isEqualTo("테스트 채널업데이트");
    }


    @Test
    @DisplayName("[정상 케이스] - 유저 초대")
    void inviteUserToChannel() {
        //given
        var user1 = userRepository.saveUser(User.builder().userName("testUser").name("테스트 유저").email("").password("").build());
        var dto2 = new ChannelPrivateCreateRequestDto(new HashSet<>(List.of(user1.getId())), "테스트 채널2", "테스트 채널 설명", false);
        var user2 = userRepository.saveUser(User.builder().userName("testUser2").name("테스트 유저2").email("").password("").build());

        var channel1 = channelService.createPrivateChannel(dto2);
        //when
        basicChannelService.inviteUserToChannel(user2.getId(),channel1.getId());
        var actualResult = basicChannelService.readChannel(channel1.getId());

        //then
        assertThat(actualResult.getUserIdList().size()).isEqualTo(2);
    }

    @Test
    @DisplayName("[정상 케이스] - 유저 회원탈퇴")
    void deleteUserFromChannel() {
        //given
        var user1 = userRepository.saveUser(User.builder().userName("testUser").name("테스트 유저").email("").password("").build());
        var channel1 = channelService.createPrivateChannel(new ChannelPrivateCreateRequestDto
                (new HashSet<>
                        (List.of(user1.getId())), "테스트 채널2", "테스트 채널 설명", false));
        //when
        basicChannelService.deleteUserFromChannel(user1.getId(),channel1.getId());
        var actualResult = basicChannelService.readChannel(channel1.getId());
        //then
        assertThat(actualResult.getUserIdList().isEmpty()).isTrue();
    }

    @RepeatedTest(value = 10, name = "{displayName} {currentRepetition}/{totalRepetitions}")
    @DisplayName("[정상 케이스] - 가장 최근 메세지 조회 ")
    void read_last_message(){
        //given
        var user1 = userRepository.saveUser(Instancio.create(User.class));
        var channel = basicChannelService.createPublicChannel(Instancio.of(ChannelPublicCreateRequestDto.class)
                .set(field(ChannelPublicCreateRequestDto::getUserIdList), new HashSet<>(List.of(user1.getId())))
                .create()
        );
        var message1 = messageRepository.saveMessage(Instancio.of(com.sprint.mission.discodeit.entity.Message.class)
                .set(field(com.sprint.mission.discodeit.entity.Message::getChannelId), channel.getId())
                .set(field(com.sprint.mission.discodeit.entity.Message::getSenderId), user1.getId())
                .create()
        );
        var message3 = messageRepository.saveMessage(Instancio.of(com.sprint.mission.discodeit.entity.Message.class)
                .set(field(com.sprint.mission.discodeit.entity.Message::getChannelId), channel.getId())
                .set(field(com.sprint.mission.discodeit.entity.Message::getSenderId), user1.getId())
                .create()
        );
        var message2 = messageRepository.saveMessage(Instancio.of(com.sprint.mission.discodeit.entity.Message.class)
        .set(field(com.sprint.mission.discodeit.entity.Message::getChannelId), channel.getId())
        .set(field(com.sprint.mission.discodeit.entity.Message::getSenderId), user1.getId())
        .create()
        );

        var recentTime = List.of(message1,message2,message3).stream().map(x->x.getUpdatedAt()).max(Comparator.naturalOrder()).orElseThrow();
        //when
        var actualResult = basicChannelService.readChannel(channel.getId()).getRecentPostTime();
        //then
        assertThat(actualResult).isEqualTo(recentTime);
    }


    @RepeatedTest(value = 10, name = "{displayName} {currentRepetition}/{totalRepetitions}")
    @DisplayName("[예외 케이스] - 프라이빗 채널 변경 ")
    void update_private_channel_error() {
        //given
        var user1 = userRepository.saveUser(Instancio.create(User.class));
        var channel = channelRepository.saveChannel(Instancio.of(Channel.class)
                .set(field(Channel::isPublic),false)
                .create()
        );

        //when & then
        assertThatThrownBy(
                ()-> basicChannelService.updateChannel(new ChannelUpdateRequestDto<>(channel.getId(), ChannelElement.NAME, "테스트 채널업데이트"))
        ).isInstanceOf(IllegalArgumentException.class);
    }

}