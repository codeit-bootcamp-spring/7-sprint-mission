package com.sprint.mission.discodeit.dto;

public record Dto_BinaryContent( //all private final
        String fileName,
        String contentType,
        byte[] bytes,
        Long size
) {
    public static Dto_BinaryContent from(String fileName, String contentType, byte[] bytes, Long fileSize) {
        return new Dto_BinaryContent(
            fileName,
            contentType,
            bytes,
            fileSize
        );
    }
}
