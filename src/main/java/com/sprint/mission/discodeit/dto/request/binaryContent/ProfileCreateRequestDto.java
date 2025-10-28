package com.sprint.mission.discodeit.dto.request.binaryContent;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProfileCreateRequestDto {

    private byte[] file;
}
