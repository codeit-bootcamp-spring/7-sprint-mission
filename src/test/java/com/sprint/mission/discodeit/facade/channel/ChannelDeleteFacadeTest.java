package com.sprint.mission.discodeit.facade.channel;

import com.sprint.mission.discodeit.dto.channel.request.ChannelCreateReq;
import com.sprint.mission.discodeit.dto.channel.request.ChannelCreateSecReq;
import com.sprint.mission.discodeit.dto.message.request.MessageCreateReq;
import com.sprint.mission.discodeit.dto.user.request.UserCreateReq;
import com.sprint.mission.discodeit.dto.binarycontent.request.BinaryContentCreateReq;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.Message;
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

import java.util.List;
import java.util.UUID;


import static org.junit.jupiter.api.Assertions.*;

class ChannelDeleteFacadeTest {
    private ChannelDeleteFacade channelDeleteFacade;
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
        channelDeleteFacade = new ChannelDeleteFacade(channelService, messageService, readStatusService);

        // --- Test User ---
        UserCreateReq userReq = new UserCreateReq(
                "deleter@test.com",
                "DeleterNick",
                "password123",
                new BinaryContentCreateReq(null, null, null)
        );
        var testUser = userCreationFacade.createUser(userReq);
        managerId = testUser.getId();

        // --- Public Channel 생성 ---
        ChannelCreateReq pubReq = new ChannelCreateReq(managerId, "PublicDeleteTest", "Desc");
        publicChannel = channelService.create(pubReq);

        // --- Private Channel 생성 ---
        ChannelCreateSecReq privReq = new ChannelCreateSecReq(managerId, List.of(managerId));
        privateChannel = channelService.create(privReq);

        // --- 메시지 추가 ---
        messageCreationFacade.createMessage(new MessageCreateReq(publicChannel.getId(), managerId, "Hello 1", List.of()));
        messageCreationFacade.createMessage(new MessageCreateReq(publicChannel.getId(), managerId, "Hello 2", List.of()));
        messageCreationFacade.createMessage(new MessageCreateReq(privateChannel.getId(), managerId, "Private Msg 1", List.of()));

        // --- ReadStatus 추가 ---
        readStatusService.create(new ReadStatus(managerId, publicChannel.getId()));
        readStatusService.create(new ReadStatus(managerId, privateChannel.getId()));
    }

    @Test
    @DisplayName("공개 채널 삭제 시 연결된 Message, ReadStatus, Channel 모두 삭제되어야 함")
    void deletePublicChannel_shouldRemoveAllRelatedData() {
        UUID channelId = publicChannel.getId();

        assertFalse(messageService.findAllByChannelId(channelId).isEmpty());
        assertFalse(readStatusService.findAllByChannelId(channelId).isEmpty());
        assertNotNull(channelService.findById(channelId));

        channelDeleteFacade.deleteChannel(channelId);

        // ✅ 모두 삭제되었는지 검증
        assertTrue(messageService.findAllByChannelId(channelId).isEmpty());
        assertTrue(readStatusService.findAllByChannelId(channelId).isEmpty());
        assertThrows(Exception.class, () -> channelService.findById(channelId));
    }

    @Test
    @DisplayName("비공개 채널 삭제 시 연결된 Message, ReadStatus, Channel 모두 삭제되어야 함")
    void deletePrivateChannel_shouldRemoveAllRelatedData() {
        UUID channelId = privateChannel.getId();

        assertFalse(messageService.findAllByChannelId(channelId).isEmpty());
        assertFalse(readStatusService.findAllByChannelId(channelId).isEmpty());
        assertNotNull(channelService.findById(channelId));

        channelDeleteFacade.deleteChannel(channelId);

        // ✅ 모두 삭제되었는지 검증
        assertTrue(messageService.findAllByChannelId(channelId).isEmpty());
        assertTrue(readStatusService.findAllByChannelId(channelId).isEmpty());
        assertThrows(Exception.class, () -> channelService.findById(channelId));
    }
}