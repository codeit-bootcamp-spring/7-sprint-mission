package com.sprint.mission.discodeit.mapper.dto;

import java.util.UUID;
import lombok.Builder;

@Builder
public record BinaryContentDto(
    UUID id,
    String fileName,
    Long size,
    String contentType
//    byte[] bytes
) {

}
