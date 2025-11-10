package com.sprint.mission.discodeit.dto.message.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateMessageRequestDto {
    private final String content;
}
