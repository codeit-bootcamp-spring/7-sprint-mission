package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.subTable.MessageAttachment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MessageAttachmentRepository extends JpaRepository<MessageAttachment, UUID> {
}
