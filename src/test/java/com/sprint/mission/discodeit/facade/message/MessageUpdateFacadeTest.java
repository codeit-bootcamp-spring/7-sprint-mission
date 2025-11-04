package com.sprint.mission.discodeit.facade.message;

import com.sprint.mission.discodeit.dto.binarycontent.request.BinaryContentUpdateReq;
import com.sprint.mission.discodeit.dto.message.request.MessageUpdateReq;
import com.sprint.mission.discodeit.dto.message.response.MessageViewRes;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.facade.mapper.MessageMapper;
import com.sprint.mission.discodeit.repository.jcf.JCFBinaryContentRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFMessageRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.basic.BasicBinaryContentService;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class MessageUpdateFacadeTest {
    private MessageUpdateFacade messageUpdateFacade;
    private MessageService messageService;
    private BinaryContentService binaryContentService;
    private MessageMapper messageMapper;

    private JCFMessageRepository messageRepository;
    private JCFBinaryContentRepository binaryContentRepository;

    private UUID channelId;
    private User testUser;
    private byte[] fileData;

    @BeforeEach
    void setup() {
        // 레포지토리 초기화
        messageRepository = new JCFMessageRepository();
        binaryContentRepository = new JCFBinaryContentRepository();
        JCFUserRepository userRepository = new JCFUserRepository();

        // 테스트 유저 생성
        testUser = new User("user@email.com", "Tester", "password");
        userRepository.save(testUser);

        // 서비스 초기화
        messageService = new BasicMessageService(messageRepository);
        binaryContentService = new BasicBinaryContentService(binaryContentRepository);
        UserService userService = new BasicUserService(userRepository); // 여기서 UserService 생성

        // Mapper & Facade
        messageMapper = new MessageMapper(binaryContentService, userService); // UserService 전달
        messageUpdateFacade = new MessageUpdateFacade(messageService, binaryContentService, messageMapper);

        // 테스트용 채널
        channelId = UUID.randomUUID();

        // 파일 데이터 준비
        fileData = "file data".getBytes(StandardCharsets.UTF_8);
    }

    @Test
    void updateMessage_shouldDeleteExistingFileWhenDataIsNull() {
        BinaryContent existingFile = binaryContentService.create(
                new BinaryContent(fileData, "old.png", "image/png")
        );

        Message message = messageService.create(
                new Message(channelId, testUser.getId(), "Original", new ArrayList<>(List.of(existingFile.getId())))
        );

        BinaryContentUpdateReq removeFileReq = new BinaryContentUpdateReq(existingFile.getId(), null, null, null);
        MessageUpdateReq updateReq = new MessageUpdateReq("Updated", List.of(removeFileReq));

        MessageViewRes result = messageUpdateFacade.updateMessage(message.getId(), updateReq);

        assertEquals("Updated", result.content());
        assertTrue(result.attachmentDatas().isEmpty());
        assertFalse(binaryContentRepository.existsById(existingFile.getId()));
    }

    @Test
    void updateMessage_shouldKeepExistingFileWhenDataIsNotNull() {
        BinaryContent existingFile = binaryContentService.create(
                new BinaryContent(fileData, "keep.png", "image/png")
        );

        Message message = messageService.create(
                new Message(channelId, testUser.getId(), "Original", new ArrayList<>(List.of(existingFile.getId())))
        );

        BinaryContentUpdateReq keepFileReq = new BinaryContentUpdateReq(existingFile.getId(), fileData, "keep.png", "image/png");
        MessageUpdateReq updateReq = new MessageUpdateReq("Updated", List.of(keepFileReq));

        MessageViewRes result = messageUpdateFacade.updateMessage(message.getId(), updateReq);

        assertEquals("Updated", result.content());
        assertEquals(1, result.attachmentDatas().size());
        assertTrue(binaryContentRepository.existsById(existingFile.getId()));
    }

    @Test
    void updateMessage_shouldAddNewFileWhenNoExistingAttachments() {
        Message message = messageService.create(
                new Message(channelId, testUser.getId(), "Original")
        );

        BinaryContentUpdateReq newFileReq = new BinaryContentUpdateReq(null, fileData, "new.png", "image/png");
        MessageUpdateReq updateReq = new MessageUpdateReq("Updated", List.of(newFileReq));

        MessageViewRes result = messageUpdateFacade.updateMessage(message.getId(), updateReq);

        assertEquals("Updated", result.content());
        assertEquals(1, result.attachmentDatas().size());

        BinaryContent savedNewFile = binaryContentService.findAll().get(0);
        assertArrayEquals(fileData, savedNewFile.getData());
        assertEquals("new.png", savedNewFile.getFileName());
    }
}