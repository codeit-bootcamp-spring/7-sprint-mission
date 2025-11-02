package com.sprint.mission.discodeit.facade.channel;


import com.sprint.mission.discodeit.dto.binarycontent.request.BinaryContentCreateReq;
import com.sprint.mission.discodeit.dto.channel.request.ChannelCreateReq;
import com.sprint.mission.discodeit.dto.channel.request.ChannelCreateSecReq;
import com.sprint.mission.discodeit.dto.channel.response.ChannelPrivateInfoRes;
import com.sprint.mission.discodeit.dto.channel.response.ChannelPublicInfoRes;
import com.sprint.mission.discodeit.dto.message.request.MessageCreateReq;
import com.sprint.mission.discodeit.dto.user.request.UserCreateReq;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.facade.mapper.ChannelMapper;
import com.sprint.mission.discodeit.facade.mapper.MessageMapper;
import com.sprint.mission.discodeit.facade.message.MessageCreationFacade;
import com.sprint.mission.discodeit.facade.user.UserCreationFacade;
import com.sprint.mission.discodeit.repository.jcf.*;
import com.sprint.mission.discodeit.service.*;
import com.sprint.mission.discodeit.service.basic.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ChannelDetailViewFacadeTest {
    private ChannelDetailViewFacade channelDetailViewFacade;
    private ChannelCreationFacade channelCreationFacade;
    private MessageCreationFacade messageCreationFacade;
    private UserCreationFacade userCreationFacade;

    private JCFChannelRepository channelRepository;
    private JCFMessageRepository messageRepository;
    private JCFReadStatusRepository readStatusRepository;
    private JCFUserRepository userRepository;
    private JCFUserStatusRepository userStatusRepository;
    private JCFBinaryContentRepository binaryContentRepository;

    private ChannelService channelService;
    private MessageService messageService;
    private ReadStatusService readStatusService;
    private UserService userService;
    private UserStatusService userStatusService;
    private BinaryContentService binaryContentService;

    private ChannelMapper channelMapper;
    private MessageMapper messageMapper;

    private Channel publicChannel;
    private Channel privateChannel;
    private UUID managerId;

    @BeforeEach
    void setUp() {
        // --- Repository ---
        channelRepository = new JCFChannelRepository();
        messageRepository = new JCFMessageRepository();
        readStatusRepository = new JCFReadStatusRepository();
        userRepository = new JCFUserRepository();
        userStatusRepository = new JCFUserStatusRepository();
        binaryContentRepository = new JCFBinaryContentRepository();

        // --- Service ---
        userService = new BasicUserService(userRepository);
        userStatusService = new BasicUserStatusService(userStatusRepository);
        binaryContentService = new BasicBinaryContentService(binaryContentRepository);
        channelService = new BasicChannelService(channelRepository);
        messageService = new BasicMessageService(messageRepository);
        readStatusService = new BasicReadStatusService(readStatusRepository);

        // --- Mapper ---
        channelMapper = new ChannelMapper(messageService);
        messageMapper = new MessageMapper(binaryContentService, userService);

        // --- Facade ---
        userCreationFacade = new UserCreationFacade(userService, binaryContentService, userStatusService);
        channelCreationFacade = new ChannelCreationFacade(channelService, readStatusService, channelMapper);
        messageCreationFacade = new MessageCreationFacade(messageService, binaryContentService, messageMapper);
        channelDetailViewFacade = new ChannelDetailViewFacade(channelService, channelMapper);

        // --- Test User ---
        UserCreateReq userReq = new UserCreateReq(
                "view@test.com",
                "ViewerNick",
                "password123",
                new BinaryContentCreateReq(null, null, null)
        );
        var testUser = userCreationFacade.createUser(userReq);
        managerId = testUser.getId();

        // --- Public Channel 생성 ---
        ChannelCreateReq pubReq = new ChannelCreateReq(managerId, "PublicViewTest", "This is public channel");
        publicChannel = channelService.create(pubReq);

        // --- Private Channel 생성 ---
        ChannelCreateSecReq privReq = new ChannelCreateSecReq(managerId, List.of(managerId));
        privateChannel = channelService.create(privReq);

        // --- 메시지 추가 ---
        messageCreationFacade.createMessage(new MessageCreateReq(publicChannel.getId(), managerId, "Hello 1", List.of()));
        messageCreationFacade.createMessage(new MessageCreateReq(publicChannel.getId(), managerId, "Hello 2", List.of()));
        messageCreationFacade.createMessage(new MessageCreateReq(privateChannel.getId(), managerId, "Secret Message", List.of()));
    }

    @Test
    @DisplayName("공개 채널 조회 시 마지막 메시지 시간 포함하여 반환되어야 함")
    void findByName_PublicChannel_ShouldReturnWithLastMessageTime() {
        var res = channelDetailViewFacade.findByName("PublicViewTest");
        assertTrue(res instanceof ChannelPublicInfoRes);

        ChannelPublicInfoRes publicRes = (ChannelPublicInfoRes) res;

        // ✅ lastMessageTime 이 null 아님
        assertNotNull(publicRes.lastMessageTime(), "lastMessageTime should not be null");

        // ✅ 실제 저장된 마지막 메시지 시간과 동일해야 함
        Instant expectedLastMessageTime = messageService.findLastMessageByChannelId(publicChannel.getId()).getCreatedAt();
        assertEquals(expectedLastMessageTime, publicRes.lastMessageTime());

        // ✅ 레포지토리 내 데이터 존재 확인
        assertTrue(channelRepository.existsById(publicChannel.getId()));
        assertFalse(messageRepository.findAllByChannelId(publicChannel.getId()).isEmpty());
    }

    @Test
    @DisplayName("비공개 채널 조회 시 마지막 메시지 시간 포함하여 반환되어야 함")
    void findByName_PrivateChannel_ShouldReturnWithLastMessageTime() {
        System.out.println(privateChannel);
        var res = channelDetailViewFacade.findByName(privateChannel.getName());
        assertTrue(res instanceof ChannelPrivateInfoRes);

        ChannelPrivateInfoRes privateRes = (ChannelPrivateInfoRes) res;

        assertNotNull(privateRes.lastMessageTime(), "lastMessageTime should not be null");

        Instant expectedLastMessageTime = messageService.findLastMessageByChannelId(privateChannel.getId()).getCreatedAt();
        assertEquals(expectedLastMessageTime, privateRes.lastMessageTime());

        // ✅ 채널과 메시지 데이터 검증
        assertTrue(channelRepository.existsById(privateChannel.getId()));
        assertFalse(messageRepository.findAllByChannelId(privateChannel.getId()).isEmpty());
    }

    @Test
    @DisplayName("존재하지 않는 채널명으로 조회 시 null 반환")
    void findByName_NonExistChannel_ShouldReturnNull() {
        var result = channelDetailViewFacade.findByName("NotExistChannelName");
        assertNull(result, "존재하지 않는 채널이면 null 이어야 함");
    }
}