package com.sprint.mission.discodeit.dto.message.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateMessageDto(
    @NotBlank(message = "메세지 내용은 필수입니다.")
    String content,

    @NotNull(message = "authorId는 필수입니다.")
    UUID authorId,

    @NotNull(message = "channelId는 필수입니다.")
    UUID channelId
) {

}
