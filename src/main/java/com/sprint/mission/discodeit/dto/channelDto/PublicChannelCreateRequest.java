package com.sprint.mission.discodeit.dto.channelDto;

import jakarta.validation.constraints.NotBlank;

public record PublicChannelCreateRequest(
        @NotBlank(message = "채널 이름은 비워둘 수 없습니다.")
        String name,
        String description
){
}
