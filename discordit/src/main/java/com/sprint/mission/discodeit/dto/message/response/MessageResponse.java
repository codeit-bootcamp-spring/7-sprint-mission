package com.sprint.mission.discodeit.dto.message.response;

import com.sprint.mission.discodeit.entity.base.Channel;
import com.sprint.mission.discodeit.entity.base.Message;
import com.sprint.mission.discodeit.entity.base.User;
import com.sprint.mission.discodeit.entity.content.BinaryContent;
import com.sprint.mission.discodeit.enums.ReceiverType;

import java.util.List;

public record MessageResponse(
        String senderId,
        ReceiverType type,
        String receiverId,
        String message,
        List<String> fileUrls
) {
    public static MessageResponse toDto(Message message) {
        ReceiverType type = ReceiverType.from(message.getReceiver());
        String receiverId;
        if (type == ReceiverType.USER) {
            receiverId = ((User) message.getReceiver()).getUserId();
        } else {
            receiverId = ((Channel) message.getReceiver()).getUuid().toString();
        }

        return new MessageResponse(
                message.getSender().getUserId(),
                type,
                receiverId,
                message.getMessage(),
                message.getAttachments().stream()
                        .map(BinaryContent::getFileUrl)
                        .toList()
        );
    }
}
