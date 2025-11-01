package com.sprint.mission.discodeit.dto.binarycontent.request;

public record BinaryContentCreateReq(
        byte[] data,
        String fileName,
        String fileType
){
}
