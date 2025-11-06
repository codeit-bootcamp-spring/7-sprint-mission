package com.sprint.mission.discodeit.dto.binarycontent.request;

import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public record BinaryContentCreateReq(
        byte[] data,
        String fileName,
        String fileType
){
    public static BinaryContentCreateReq from(MultipartFile file){
        if(file.isEmpty()) throw  new CustomException(ErrorCode.INVALID_FILE_REQUEST);
        try {
            return new BinaryContentCreateReq(
                    file.getBytes(),
                    file.getOriginalFilename(),
                    file.getContentType()
            );
        } catch (IOException e) {
            throw new CustomException(ErrorCode.FILE_CONVERSION_FAILED);
        }
    }
}
