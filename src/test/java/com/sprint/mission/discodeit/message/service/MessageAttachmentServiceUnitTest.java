package com.sprint.mission.discodeit.message.service;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.MessageAttachmentRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.basic.BasicMessageAttachmentService;
import com.sprint.mission.discodeit.subTable.MessageAttachment;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
@DisplayName("MessageAttachmentService Unit Test")
public class MessageAttachmentServiceUnitTest {

    @Mock
    private MessageAttachmentRepository messageAttachmentRepository;

    @Mock
    private BinaryContentRepository binaryContentRepository;

    @Mock
    private MessageRepository messageRepository;

    @InjectMocks
    private BasicMessageAttachmentService messageAttachmentService;

    private Message message;
    private BinaryContent binaryContent;
    private UUID messageId;
    private UUID binaryContentId;

    @BeforeEach
    void setUp() {
        message = Message.createMessageFactory(
                "Hello",
                null,
                null
        );
        binaryContent = new BinaryContent(
                "fileName",
                "text/plain",
                10L
        );
        messageId = UUID.randomUUID();
        binaryContentId = UUID.randomUUID();
    }
    @Test
    @DisplayName("[정상 케이스] 메세지 부착물 생성 성공")
    void createMessageAttachment_Success() {

        given(messageRepository.findById(any(UUID.class)))
                .willReturn(Optional.of(message));

        given(binaryContentRepository.findById(any(UUID.class)))
                .willReturn(Optional.of(binaryContent));
        given(messageAttachmentRepository.save(any(MessageAttachment.class)))
                .willReturn(new MessageAttachment(message, binaryContent));

        MessageAttachment response = messageAttachmentService.save(messageId, binaryContentId);

        assertThat(response.getMessage()).isEqualTo(message);
        assertThat(response.getBinaryContent()).isEqualTo(binaryContent);

        then(messageAttachmentRepository)
                .should(times(1)).save(ArgumentCaptor.forClass(MessageAttachment.class).capture());

    }

    @Test
    @DisplayName("[정상 케이스] 메세지 부착물 조회 (메세지 ID) 성공")
    void readMessageAttachment_MessageId_Success() {
        given(messageRepository.findById(any(UUID.class)))
                .willReturn(Optional.of(message));
        given(messageAttachmentRepository.findByMessage(any(Message.class)))
                .willReturn(new MessageAttachment(message, binaryContent));

        MessageAttachment response = messageAttachmentService.readByMessageId(messageId);

        assertThat(response.getMessage()).isEqualTo(message);
        assertThat(response.getBinaryContent()).isEqualTo(binaryContent);

        then(messageAttachmentRepository)
                .should(times(1)).findByMessage(ArgumentCaptor.forClass(Message.class).capture());
    }

    @Test
    @DisplayName("[정상 케이스] 메세지 부착물 조회 (파일 ID) 성공")
    void readMessageAttachment_BinaryContentId_Success() {

        given(binaryContentRepository.findById(any(UUID.class)))
                .willReturn(Optional.of(binaryContent));
        given(messageAttachmentRepository.findByBinaryContent(any(BinaryContent.class)))
                .willReturn(new MessageAttachment(message, binaryContent));

        MessageAttachment response = messageAttachmentService.readByBinaryContentId(messageId);

        assertThat(response.getMessage()).isEqualTo(message);
        assertThat(response.getBinaryContent()).isEqualTo(binaryContent);

        then(messageAttachmentRepository)
                .should(times(1)).findByBinaryContent(ArgumentCaptor.forClass(BinaryContent.class).capture());


    }
}
