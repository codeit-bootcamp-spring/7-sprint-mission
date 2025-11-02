package com.sprint.mission.discodeit.facade.message;

import com.sprint.mission.discodeit.dto.binarycontent.request.BinaryContentCreateReq;
import com.sprint.mission.discodeit.dto.message.response.MessageViewRes;
import com.sprint.mission.discodeit.dto.user.request.UserCreateReq;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.facade.mapper.MessageMapper;
import com.sprint.mission.discodeit.facade.user.UserCreationFacade;
import com.sprint.mission.discodeit.repository.jcf.JCFBinaryContentRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFMessageRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserStatusRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import com.sprint.mission.discodeit.service.basic.BasicBinaryContentService;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import com.sprint.mission.discodeit.service.basic.BasicUserStatusService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class MessageOverviewFacadeTest {
    private MessageOverviewFacade messageOverviewFacade;
    private MessageService messageService;
    private BinaryContentService binaryContentService;
    private UserService userService;
    private UserCreationFacade userFacade;
    private UserStatusService userStatusService;

    private JCFMessageRepository messageRepository;
    private JCFBinaryContentRepository binaryContentRepository;
    private JCFUserRepository userRepository;
    private JCFUserStatusRepository userStatusRepository;

    private UUID channelId;
    private User testUser;
    private byte[] sampleAttachmentData;

    @BeforeEach
    void setup() {
        // Repository
        messageRepository = new JCFMessageRepository();
        binaryContentRepository = new JCFBinaryContentRepository();
        userRepository = new JCFUserRepository();
        userStatusRepository = new JCFUserStatusRepository();

        // Service
        messageService = new BasicMessageService(messageRepository);
        binaryContentService = new BasicBinaryContentService(binaryContentRepository);
        userService = new BasicUserService(userRepository);
        userStatusService = new BasicUserStatusService(userStatusRepository);
        userFacade = new UserCreationFacade(userService, binaryContentService, userStatusService); // UserStatusService는 필요 없음

        // Mapper
        MessageMapper messageMapper = new MessageMapper(binaryContentService, userService);

        // Facade
        messageOverviewFacade = new MessageOverviewFacade(messageService, messageMapper);

        // Test user
        testUser = userFacade.createUser(new UserCreateReq("user@email.com", "TestUser", "password", new BinaryContentCreateReq(null, null, null)));

        // Channel
        channelId = UUID.randomUUID();

        // Sample attachment
        sampleAttachmentData = "attachment data".getBytes(StandardCharsets.UTF_8);
    }

    @Test
    void findAllByChannelId_withMessages_shouldReturnAllMessages() {
        // 메시지 1: 첨부파일 없음
        Message message1 = messageService.create(new Message(channelId, testUser.getId(), "Hello world"));

        // 메시지 2: 첨부파일 있음
        BinaryContent attachment = binaryContentService.create(new BinaryContent(sampleAttachmentData, "file.png", "image/png"));
        Message message2 = messageService.create(new Message(channelId, testUser.getId(), "Message with attachment", List.of(attachment.getId())));

        // Facade 조회
        List<MessageViewRes> results = messageOverviewFacade.findAllByChannelId(channelId);

        // 결과 검증
        assertEquals(2, results.size());

        // 메시지1 검증
        MessageViewRes res1 = results.stream().filter(m -> m.content().equals("Hello world")).findFirst().orElseThrow();
        assertEquals("TestUser", res1.speaker());
        assertTrue(res1.attachmentDatas().isEmpty());

        // 메시지2 검증
        MessageViewRes res2 = results.stream().filter(m -> m.content().equals("Message with attachment")).findFirst().orElseThrow();
        assertEquals("TestUser", res2.speaker());
        assertEquals(1, res2.attachmentDatas().size());
        assertArrayEquals(sampleAttachmentData, res2.attachmentDatas().get(0).data());
        assertEquals("file.png", res2.attachmentDatas().get(0).fileName());
        assertEquals("image/png", res2.attachmentDatas().get(0).fileType());
    }

    @Test
    void findAllByChannelId_noMessages_shouldReturnEmptyList() {
        List<MessageViewRes> results = messageOverviewFacade.findAllByChannelId(channelId);
        assertTrue(results.isEmpty());
    }
}