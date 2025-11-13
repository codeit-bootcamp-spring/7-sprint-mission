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

@Getter
public class MessageCreateRequestDto {

    private String content;
    @NotNull (message = "message author id")
    private UUID authorId;
    //private boolean isMarkDown;
    @NotNull(message = "message channel id")
    private UUID channelId;
//    private Set<ProfileCreateRequestDto> attachments  = new HashSet<>();
}
