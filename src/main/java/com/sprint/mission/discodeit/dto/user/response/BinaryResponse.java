package com.sprint.mission.discodeit.dto.user.response;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.content.BinaryContent;

import java.util.UUID;

public record BinaryResponse(
        //내용물만 담긴 저장소를 스트링으로 구분했다고 치고 그 주소를 반환
         UUID binaryContentId
) {

    public static BinaryResponse from(BinaryContent binaryContent) {
        return new BinaryResponse(
                binaryContent.getId()
        );
    }
}
