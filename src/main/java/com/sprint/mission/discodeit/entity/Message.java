package com.sprint.mission.discodeit.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Function;
@Getter
@ToString
@Setter
@Builder
public class Message extends Entity{

    private String content;
    private UUID senderId;
    private boolean isMarkDown;
    private UUID channelId;
    private HashSet<UUID> attachmentIdList;

//    public Message(String content, UUID senderId, boolean isMarkDown, UUID channelId) {
//        super();
//        this.content = content;
//        this.senderId = senderId;
//        this.isMarkDown = isMarkDown;
//        this.channelId = channelId;
//    }
//
//    public Message(UUID id, String content, UUID senderId, boolean isMarkDown, UUID channelId) {
//        super(id);
//        this.content = content;
//        this.senderId = senderId;
//        this.isMarkDown = isMarkDown;
//        this.channelId = channelId;
//    }






}
