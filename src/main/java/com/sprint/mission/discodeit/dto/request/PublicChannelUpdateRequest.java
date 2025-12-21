package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.Size;

public record PublicChannelUpdateRequest(
        @Size(min = 1, max = 100, message = "채널명은 1~100자 사이여야 합니다.")
        String newName,

        @Size(max = 500, message = "설명은 500자 이하여야 합니다.")
        String newDescription
) {

}
