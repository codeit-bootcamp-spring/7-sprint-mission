package com.sprint.mission.discodeit.dto.request.binaryContent;

import com.sprint.mission.discodeit.entity.BinaryContentUsage;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BinaryContentCreateRequestDto {

    private byte[] binaryFile;
    private BinaryContentUsage binaryContentUsage;
}
