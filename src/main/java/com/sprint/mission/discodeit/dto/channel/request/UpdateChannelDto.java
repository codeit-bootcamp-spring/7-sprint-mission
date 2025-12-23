package com.sprint.mission.discodeit.dto.channel.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateChannelDto(
        @NotBlank(message = "채널명은 필수입니다.")
        String newName,
        
        @NotBlank(message = "채널 설명은 필수입니다.")
        String newDescription
) {

}
