package com.sprint.mission.discodeit.entity.dto.binaryContentDto;

import com.sprint.mission.discodeit.entity.BinaryContent;
import lombok.Builder;

import java.time.Instant;
import java.util.Arrays;
import java.util.UUID;

@Builder
public record BinaryContentResponseDto(
        UUID id,
        UUID userId,
        UUID messageId,
        byte[] data,
        String dataName,
        String dataType,
        Instant createdAt) {

    public static BinaryContentResponseDto from(BinaryContent binaryContent) {
        return BinaryContentResponseDto.builder()
                .id(binaryContent.getId())
                .userId(binaryContent.getUserId())
                .messageId(binaryContent.getMessageId())
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
