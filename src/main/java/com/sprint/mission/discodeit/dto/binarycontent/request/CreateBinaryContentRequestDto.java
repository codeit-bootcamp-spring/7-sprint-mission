package com.sprint.mission.discodeit.dto.binarycontent.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateBinaryContentRequestDto {
    private byte[] content;
}
