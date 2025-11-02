package com.sprint.mission.discodeit.facade.message;

import com.sprint.mission.discodeit.dto.binarycontent.request.BinaryContentCreateReq;
import com.sprint.mission.discodeit.dto.message.request.MessageCreateReq;
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
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class MessageCreationFacadeTest {
    private MessageCreationFacade messageCreationFacade;
    private MessageService messageService;
    private BinaryContentService binaryContentService;
    private UserService userService;
    private UserCreationFacade userFacade;
    private UserStatusService userStatusService;

    private JCFMessageRepository messageRepository;
    private JCFBinaryContentRepository binaryContentRepository;
    private JCFUserRepository userRepository;
    private JCFUserStatusRepository userStatusRepository;

    private User testUser;
    private UUID channelId;
    private byte[] sampleAttachmentData;

    @BeforeEach
    void cleanRepositories() {
        if (messageRepository != null) messageRepository.data.clear();
        if (binaryContentRepository != null) binaryContentRepository.data.clear();
        if (userRepository != null) userRepository.data.clear();
    }

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
        userStatusService = new BasicUserStatusService(userStatusRepository);
        userService = new BasicUserService(userRepository);
        userFacade = new UserCreationFacade(userService, binaryContentService, userStatusService);

        // Facade
        messageCreationFacade = new MessageCreationFacade(messageService, binaryContentService, new MessageMapper(binaryContentService, userService));

        // Test user
        testUser = userFacade.createUser(new UserCreateReq("speaker@email.com", "SpeakerNick", "password",
                new BinaryContentCreateReq(null, null, null)));
        channelId = UUID.randomUUID();

        // Sample attachment
        sampleAttachmentData = "attachment data".getBytes(StandardCharsets.UTF_8);
    }

    @Test
    void createMessage_withoutAttachments_shouldCreateMessageOnly() {
        testUser = userFacade.createUser(new UserCreateReq("speaker@email.com", "SpeakerNick", "password",
                new BinaryContentCreateReq(null, null, null)));
        MessageCreateReq req = new MessageCreateReq(
                channelId,
                testUser.getId(),
                "Hello world!",
                List.of()
        );
        System.out.println(userRepository.data);
        MessageViewRes result = messageCreationFacade.createMessage(req);


        assertEquals("Hello world!", result.content());
        assertEquals("SpeakerNick", result.speaker());
        assertTrue(result.attachmentDatas().isEmpty());

        Message saved = messageService.findAllByChannelId(channelId).get(0);
        assertEquals("Hello world!", saved.getContent());
        assertTrue(saved.getAttachmentIds().isEmpty());
    }

    @Test
    void createMessage_withAttachments_shouldCreateMessageAndBinaryContent() {
        testUser = userFacade.createUser(new UserCreateReq("speaker@email.com", "SpeakerNick", "password",
                new BinaryContentCreateReq(null, null, null)));
        BinaryContentCreateReq attachmentReq = new BinaryContentCreateReq(
                sampleAttachmentData,
                "file.png",
                "image/png"
        );

        MessageCreateReq req = new MessageCreateReq(
                channelId,
                testUser.getId(),
                "Message with attachment",
                List.of(attachmentReq)
        );

        MessageViewRes result = messageCreationFacade.createMessage(req);

        // Message basic info
        assertEquals("Message with attachment", result.content());
        assertEquals("SpeakerNick", result.speaker());
        assertEquals(1, result.attachmentDatas().size());

        // Attachment 검증 (data 기반)
        byte[] returnedData = result.attachmentDatas().get(0).data();
        BinaryContent savedAttachment = binaryContentService.findAll().stream()
                .filter(b -> Arrays.equals(b.getData(), returnedData))
                .findFirst()
                .orElseThrow();

        assertArrayEquals(sampleAttachmentData, savedAttachment.getData());
        assertEquals("file.png", savedAttachment.getFileName());
        assertEquals("image/png", savedAttachment.getFileType());

        // Message 저장 검증
        Message savedMessage = messageService.findAllByChannelId(channelId).get(0);
        assertEquals(1, savedMessage.getAttachmentIds().size());
        assertEquals(savedAttachment.getId(), savedMessage.getAttachmentIds().get(0));
    }
}