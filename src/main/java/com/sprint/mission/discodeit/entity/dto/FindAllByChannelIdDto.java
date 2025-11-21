package com.sprint.mission.discodeit.entity.dto;

import java.util.UUID;

public record FindAllByChannelIdDto(
    UUID channelID,
    Pageable pageable) {
}
