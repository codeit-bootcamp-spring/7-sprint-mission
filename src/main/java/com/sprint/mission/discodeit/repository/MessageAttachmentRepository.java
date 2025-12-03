package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.MessageAttachment;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MessageAttachmentRepository extends JpaRepository<MessageAttachment, UUID> {
    List<MessageAttachment> findAllByMessageId(UUID messageId);
}
