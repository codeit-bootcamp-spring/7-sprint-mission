package com.sprint.mission.discodeit.dto.binarycontent.response;

import java.util.UUID;

public record BinaryContentInfoRes(
    UUID binaryContentId,
    String fileName,
    String fileType,
    long fileSize
) {

}
