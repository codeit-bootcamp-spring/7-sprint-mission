package com.sprint.mission.discodeit.entity.dto.channelDto;

import lombok.NonNull;

public record PublicChannelUpdateRequest(@NonNull String newName, String newDescription) {
}
