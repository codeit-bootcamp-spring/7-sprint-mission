package com.sprint.mission.discodeit.mapper.dto;

import java.util.UUID;

public record AttachmentsDto(
    UUID id,
    String fileName,
    Long size,
    String contentType
) {

}
