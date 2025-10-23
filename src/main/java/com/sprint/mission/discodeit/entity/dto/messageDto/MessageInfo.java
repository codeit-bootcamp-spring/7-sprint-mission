package com.sprint.mission.discodeit.entity.dto.messageDto;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.MessageType;
import lombok.Getter;

import java.util.UUID;

import static com.sprint.mission.discodeit.entity.dto.ChangeTime.changeTime;

@Getter
public class MessageInfo {

    private final UUID id;
    private final String author;
    private final String receiver;
    private final String content;
    private final String createdAt;
    private final String updatedAt;

    public MessageInfo(Message message) {
        this.id = message.getId();
        this.author = message.getAuthor().getUserName();
        this.content = message.getContent();
        this.createdAt = changeTime(message.getCreatedAt());
        this.updatedAt = changeTime(message.getUpdatedAt());
        this.receiver = message.getType() == MessageType.DIRECT ?
                message.getReceiver().getUserName() :
                message.getChannel().getChannelName();
    }

    @Override
    public String toString() {

        String editSign = createdAt.equals(updatedAt) ? "" : " (수정됨)";
        return author + "   " + createdAt + "\n" + content  + editSign;
    }
}
