package com.sprint.mission.discodeit.dto.request.channel;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashSet;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class ChannelPublicCreateRequestDto {
    public HashSet<UUID> participantIds;
    @NotBlank(message = "Public Channel name")
    public String name ;
    public String description;
    public boolean isTextChannel;
}
