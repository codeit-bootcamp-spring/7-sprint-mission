package com.sprint.mission.discodeit.dto.binarycontent.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateBinaryContentRequestDto {
    String fileName;
    String contentType;
    private final byte[] bytes;
}
