package com.sprint.mission.discodeit.dto.Binarycontent.response;

import com.sprint.mission.discodeit.dto.Binarycontent.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.entity.content.BinaryContent;
import com.sprint.mission.discodeit.entity.content.ContentsType;

import java.util.UUID;

public record BinaryContentResponse(

        String contentsType,
        byte[] contentByte
) {
    public static BinaryContentResponse from(BinaryContent binaryContent) {
        return new BinaryContentResponse(
                binaryContent.getContentType(),
                binaryContent.getBytes()
        );
    }
}
