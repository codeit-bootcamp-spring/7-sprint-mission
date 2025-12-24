package com.sprint.mission.discodeit.dto.request;


import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.UUID;

public record CreatePrivateChannelDto(

    @Size(min = 1)
    List<UUID> participantIds
) {

}
