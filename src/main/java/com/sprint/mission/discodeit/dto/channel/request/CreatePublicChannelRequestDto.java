package com.sprint.mission.discodeit.dto.channel.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CreatePublicChannelRequestDto {

    @NotBlank(message = "채널 이름은 필수 값입니다.")
    String name;
    String description; // 설명은 없어도 되므로 검증 제외
}
