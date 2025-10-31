package com.sprint.mission.discodeit.dto.binaryContent.request;

public record BinaryContentCreateReq(
        byte[] data,
        String fileName,
        String fileType
){
}
