package com.sprint.mission.discodeit.dto.binarycontent.response;

import java.util.UUID;

public record BinaryContentInfoRes(
    UUID binaryContentId,
    byte[] data,
    String fileName,
    String fileType,
    String createdAt
) {

}
