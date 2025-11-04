package com.sprint.mission.discodeit.dto.channel.request;

import java.util.UUID;

public record ChannelCreateReq(
        UUID managerId,
        String name,
        String description
) {}
