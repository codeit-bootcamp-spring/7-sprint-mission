package com.sprint.mission.discodeit.dto.user.request;

import com.sprint.mission.discodeit.entity.content.ContentsType;

import java.util.UUID;

public record BinaryRequest(
        //메시지면 메시지 uuid와 담긴 파일
        //유저면 유저 uuid와 담긴 이미지
        //그러니 uuid  ,담길건탠츠 , 그걸저장할 저장소 id
          UUID typeUUID,
          ContentsType contentsType,
          String contentsId
) {
}
