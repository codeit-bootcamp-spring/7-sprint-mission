package com.sprint.mission.discodeit.dto.channel.request;

import jakarta.validation.constraints.NotBlank;

public record ChannelUpdateReq(
    @NotBlank String name,
    String description
) {

}
