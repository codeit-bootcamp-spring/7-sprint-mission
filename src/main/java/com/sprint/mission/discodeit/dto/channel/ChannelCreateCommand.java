package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.entity.type.ChannelType;

import java.util.List;
import java.util.UUID;

public record ChannelCreateCommand(
        String title,
        String description,
        ChannelType type,
        List<UUID> memberIds,
        Boolean notificationEnabled
) {

    public static ChannelCreateCommand from(ChannelCreateRequestDto requestDto, ChannelType channelType) {
        boolean notification = channelType == ChannelType.PRIVATE;
        return new ChannelCreateCommand(
                requestDto.name(), // NOTE: 이와같이 요구하는 dto의 필드 이름과 비즈니스에서 사용하고자하는 필드가 다를시 Command, Params 사용 경계를 나눌수있는 이점이있다
                requestDto.description(),
                channelType,
                requestDto.participantIds() == null ? List.of() : requestDto.participantIds(),
                notification
        );
    }
}
