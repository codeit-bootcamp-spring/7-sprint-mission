package com.sprint.mission.discodeit.entity.dto;

import com.sprint.mission.discodeit.entity.BinaryContent;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.util.UUID;

@Builder
// 🚫 편의상 변경. CamelCase 위반 -> 추후 수정 할꺼예용~
public record Res_BinaryContent( //all private final
        @NotBlank(message = "binaryContentId is mandatory")
        UUID binaryContentId,
        String fileName,
        String contentType,
        byte[] bytes,
        Long fileSize
) {
    public static Res_BinaryContent from(BinaryContent binaryContent) {
        return Res_BinaryContent.builder()
                .binaryContentId(binaryContent.getId())
                .fileName(binaryContent.getFileName())
                .contentType(binaryContent.getContentType())
                .bytes(binaryContent.getData())
                .fileSize(binaryContent.getFileSize())
                .build();
    }
}
