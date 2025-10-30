package com.sprint.mission.discodeit.dto.message.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class UpdateMessageRequestDto {
    private UUID id;
    private String content;
}
