package com.sprint.mission.discodeit.dto.channel.request;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record MessageGetByChannelIdRequest(
    @NotNull(message = "아이디는 필수입니다.")
    UUID channelId

) {

}
