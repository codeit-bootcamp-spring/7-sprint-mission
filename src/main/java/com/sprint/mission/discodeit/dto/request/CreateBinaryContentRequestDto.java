package com.sprint.mission.discodeit.dto.request;

import org.springframework.web.multipart.MultipartFile;

public record CreateBinaryContentRequestDto(

    String fileName,
    String contentType,
    byte[] bytes
) {

}


