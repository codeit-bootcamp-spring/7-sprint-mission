package com.sprint.mission.discodeit.dto.archive.request;

import com.sprint.mission.discodeit.entity.ChannelType;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CreateChannelRequestDto {
    ChannelType channelType;
    String description; // 설명은 없어도 되므로 제외

    @NotBlank(message = "채널 이름은 필수 값입니다.")
    private final String channelName;
}
