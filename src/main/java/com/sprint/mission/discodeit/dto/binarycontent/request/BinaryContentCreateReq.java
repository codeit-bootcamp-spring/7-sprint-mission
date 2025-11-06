package com.sprint.mission.discodeit.dto.binarycontent.request;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public record BinaryContentCreateReq(
        byte[] data,
        String fileName,
        String fileType
){
    public static BinaryContentCreateReq from(MultipartFile file) throws IOException {
        return new BinaryContentCreateReq(
                file.getBytes(),
                file.getOriginalFilename(),
                file.getContentType()
        );
    }
}
