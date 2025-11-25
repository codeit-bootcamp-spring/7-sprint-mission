package com.sprint.mission.discodeit.dto.channel.request;

import jakarta.validation.constraints.Pattern;

public record ChannelCreateReq(
    @Pattern(regexp = "^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{1,6}$")
    String name,
    String description
) {

}
