package com.sprint.mission.discodeit.dto.Binarycontent.request;

import com.sprint.mission.discodeit.entity.content.BinaryContent;
import com.sprint.mission.discodeit.entity.content.ContentsType;

import java.util.UUID;

public record BinaryContentCreateRequest(
        UUID typeUUID,
        ContentsType contentsType,
        UUID contentsId
) {

}
