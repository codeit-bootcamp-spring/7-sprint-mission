package com.sprint.mission.discodeit.dto.request.message;

import com.sprint.mission.discodeit.dto.request.binaryContent.ProfileCreateRequestDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;


public record MessageCreateRequestDto(

        String content,

        @NotNull(message = "message author id")
        UUID authorId,

        @NotNull(message = "message channel id")
        UUID channelId
) {


}
