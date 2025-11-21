package com.sprint.mission.discodeit.dto.messageDto;

import lombok.Builder;

@Builder
public record MessageUpdateRequest(String newContent) {
}
