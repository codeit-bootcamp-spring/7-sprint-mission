package com.sprint.mission.discodeit.dto.message.response;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.enums.ReceiverType;

import java.util.List;

public record MessageResponse(
        String senderId,
        ReceiverType type,
        String receiverId,
        String receiverDisplayName,
        String message,
        List<String> fileUrls
) {
    public static MessageResponse toDto(Message message) {
        ReceiverType type = ReceiverType.from(message.getReceiver());
        String receiverId;
        String receiverDisplayName;
        if (type == ReceiverType.USER) {
            User receiver = (User) message.getReceiver();
            receiverId = receiver.getUserId();
            receiverDisplayName = receiver.getDisplayName();
        } else {
            Channel receiver = (Channel) message.getReceiver();
            receiverId = receiver.getUuid().toString();
            receiverDisplayName = receiver.getDisplayName();
        }

        return new MessageResponse(
                message.getSender().getUserId(),
                type,
                receiverId,
                receiverDisplayName,
                message.getMessage(),
                message.getAttachments().stream()
                        .map(BinaryContent::getFileUrl)
                        .toList()
        );
    }
}
