package com.sprint.mission.discodeit.dto.request;

import java.util.List;
import java.util.UUID;

public record CreateMessageRequestDto(

    String content, // 메시지
    UUID channelId, // 채널ID
    UUID authorId // 유저ID
) {

}
