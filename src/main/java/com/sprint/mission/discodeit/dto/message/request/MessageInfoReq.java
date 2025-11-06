package com.sprint.mission.discodeit.dto.message.request;

import jakarta.validation.constraints.NotBlank;

public record MessageInfoReq(
    @NotBlank
    String content
){}
