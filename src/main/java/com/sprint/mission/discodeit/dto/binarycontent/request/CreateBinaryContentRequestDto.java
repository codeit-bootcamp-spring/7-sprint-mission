package com.sprint.mission.discodeit.dto.binarycontent.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@AllArgsConstructor
public class CreateBinaryContentRequestDto {
    private final MultipartFile file;
}
