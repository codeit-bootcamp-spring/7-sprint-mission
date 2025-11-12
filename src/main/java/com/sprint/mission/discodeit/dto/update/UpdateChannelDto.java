package com.sprint.mission.discodeit.dto.update;

import com.sprint.mission.discodeit.enum_.ChannelType;

import java.util.UUID;

public record UpdateChannelDto(
    String newName,
    String newDescription
) {

}
