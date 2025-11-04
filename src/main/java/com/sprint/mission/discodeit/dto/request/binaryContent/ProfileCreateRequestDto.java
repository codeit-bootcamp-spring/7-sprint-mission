package com.sprint.mission.discodeit.dto.request.binaryContent;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ProfileCreateRequestDto {

    private byte[] file;
}
