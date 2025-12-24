package com.sprint.mission.discodeit.dto.entity.channel.response;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public record ChannelParticipantIdsResponse (
        List<UUID> participantIds
) {
    public static ChannelParticipantIdsResponse toDto(Channel channel) {
        return new ChannelParticipantIdsResponse(
                channel.getParticipants().stream()
                        .map(User::getId)
                        .toList()
        );
    }
}
