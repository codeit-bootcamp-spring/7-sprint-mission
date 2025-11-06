package com.sprint.mission.discodeit.dto.binarycontent.response;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.util.DateTimeUtil;

import java.util.UUID;

public record BinaryContentInfoRes(
        UUID BinaryContentId,
        byte[] data,
        String fileName,
        String fileType,
        String createdAt
){
    public static BinaryContentInfoRes from(BinaryContent binaryContent){
        return new BinaryContentInfoRes(
                binaryContent.getId(),
                binaryContent.getData(),
                binaryContent.getFileName(),
                binaryContent.getFileType(),
                DateTimeUtil.format(binaryContent.getCreatedAt())
        );
    }
}
