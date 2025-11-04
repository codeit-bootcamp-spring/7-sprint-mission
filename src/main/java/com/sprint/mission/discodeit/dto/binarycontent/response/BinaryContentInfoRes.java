package com.sprint.mission.discodeit.dto.binarycontent.response;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.util.DateTimeUtil;
import lombok.Builder;

@Builder
public record BinaryContentInfoRes(
        byte[] data,
        String fileName,
        String fileType,
        String createdAt
){
    public static BinaryContentInfoRes from(BinaryContent binaryContent){
        return BinaryContentInfoRes.builder()
                .data(binaryContent.getData())
                .fileName(binaryContent.getFileName())
                .fileType(binaryContent.getFileType())
                .createdAt(DateTimeUtil.format(binaryContent.getCreatedAt()))
                .build();
    }
}
