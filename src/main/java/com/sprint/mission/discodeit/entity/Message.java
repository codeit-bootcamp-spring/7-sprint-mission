package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.entityType.MessageType;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@ToString
public class Message extends BaseEntity {
    private final User author;
    private String content;

    private final User receiver;
    private final Channel channel;

    private final MessageType type;

    // 첨부파일 추가, 필수 아님
    private List<UUID> attachmentIds;

    public Message(User author, User receiver, String content, List<UUID> attachmentIds) {
        super();
        this.author = author;
        this.receiver = receiver;
        this.content = content;
        this.type = MessageType.DIRECT;
        this.channel = null;
        this.attachmentIds = attachmentIds != null ? attachmentIds : new ArrayList<>();
    }   // DM

    public Message(User author, Channel channel, String content, List<UUID> attachmentIds) {
        super();
        this.author = author;
        this.receiver = null;
        this.content = content;
        this.channel = channel;
        this.type = MessageType.CHANNEL;
        this.attachmentIds = attachmentIds != null ? attachmentIds : new ArrayList<>();
    }   // CM

    public void updateContent(String content) {
        this.content = content;
        updateTimestamp();
    }
}



