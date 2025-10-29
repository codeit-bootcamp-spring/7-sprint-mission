package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@ToString
public class Message extends BaseEntity {
    private static final long serialVersionUID = 1L;

    //Field
    private final UUID channelId;             //채널 UUID
    private final UUID speakerId;             //화자 UUID
    private String content;             //메세지 내용

    //Constructor
    public Message(UUID channelId, UUID speakerId, String content) {
        this.channelId = channelId;
        this.speakerId = speakerId;
        this.content = content;
    }

    public Message update(String content){
        super.update();
        this.content = content;
        return this;
    }
}
