package com.sprint.mission.discodeit.dto.request;

import com.sprint.mission.discodeit.enum_.ChannelType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;


public record CreatePublicChannelDto(

    @NotBlank(message = "채널 이름은 필수입니다.")
    String name, //채널 이름

    @NotBlank(message = "채널 설명은 필수입니다")
    String description //채널 설명
) {

}
