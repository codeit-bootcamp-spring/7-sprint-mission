package com.sprint.mission.discodeit.entity.binaryContent.dto;

import com.sprint.mission.discodeit.entity.binaryContent.BinaryContent;
import lombok.Builder;

import java.time.Instant;
import java.util.Arrays;
import java.util.UUID;

@Builder
public record BinaryContentInfoDto(
        UUID userId,
        UUID messageId,
        UUID id,
        byte[] data,
        String dataName,
        String dataType,
        Instant createdAt) {

    public static BinaryContentInfoDto from(BinaryContent binaryContent) {
        return BinaryContentInfoDto.builder()
                .userId(binaryContent.getUserId())
                .messageId(binaryContent.getMessageId())
                .id(binaryContent.getId())
                .data(binaryContent.getBinaryData())
                .dataName(binaryContent.getDataName())
                .dataType(binaryContent.getDataType())
                .createdAt(binaryContent.getCreateAt())
                .build();
    }

    @Override
    public String toString() {  // 이미지 출력은 어떻게 하는거지
        return Arrays.toString(data);
    }
}
