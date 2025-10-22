package com.sprint.mission.discodeit.entity.dto;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.MessageType;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

import static com.sprint.mission.discodeit.entity.dto.ChangeTime.changeTime;

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

    public UUID getId() { return id; }
    public String getAuthor() {
        return author;
    }
    public String getReceiver() {
        return receiver;
    }
    public String getContent() {
        return content;
    }
    public String getCreatedAt() {
        return createdAt;
    }
    public String getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public String toString() {

        String editSign = createdAt.equals(updatedAt) ? "" : " (수정됨)";
        return author + "   " + createdAt + "\n" + content  + editSign;
    }
}
