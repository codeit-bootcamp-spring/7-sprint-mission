package com.sprint.mission.discodeit.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Message extends BaseEntity {
    private static final long serialVersionUID = 1L;

    //Field
    private UUID channelId;             //채널 UUID
    private UUID speakerId;             //화자 UUID
    private String content;                   //메세지 내용
    private List<UUID> attachmentIds;         //첨부 파일들 UUID


    //Constructor
    private Message(UUID channelId, UUID speakerId,
                   String content, List<UUID> attachmentIds) {
        this.channelId = channelId;
        this.speakerId = speakerId;
        this.content = content;
        this.attachmentIds = attachmentIds;
    }

    //Factory
    public static Message createText(UUID channelId, UUID speakerId, String content){
        return new Message(channelId, speakerId, content, List.of());
    }

    public static Message createWithAttachment(UUID channelId, UUID speakerId,
                                               String content, List<UUID> attachmentIds){
        return new Message(channelId, speakerId, content, attachmentIds);
    }

    //메세지 수정
    public Message update(String content,
                          List<UUID> attachmentIds){
        super.update();
        this.content = content;
        this.attachmentIds = attachmentIds;
        return this;
    }
}
