package com.sprint.mission.discodeit.dto.request.binarycontent;

import com.sprint.mission.discodeit.common.exception.binarycontent.InvalidBinaryContentRequestException;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public record BinaryContentCreateRequestDto(
        @NotBlank(message = "파일 이름이 필요합니다.")
        @Size(max = 255)
        String fileName,

        @NotBlank(message = "내용을 입력해주세요.")
        @Size(max = 100)
        String contentType,

        @NotNull
        byte[] data) {

    public static BinaryContentCreateRequestDto from(MultipartFile file) {
        if(file == null || file.isEmpty()) {
            throw new InvalidBinaryContentRequestException("파일이 비어있습니다.");
        }
        try {
            return new BinaryContentCreateRequestDto(
                    file.getOriginalFilename(),
                    file.getContentType(),
                    file.getBytes()
            );
        } catch (IOException e) {
            throw new InvalidBinaryContentRequestException("파일을 읽을 수 없습니다.");
        }
    }
}
