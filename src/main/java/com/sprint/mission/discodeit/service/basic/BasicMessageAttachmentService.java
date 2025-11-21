package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.service.MessageAttachmentService;
import com.sprint.mission.discodeit.subTable.MessageAttachment;

import java.util.UUID;

public class BasicMessageAttachmentService implements MessageAttachmentService {

    @Override
    public MessageAttachment save(UUID messageId, UUID binaryContentId) {
        return null;
    }

    @Override
    public MessageAttachment readByMessageId(UUID messageId) {
        return null;
    }

    @Override
    public MessageAttachment readByBinaryContentId(UUID binaryContentId) {
        return null;
    }

    @Override
    public void deleteByMessageId(UUID messageId) {

    }

    @Override
    public void deleteByBinaryContentId(UUID binaryContentId) {

    }
}
