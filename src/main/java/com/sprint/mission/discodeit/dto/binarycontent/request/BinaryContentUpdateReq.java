package com.sprint.mission.discodeit.dto.binarycontent.request;

import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

public record BinaryContentUpdateReq(
        UUID binaryContentId,
        byte[] data,
        String fileName,
        String fileType
){
    public static BinaryContentUpdateReq from(UUID binaryContentId, MultipartFile file){
        if(file.isEmpty()) throw  new CustomException(ErrorCode.INVALID_FILE_REQUEST);
        try {
            return new BinaryContentUpdateReq(
                    binaryContentId,
                    file.getBytes(),
                    file.getOriginalFilename(),
                    file.getContentType()
            );
        } catch (IOException e) {
            throw new CustomException(ErrorCode.FILE_CONVERSION_FAILED);
        }
    }
}
