package com.sprint.mission.discodeit.dto.binaryContent.request;

import java.util.UUID;

public record BinaryContentUpdateReq(
        UUID binaryContentId,
        byte[] data,
        String fileName,
        String fileType
){
}
