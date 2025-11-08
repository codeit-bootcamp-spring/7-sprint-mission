package com.sprint.mission.discodeit.dto.response.binarycontent;

import com.sprint.mission.discodeit.entity.BinaryContent;

import java.time.Instant;
import java.util.Base64;
import java.util.UUID;

public record BinaryContentResponseDto(
        UUID id,
        Instant createdAt,
        String fileName,
        String contentType,
        String bytes
        /*byte[] data */) {

    public static BinaryContentResponseDto from(BinaryContent bc) {
        String encodedString = Base64.getEncoder().encodeToString(bc.getData());
        return new BinaryContentResponseDto(
                bc.getId(),
                bc.getCreatedAt(),
                bc.getFileName(),
                bc.getContentType(),
                encodedString
        );
    }
}
