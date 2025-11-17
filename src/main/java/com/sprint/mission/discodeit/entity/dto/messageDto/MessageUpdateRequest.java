package com.sprint.mission.discodeit.entity.dto.messageDto;

import lombok.Builder;

@Builder
public record MessageUpdateRequest(String newContent) {
}
