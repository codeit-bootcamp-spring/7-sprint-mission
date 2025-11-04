package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
public class Message extends BasicEntity{

    private String content; // 메시지
    private final UUID authorId; // 유저ID
    private final UUID channelId; // 채널ID
    private List<UUID> attachmentIds; // 첨부파일


    public Message(String content, UUID authorId, UUID channelId, List<UUID> attachmentIds) {
        super();
        this.content = content;
        this.authorId = authorId;
        this.channelId = channelId;
        this.attachmentIds = attachmentIds;
    }

    public void updateContent(String newContent) {
        this.content = newContent;
        update();
    }
}
