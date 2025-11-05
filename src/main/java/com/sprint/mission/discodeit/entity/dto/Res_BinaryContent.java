package com.sprint.mission.discodeit.entity.dto;

import com.sprint.mission.discodeit.entity.BinaryContent;
import lombok.Builder;

import java.util.UUID;

//@Getter
//@NoArgsConstructor
//@AllArgsConstructor
@Builder
// 🚫 편의상 변경. CamelCase 위반 -> 추후 수정 할꺼예용~
public record Res_BinaryContent(
        UUID id
//        Instant createdAt,
//        Instant updatedAt,
//
//        String fileName,
//        String contentType,
//        byte[] data,
//        Long size
) {
//    public static Res_BinaryContent from(String fileName, String contentType, byte[] data, Long size) {
//        return Res_BinaryContent.builder()
////                                .readStatusID(UUID.randomUUID())
////                                .createdAt(Instant.now())
////                                .updatedAt(Instant.now())
//                .fileName(fileName)
//                .contentType(contentType)
//                .data(data)
//                .size(size)
//                                .build();
//    }

    public static Res_BinaryContent from(BinaryContent binaryContent) {
        return Res_BinaryContent.builder()
//                                .readStatusID(UUID.randomUUID())
//                                .createdAt(Instant.now())
//                                .updatedAt(Instant.now())
//                .fileName(binaryContent.getFileName())
//                .contentType(binaryContent.getContentType())
//                .data(binaryContent.getData())
//                .size(binaryContent.getSize())
                .id(binaryContent.getId())
                .build();
    }
}
