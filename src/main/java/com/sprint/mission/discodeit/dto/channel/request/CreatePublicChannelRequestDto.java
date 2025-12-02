package com.sprint.mission.discodeit.dto.channel.request;

import jakarta.validation.constraints.NotBlank;

public record CreatePublicChannelRequestDto(
        @NotBlank(message = "채널 이름은 필수 값입니다.")
        String name,
        String description // 설명은 없어도 되므로 검증 제외
) { }
