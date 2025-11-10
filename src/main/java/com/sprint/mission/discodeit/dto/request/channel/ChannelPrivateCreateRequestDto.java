package com.sprint.mission.discodeit.dto.request.channel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import static com.sprint.mission.discodeit.service.util.StaticString.*;


@Getter
@Builder
@NoArgsConstructor
public class ChannelPrivateCreateRequestDto {
    private HashSet<UUID> userIdList;
    private String name = DEFAULT_CHANNEL_NAME;
    private String description = DEFAULT_CHANNEL_DESCRIPTION;
    private boolean isTextChannel;

    public ChannelPrivateCreateRequestDto(
            HashSet<UUID> userIdList,
            String name,
            String description,
            boolean isTextChannel
    ) {
        this.userIdList = userIdList;
        this.name = (name != null) ? name : DEFAULT_CHANNEL_NAME;
        this.description = (description != null) ? description : DEFAULT_CHANNEL_DESCRIPTION;
        this.isTextChannel = isTextChannel;
    }

    // ✅ name, description 생략용 보조 생성자 (편의 생성자)
    public ChannelPrivateCreateRequestDto(
            HashSet<UUID> userIdList,
            boolean isTextChannel
    ) {
        this(userIdList, null, null, isTextChannel);
    }

}
