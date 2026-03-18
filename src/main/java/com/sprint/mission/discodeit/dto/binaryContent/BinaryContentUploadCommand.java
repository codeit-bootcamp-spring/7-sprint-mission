package com.sprint.mission.discodeit.dto.binaryContent;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Slf4j
public record BinaryContentUploadCommand(
        String fileName,
        String contentType,
        byte[] bytes,
        Long size,
        UUID userId
) {
    public static BinaryContentUploadCommand from(MultipartFile file) {
        try {
            byte[] bytes = file.getBytes();
            long size = bytes.length;
            return new BinaryContentUploadCommand(
                    file.getOriginalFilename(),
                    file.getContentType(),
                    file.getBytes(),
                    size,
                    null
            );
        } catch (IOException e) {
            log.error("파일 업로드 중 바이너리 읽기 실패 fileName={}", file.getOriginalFilename(), e);
            throw new RuntimeException("파일 읽기 실패", e);
        }
    }


    public static BinaryContentUploadCommand from(MultipartFile file, UUID userId) {
        try {
            byte[] bytes = file.getBytes();
            long size = bytes.length;
            return new BinaryContentUploadCommand(
                    file.getOriginalFilename(),
                    file.getContentType(),
                    file.getBytes(),
                    size,
                    userId
            );
        } catch (IOException e) {
            log.error("파일 업로드 중 바이너리 읽기 실패 fileName={}", file.getOriginalFilename(), e);
            throw new RuntimeException("파일 읽기 실패", e);
        }
    }
}
