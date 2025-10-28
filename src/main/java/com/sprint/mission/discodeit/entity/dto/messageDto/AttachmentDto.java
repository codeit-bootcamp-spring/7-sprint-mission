package com.sprint.mission.discodeit.entity.dto.messageDto;

import lombok.Builder;

@Builder
public record AttachmentDto(byte[] file, String fileName, String fileType) {
}
