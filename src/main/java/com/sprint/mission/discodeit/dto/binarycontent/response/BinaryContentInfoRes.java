package com.sprint.mission.discodeit.dto.binarycontent.response;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.util.DateTimeUtil;

public record BinaryContentInfoRes(
        byte[] data,
        String fileName,
        String fileType,
        String createdAt
){
    public static BinaryContentInfoRes from(BinaryContent binaryContent){
        return new BinaryContentInfoRes(
                binaryContent.getData(),
                binaryContent.getFileName(),
                binaryContent.getFileType(),
                DateTimeUtil.format(binaryContent.getCreatedAt())
        );
    }
}
