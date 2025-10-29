package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class Message extends BaseEntity {
    private static final long serialVersionUID = 1L;

    //Field
    private UUID id;
    private final UUID speakerId;             //화자 UUID
    private final UUID channelId;             //채널 UUID
    private String content;             //메세지 내용

    //Constructor
    public Message(UUID channelId, UUID speakerId, String content) {
        this.speakerId = speakerId;
        this.channelId = channelId;
        this.content = content;
    }

    public Message update(String content){
        this.content = content;
        return this;
    }

    //Getter
    public UUID getSpeakerId() {
        return speakerId;
    }

    public UUID getChannelId() {
        return channelId;
    }

    public String getContent() {
        return content;
    }
}
