package com.sprint.mission.discodeit.entity.dto.channelDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class PublicChannelRequestDto {
    @NotBlank(message = "채널 이름은 비워둘 수 없습니다.")
    private String name;
    private String description;
}
