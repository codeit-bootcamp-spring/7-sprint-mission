package com.sprint.mission.discodeit.dto.request.binaryContent;

import com.sprint.mission.discodeit.entityElement.BinaryContentUsage;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BinaryContentCreateRequestDto {
    private final byte[] bytes;
    private String fileName;
    private Long size;
    private String contentType;
}
