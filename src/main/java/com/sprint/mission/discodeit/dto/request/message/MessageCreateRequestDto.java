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

    private String content;
    private UUID senderId;
    private boolean isMarkDown;
    private UUID channelId;
    private HashSet<ProfileCreateRequestDto> attachmentIdList  = new HashSet<>();

}
