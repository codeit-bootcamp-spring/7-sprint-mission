package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.subTable.MessageAttachment;

import java.util.UUID;

public interface MessageAttachmentService {

    MessageAttachment save(UUID messageId, UUID binaryContentId);
    MessageAttachment readByMessageId(UUID messageId);
    MessageAttachment readByBinaryContentId(UUID binaryContentId);
    void deleteByMessageId(UUID messageId);
    void deleteByBinaryContentId(UUID binaryContentId);
}
