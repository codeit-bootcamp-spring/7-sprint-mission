package com.sprint.mission.discodeit.dto.channel.request;

import jakarta.validation.constraints.Pattern;

public record ChannelCreateReq(
    @Pattern(regexp = "^(?!\\\\s).{2,}$")
    String name,
    String description
) {

}
