package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.subTable.MessageAttachment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;


public interface MessageAttachmentRepository extends JpaRepository<MessageAttachment, UUID> {
    MessageAttachment findByMessage(Message message);

    MessageAttachment findByBinaryContent(BinaryContent binaryContent);
}
