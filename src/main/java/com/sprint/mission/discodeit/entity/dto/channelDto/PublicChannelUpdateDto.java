package com.sprint.mission.discodeit.entity.dto.channelDto;

import lombok.NonNull;

public record PublicChannelUpdateDto(@NonNull String newName, String newDescription) {
}
