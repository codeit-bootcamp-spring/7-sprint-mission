package com.sprint.mission.discodeit.entity.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.util.UUID;

@Builder
// 🚫 편의상 변경. CamelCase 위반 -> 추후 수정 할꺼예용~
public record Dto_Message( //all private final
    @NotBlank(message = "channelId is mandatory")
    UUID channelId,
    @NotBlank(message = "authorid is mandatory")
    UUID authorid,
    @NotBlank(message = "message is mandatory")
    String message
) {
    public static Dto_Message from(UUID channelId, UUID authorid, String message) {
        return Dto_Message.builder()
                .channelId(channelId)
                .authorid(authorid)
                .message(message)
                .build();
    }
}