package com.sprint.mission.discodeit.facade.message;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.jcf.JCFBinaryContentRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFMessageRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.basic.BasicBinaryContentService;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class MessageDeleteFacadeTest {
    private MessageDeleteFacade messageDeleteFacade;
    private MessageService messageService;
    private BinaryContentService binaryContentService;

    private JCFMessageRepository messageRepository;
    private JCFBinaryContentRepository binaryContentRepository;

    private UUID channelId;
    private byte[] attachmentData;

    @BeforeEach
    void setup() {
        // Repository
        messageRepository = new JCFMessageRepository();
        binaryContentRepository = new JCFBinaryContentRepository();

        // Service
        messageService = new BasicMessageService(messageRepository);
        binaryContentService = new BasicBinaryContentService(binaryContentRepository);

        // Facade
        messageDeleteFacade = new MessageDeleteFacade(messageService, binaryContentService);

        channelId = UUID.randomUUID();
        attachmentData = "sample attachment".getBytes();
    }

    @Test
    void deleteMessage_shouldRemoveMessageAndAttachments() {
        // 1. 첨부파일 생성
        BinaryContent attachment = binaryContentService.create(
                new BinaryContent(attachmentData, "file.txt", "text/plain")
        );

        // 2. 메시지 생성 (첨부파일 포함)
        Message message = new Message(channelId, UUID.randomUUID(), "Test message", List.of(attachment.getId()));
        messageService.create(message);

        // 데이터 저장 확인
        assertEquals(1, messageRepository.data.size());
        assertEquals(1, binaryContentRepository.data.size());

        // 3. 메시지 삭제
        messageDeleteFacade.deleteMessage(message.getId());

        // 4. 메시지 삭제 확인
        assertTrue(messageRepository.data.isEmpty());

        // 5. 첨부파일 삭제 확인
        assertTrue(binaryContentRepository.data.isEmpty());
    }

    @Test
    void deleteMessage_withoutAttachments_shouldRemoveMessageOnly() {
        // 1. 메시지 생성 (첨부파일 없음)
        Message message = new Message(channelId, UUID.randomUUID(), "No attachment message");
        messageService.create(message);

        // 데이터 저장 확인
        assertEquals(1, messageRepository.data.size());

        // 2. 메시지 삭제
        messageDeleteFacade.deleteMessage(message.getId());

        // 3. 메시지 삭제 확인
        assertTrue(messageRepository.data.isEmpty());
    }
}