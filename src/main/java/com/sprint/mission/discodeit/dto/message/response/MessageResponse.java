package com.sprint.mission.discodeit.dto.message.response;

import com.sprint.mission.discodeit.dto.channel.response.ChannelFindResponse;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record MessageResponse(
         UUID messageId,
         UUID authorId,
         UUID channelId,
         Instant time,
         String content,
         List<UUID>attachmentIds
) {

    public static MessageResponse from(Message message) {
        return new MessageResponse(
                message.getId(),
                message.getAuthorId(),
                message.getChannelId(),
                message.getTime(),
                message.getContent(),
                message.getAttachmentIds()
        );
    }


}
