package com.sprint.mission.discodeit.dto.request.binarycontent;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public record BinaryContentCreateRequestDto(
        @NotBlank(message = "파일 이름이 필요합니다.")
        String fileName,

        @NotBlank(message = "내용을 입력해주세요.")
        String contentType,

        @NotNull
        byte[] data) {

    public static BinaryContentCreateRequestDto from(MultipartFile file) {
        if(file == null || file.isEmpty()) {
            throw new IllegalArgumentException("file이 비어있습니다.");
        }
        try {
            return new BinaryContentCreateRequestDto(
                    file.getOriginalFilename(),
                    file.getContentType(),
                    file.getBytes()
            );
        } catch (IOException e) {
            throw new IllegalArgumentException("파일을 읽을 수 없습니다.",e);
        }
    }
}
