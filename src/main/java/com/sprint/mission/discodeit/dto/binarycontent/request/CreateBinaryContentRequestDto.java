package com.sprint.mission.discodeit.dto.binarycontent.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CreateBinaryContentRequestDto {
    String fileName;
    String contentType;
    byte[] bytes;
}
