package com.sprint.mission.discodeit.dto.Binarycontent.response;

import com.sprint.mission.discodeit.dto.Binarycontent.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.entity.content.BinaryContent;
import com.sprint.mission.discodeit.entity.content.ContentsType;

import java.util.UUID;

public record BinaryContentResponse(
        UUID binaryContentId,//바이너리아이디
        UUID typeUUID,//주된 바이너리 uuid
        ContentsType contentsType,//그 바이너리의 파일이냐 이미지냐
        UUID contentsId//해당하는 건탠츠 아이디
) {
    public static BinaryContentResponse from(BinaryContent binaryContent) {
        return new BinaryContentResponse(
                binaryContent.getId(),
                binaryContent.getTypeUUID(),
                binaryContent.getContentsType(),
                binaryContent.getContentsId()
        );
    }
}
