package com.sprint.mission.discodeit.entity.dto;

import lombok.Builder;

@Builder
public record Dto_BinaryContent(
//    final UUID id,
//    final Instant createdAt,
//    Instant updatedAt,

        String fileName,
        String contentType,
        byte[] bytes,
        Long fileSize ) {
    public static Dto_BinaryContent from(String fileName, String contentType, byte[] bytes, Long fileSize) {
        return Dto_BinaryContent.builder()
                .fileName(fileName)
                .contentType(contentType)
                .bytes(bytes)
                .fileSize(fileSize)
                .build();
    }
}
