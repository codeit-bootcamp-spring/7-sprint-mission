package com.sprint.mission.discodeit.dto.response;


import java.util.UUID;

public record BinaryContentResponseDto(
    UUID id,
    String fileName,
    Long size,
    String contentType,
    byte[] bytes//표준 데이터
) {

}
