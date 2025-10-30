package com.sprint.mission.discodeit.dto.request;

import java.util.UUID;

public record CreateReadStatusRequestDto (

    UUID userId, //유저 ID
    UUID channelId //채널 ID
) {}
