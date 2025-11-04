package com.sprint.mission.discodeit.facade.channel;

import com.sprint.mission.discodeit.dto.binarycontent.request.BinaryContentCreateReq;
import com.sprint.mission.discodeit.dto.channel.request.ChannelCreateReq;
import com.sprint.mission.discodeit.dto.channel.request.ChannelCreateSecReq;
import com.sprint.mission.discodeit.dto.channel.response.ChannelInfoRes;
import com.sprint.mission.discodeit.dto.channel.response.ChannelPrivateInfoRes;
import com.sprint.mission.discodeit.dto.channel.response.ChannelPublicInfoRes;
import com.sprint.mission.discodeit.dto.message.request.MessageCreateReq;
import com.sprint.mission.discodeit.dto.user.request.UserCreateReq;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.facade.mapper.ChannelMapper;
import com.sprint.mission.discodeit.facade.mapper.MessageMapper;
import com.sprint.mission.discodeit.facade.message.MessageCreationFacade;
import com.sprint.mission.discodeit.facade.user.UserCreationFacade;
import com.sprint.mission.discodeit.factory.UserFactory;
import com.sprint.mission.discodeit.repository.jcf.*;
import com.sprint.mission.discodeit.service.*;
import com.sprint.mission.discodeit.service.basic.*;
import com.sprint.mission.discodeit.util.DateTimeUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ChannelCreationFacadeTest {
    private ChannelCreationFacade channelFacade;
    private ChannelService channelService;
    private ReadStatusService readStatusService;
    private ChannelMapper channelMapper;
    private MessageCreationFacade messageFacade;
    private MessageService messageService;
    private MessageMapper messageMapper;
    private BinaryContentService binaryContentService;
    private UserService userService;
    private UserCreationFacade userFacade;
    private UserStatusService userStatusService;

    private JCFChannelRepository channelRepository;
    private JCFMessageRepository messageRepository;
    private JCFBinaryContentRepository binaryContentRepository;
    private JCFUserRepository userRepository;
    private JCFUserStatusRepository userStatusRepository;
    private JCFReadStatusRepository readStatusRepository;

    private User testUser;

    @BeforeEach
    void setup() {
        // 레포지토리 초기화
        userRepository = new JCFUserRepository();
        userStatusRepository = new JCFUserStatusRepository();
        binaryContentRepository = new JCFBinaryContentRepository();
        channelRepository = new JCFChannelRepository();
        messageRepository = new JCFMessageRepository();
        readStatusRepository = new JCFReadStatusRepository();


        // 서비스 생성
        userService = new BasicUserService(userRepository);
        binaryContentService = new BasicBinaryContentService(binaryContentRepository);
        messageService = new BasicMessageService(messageRepository);
        channelService = new BasicChannelService(channelRepository);
        readStatusService = new BasicReadStatusService(readStatusRepository);
        messageMapper = new MessageMapper(binaryContentService, userService);
        channelMapper = new ChannelMapper(messageService);
        userStatusService = new BasicUserStatusService(userStatusRepository);

        // Facade 생성
        userFacade = new UserCreationFacade(userService, binaryContentService, userStatusService);
        messageFacade = new MessageCreationFacade(messageService, binaryContentService, messageMapper);
        channelFacade = new ChannelCreationFacade(channelService, readStatusService, channelMapper);

        // 테스트용 유저 생성
        UserCreateReq userReq = new UserCreateReq("test@nadfj.com", "TestNick", "password123",
                new BinaryContentCreateReq(null, null, null));
        testUser = userFacade.createUser(userReq);
    }

    @Test
    void createPublicChannel_noMessage_shouldReturnPublicRes() {
        // 공개 채널 생성
        ChannelCreateReq req = new ChannelCreateReq(testUser.getId(), "PublicChannel", "Public Description");
        ChannelInfoRes res = channelFacade.createPublicChannel(req);

        assertNotNull(res);
        assertEquals("public", res instanceof com.sprint.mission.discodeit.dto.channel.response.ChannelPublicInfoRes
                ? ((com.sprint.mission.discodeit.dto.channel.response.ChannelPublicInfoRes) res).type()
                : null);
    }

    @Test
    void createPublicChannel_withMessages_shouldReturnPublicResWithLastMessage() {
        // 공개 채널 생성
        ChannelCreateReq req = new ChannelCreateReq(testUser.getId(), "PublicChannelWithMsgs", "Desc");
        ChannelInfoRes res = channelFacade.createPublicChannel(req);

        Channel channel = channelRepository.findAll().get(0);

        // 메시지 여러 개 생성
        MessageCreateReq msg1 = new MessageCreateReq(channel.getId(), testUser.getId(), "Hello World 1", List.of());
        MessageCreateReq msg2 = new MessageCreateReq(channel.getId(), testUser.getId(), "Hello World 2", List.of());
        messageFacade.createMessage(msg1);
        messageFacade.createMessage(msg2);
        // Mapper에서 마지막 메시지 시간 가져오기
        ChannelPublicInfoRes publicRes = ChannelPublicInfoRes.from(channel, messageService.findLastMessageByChannelId(channel.getId()).getCreatedAt());

        assertNotNull(publicRes.lastMessageTime());
        assertEquals(messageService.findLastMessageByChannelId(channel.getId()).getCreatedAt(),
                publicRes.lastMessageTime());
    }

    @Test
    void createPrivateChannel_shouldReturnPrivateRes() {
        // 비공개 채널 생성
        ChannelCreateSecReq req = new ChannelCreateSecReq(testUser.getId(), List.of(testUser.getId()));
        ChannelInfoRes res = channelFacade.createPrivateChannel(req);

        assertNotNull(res);
        assertTrue(res instanceof com.sprint.mission.discodeit.dto.channel.response.ChannelPrivateInfoRes);
        com.sprint.mission.discodeit.dto.channel.response.ChannelPrivateInfoRes privateRes =
                (com.sprint.mission.discodeit.dto.channel.response.ChannelPrivateInfoRes) res;
        assertEquals(List.of(testUser.getId()), privateRes.users());
    }
}