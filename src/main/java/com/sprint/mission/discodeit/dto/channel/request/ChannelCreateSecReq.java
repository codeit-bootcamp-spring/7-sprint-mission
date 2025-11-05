package com.sprint.mission.discodeit.dto.channel.request;

import java.util.List;
import java.util.UUID;

public record ChannelCreateSecReq(
        UUID managerId,
        List<UUID> userIds
) {}
