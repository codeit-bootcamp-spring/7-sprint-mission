package com.sprint.mission.discodeit.dto.request;

import com.sprint.mission.discodeit.enum_.ChannelType;
import jakarta.validation.constraints.NotBlank;

import java.util.List;
import java.util.UUID;

public record CreatePrivateChannelDto(

    List<UUID> participantIds
) {

}
