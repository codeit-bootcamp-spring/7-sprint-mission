package com.sprint.mission.discodeit.dto;

import java.util.UUID;

public record FindAllByChannelIdDto(
    UUID channelID,
    Pageable pageable) {
}
