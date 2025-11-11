package com.sprint.mission.discodeit.dto.response;

import com.sprint.mission.discodeit.entity.Message;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.HashSet;
import java.util.UUID;

@Getter
@Builder
public class MessageReadResponseDto {


    private UUID id;
    private Instant createdAt;
    private Instant updatedAt;
    private String content;
    private UUID authorId;
    private boolean isMarkDown;
    private UUID channelId;
    private HashSet<UUID> attachmentIds;

    public static MessageReadResponseDto from(Message message){
        return MessageReadResponseDto.builder()
                .content(message.getContent())
                .authorId(message.getSenderId())
                .isMarkDown(message.isMarkDown())
                .channelId(message.getChannelId())
                .attachmentIds(message.getAttachmentIdList())
                .id(message.getId())
                .updatedAt(message.getUpdatedAt())
                .createdAt(message.getCreatedAt())
                .build();
    }
}
