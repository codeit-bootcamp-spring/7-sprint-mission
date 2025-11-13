package com.sprint.mission.discodeit.entity.dto.channelDto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class PublicChannelCreateRequest {
    @NotBlank(message = "채널 이름은 비워둘 수 없습니다.")
    private String name;
    private String description;
}
