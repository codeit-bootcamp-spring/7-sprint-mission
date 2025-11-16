package com.sprint.mission.discodeit.entity.dto;

import java.util.UUID;


// 🚫 편의상 변경. CamelCase 위반 -> 추후 수정 할꺼예용~
public record MessageCreateRequest( //all private final,
    //@NotBlank(message = "content is mandatory")
    String content,
    //@NotBlank(message = "channelId is mandatory")
    UUID channelId,
    //@NotBlank(message = "authorId is mandatory")
    UUID authorId
) {
    public static MessageCreateRequest from(UUID channelId, UUID authorid, String content) {
        return new MessageCreateRequest(
            content,
            channelId,
            authorid
        );
    }
}