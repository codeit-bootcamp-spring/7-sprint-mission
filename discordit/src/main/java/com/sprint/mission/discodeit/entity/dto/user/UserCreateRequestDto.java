package com.sprint.mission.discodeit.entity.dto.user;

import com.sprint.mission.discodeit.entity.content.BinaryContent;
import com.sprint.mission.discodeit.enums.OnlineStatus;

import java.util.Optional;

public record UserCreateRequestDto(String id,
                                   String passwd,
                                   String displayName,
                                   Optional<BinaryContent> profileImage) {
}
