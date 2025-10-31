package com.sprint.mission.discodeit.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.UUID;

@Getter
@ToString
@Builder
public class Message extends BaseEntity {
    private static final long serialVersionUID = 1L;

    //Field
    private final UUID channelId;             //채널 UUID
    private final UUID speakerId;             //화자 UUID
    private String content;                   //메세지 내용
    private List<UUID> attachmentIds;         //첨부 파일들 UUID


    //Constructor
    public Message(UUID channelId, UUID speakerId,
                   String content, List<UUID> attachmentIds) {
        this.channelId = channelId;
        this.speakerId = speakerId;
        this.content = content;
        this.attachmentIds = attachmentIds;
    }

    public Message(UUID channelId, UUID speakerId, String content) {
        this(channelId, speakerId, content, List.of());
    }

    //메세지 수정
    public Message update(String content,
                          List<UUID> attachmentIds){
        super.update();
        this.content = content;
        this.attachmentIds = attachmentIds;
        return this;
    }

    public Message update(String content){
        update(content, this.attachmentIds);
        return this;
    }
}
