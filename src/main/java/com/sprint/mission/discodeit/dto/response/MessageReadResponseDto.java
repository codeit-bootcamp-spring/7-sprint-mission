package com.sprint.mission.discodeit.dto.response;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.util.StaticString;
import lombok.Builder;
import lombok.Getter;

import java.util.HashSet;
import java.util.UUID;

@Getter
@Builder
public class MessageReadResponseDto {
    private String content;
    private UUID senderId;
    private boolean isMarkDown;
    private UUID channelId;
    private HashSet<UUID> attachmentIdList;

    public static MessageReadResponseDto from(Message message){
        return MessageReadResponseDto.builder()
                .content(message.getContent())
                .senderId(message.getSenderId())
                .isMarkDown(message.isMarkDown())
                .channelId(message.getChannelId())
                .attachmentIdList(message.getAttachmentIdList())
                .build();
    }
}
