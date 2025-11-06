package com.sprint.mission.discodeit.dto.binarycontent.request;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

public record BinaryContentUpdateReq(
        UUID binaryContentId,
        byte[] data,
        String fileName,
        String fileType
){
    public static BinaryContentUpdateReq from(UUID binaryContentId, MultipartFile file) throws IOException, IOException {
        return new BinaryContentUpdateReq(
                binaryContentId,
                file.getBytes(),
                file.getOriginalFilename(),
                file.getContentType()
        );
    }
}
