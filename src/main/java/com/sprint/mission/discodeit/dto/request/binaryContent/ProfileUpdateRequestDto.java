package com.sprint.mission.discodeit.dto.request.binaryContent;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProfileUpdateRequestDto {
    private byte[] file;
}
