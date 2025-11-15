package com.sprint.mission.discodeit.dto.archive.request;

import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.ChannelVisibility;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateChannelRequestDto {
    private final ChannelType channelType;
    private final ChannelVisibility channelVisibility;
    private final String description; // 설명은 없어도 되므로 제외

    @NotBlank(message = "채널 이름은 필수 값입니다.")
    private final String channelName;
}
