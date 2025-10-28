package com.sprint.mission.discodeit.dto.request.message;

import com.sprint.mission.discodeit.dto.request.binaryContent.ProfileCreateRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@Getter

public class MessageCreateRequestDto {

    private final String content;
    private final UUID senderId;
    private final boolean isMarkDown;
    private final UUID channelId;
    private HashSet<ProfileCreateRequestDto> attachmentIdList  = new HashSet<>();


    public MessageCreateRequestDto(String content, UUID senderId, boolean isMarkDown, UUID channelId) {
        this.content = content;
        this.senderId = senderId;
        this.isMarkDown = isMarkDown;
        this.channelId = channelId;
    }
    public MessageCreateRequestDto(String content, UUID senderId, boolean isMarkDown, UUID channelId, HashSet<ProfileCreateRequestDto> attachmentIdList) {
        this.content = content;
        this.senderId = senderId;
        this.isMarkDown = isMarkDown;
        this.channelId = channelId;
        this.attachmentIdList = attachmentIdList;
    }
}
