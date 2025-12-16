package com.sprint.mission.discodeit.dto.request.channel;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashSet;
import java.util.UUID;


public record ChannelPublicCreateRequestDto(


        @NotBlank(message = "채널 이름은 필수값입니다")
        String name,

        @NotBlank(message = "채널 설명은 필수값입니다")
        String description
) {


}
