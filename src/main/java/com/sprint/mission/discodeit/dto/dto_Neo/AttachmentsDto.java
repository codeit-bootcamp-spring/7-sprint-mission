package com.sprint.mission.discodeit.dto.dto_Neo;

import java.util.UUID;

public record AttachmentsDto(
    UUID id,
    String fileName,
    Long size,
    String contentType
) {

}
