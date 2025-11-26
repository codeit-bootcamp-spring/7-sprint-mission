package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.MessageAttachmentRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.MessageAttachmentService;
import com.sprint.mission.discodeit.subTable.MessageAttachment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicMessageAttachmentService implements MessageAttachmentService {

    private final MessageAttachmentRepository messageAttachmentRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final MessageRepository messageRepository;

    @Override
    public MessageAttachment save(UUID messageId, UUID binaryContentId) {
        Message targetMessage = messageRepository.findById(messageId).orElseThrow();
        BinaryContent targetBinaryContent = binaryContentRepository.findById(binaryContentId).orElseThrow();

        MessageAttachment messageAttachment = new MessageAttachment(targetMessage, targetBinaryContent);
        return messageAttachmentRepository.save(messageAttachment);
    }

    @Override
    @Transactional(readOnly = true)
    public MessageAttachment readByMessageId(UUID messageId) {
        Message targetMessage = messageRepository.findById(messageId).orElseThrow();
        return messageAttachmentRepository.findByMessage(targetMessage);
    }

    @Override
    @Transactional(readOnly = true)
    public MessageAttachment readByBinaryContentId(UUID binaryContentId) {
        BinaryContent targetBinaryContent = binaryContentRepository.findById(binaryContentId).orElseThrow();
        return messageAttachmentRepository.findByBinaryContent(targetBinaryContent);
    }

    @Override
    public void deleteByMessageId(UUID messageId) {
        return;
    }

    @Override
    public void deleteByBinaryContentId(UUID binaryContentId) {
        return;
    }
}
