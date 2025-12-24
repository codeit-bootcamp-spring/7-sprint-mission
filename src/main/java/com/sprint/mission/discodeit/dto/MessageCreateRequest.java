package com.sprint.mission.discodeit.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Getter;

public record MessageCreateRequest( //all private final,
//    @NotBlank - err
    String content,
    @NotNull
    UUID channelId,
    @NotNull
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